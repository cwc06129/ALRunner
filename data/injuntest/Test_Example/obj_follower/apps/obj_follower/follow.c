#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include "ecrobot_interface.h"
#include "camera.h"
#include "filter.h"
#include "lib.h"

#include "osek.h"
#include "platform.h"

#define PORT_TOUCH_SENSOR		NXT_PORT_S1
#define PORT_MOTOR_LEFT			NXT_PORT_B
#define PORT_MOTOR_RIGHT		NXT_PORT_A
#define PORT_CAMERA				NXT_PORT_S2

/* Camera color id of the tracked object */
const int  COLORID =				0;

/* Minimum dectected area recognized as an object by the camera */
const int  MIN_DETECTED_AREA =		-1;

/* References for the control algorithms */
const int  SIZE_REFERENCE =			50;
const int  POSITION_REFERENCE =		83;

/* Maximum total speed fed to a motor */
const int  MAX_SPEED =				100;
/* Normal speed of without any adjustments from the PID controllers */
const int  NORMAL_SPEED =			50;
/* Maximum speed where the robot does not move forward */
const int  STALL_SPEED =			50;
/* Speed used for spinning */
const int  SPIN_SPEED = 			65;

/* Mutex lock for shared object data */
DeclareResource(dataMutex);

/* Declaring all tasks */
DeclareTask(AcquisitionTask);
DeclareTask(ControlTask);
DeclareTask(DisplayTask);

typedef struct {
	int position;
	int size;
} object_data_t;

/* Global variables used to display information */
static int sizeLCD, areaLCD, xLCD, speedLCD, devspeedLCD, diradjLCD, lmotLCD,
		rmotLCD;

/* Resource containing acquired data. Protected by mutex */
object_data_t objData = { 0, 0 };

/* Device initialization */
void ecrobot_device_initialize() {
	init_nxtcam(PORT_CAMERA);
}
void ecrobot_device_terminate() {
	nxt_motor_set_speed(PORT_MOTOR_RIGHT, 0, 1);
	nxt_motor_set_speed(PORT_MOTOR_LEFT, 0, 1);
	term_nxtcam(PORT_CAMERA);
}

/* We use a timer for scheduling tasks, so we need to have an ISR handler, even if empty. */
void user_1ms_isr_type2(void) {
}

	S8 tracking_enabled = -1;
	int acq_rectindex, area, acq_size, x;

/* Acquire data from sensors */
TASK(AcquisitionTask) {}
// jump to
// jump_AcquisitionTask();

// schedule_init(AcquisitionTask, 1);
void Func_AcquisitionTask_1() {
	if (tracking_enabled != 0) {
		// to 2
		current_pc[AcquisitionTask] = 2;
	} else {
		// to 5
		current_pc[AcquisitionTask] = 5;
	}
}
// schedule(AcquisitionTask, 2);
		//Enable object tracking
void Func_AcquisitionTask_2() {
		tracking_enabled = send_nxtcam_command(PORT_CAMERA, ENABLE_TRACKING);
		current_pc[AcquisitionTask] = 3;
}
	// } else {

// schedule(AcquisitionTask, 5);
void Func_AcquisitionTask_5() {
		//Requests data from the camera
		request(PORT_CAMERA);

		acq_rectindex = getbiggestrect(COLORID, MIN_DETECTED_AREA);

		current_pc[AcquisitionTask] = 6;
}
void Func_AcquisitionTask_6() {
// schedule(AcquisitionTask, 6);
		if (acq_rectindex >= 0) {
			// to 7
			current_pc[AcquisitionTask] = 7;
		} else {
			// to 3
			current_pc[AcquisitionTask] = 3;
		}
}
void Func_AcquisitionTask_7() {
// schedule(AcquisitionTask, 7);
			//Read data from the camera
			area = getArea(acq_rectindex);
			//Filter the sample
			area = median_filter(area);
			//Extract the object size from the area
			acq_size = fisqrt(area);
			//Get the position of the tracked object
			x = getX(acq_rectindex);

			printf("\t\t- acq_rectindex: %d\n", acq_rectindex); fflush(stdout);
			printf("\t\t- acq_size: %d\n", acq_size); fflush(stdout);
			printf("\t\t- x: %d\n", x); fflush(stdout);

			current_pc[AcquisitionTask] = 8;
}
void Func_AcquisitionTask_8() {
// schedule(AcquisitionTask, 8);
			//Update the struct containing object data
			GetResource(dataMutex);
	current_pc[AcquisitionTask] = 9;
}
void Func_AcquisitionTask_9() {
// schedule(AcquisitionTask, 9);
			objData.position = x;
			objData.size = acq_size;
	current_pc[AcquisitionTask] = 10;
}
void Func_AcquisitionTask_10() {
			ReleaseResource(dataMutex);
	current_pc[AcquisitionTask] = 11;
}
void Func_AcquisitionTask_11() {
// schedule(AcquisitionTask, 12);
			//Prepare data for display
			sizeLCD = acq_size;
			areaLCD = area;
			xLCD = x;
	current_pc[AcquisitionTask] = 3;
}
		// }

	// }
void Func_AcquisitionTask_3() {
// schedule(AcquisitionTask, 3);
	current_pc[AcquisitionTask] = 4;
}
void Func_AcquisitionTask_4() {
	TerminateTask();
	current_pc[AcquisitionTask] = 1;
}
// schedule(AcquisitionTask, 4);
// schedule(AcquisitionTask, 13); // omited
// schedule_reset(AcquisitionTask, 1);
// }

/*********** Functions used by the Control Task **************/

/* Gets data from the mutex protected structure */
object_data_t getData() {
	object_data_t temp;

	GetResource(dataMutex);
	temp = objData;
	ReleaseResource(dataMutex);
	return temp;
}


/* PID constants for speed control */
#define sKp 1.5
#define sKi 0
#define sKd 0

int integral = 0;
int prevError = 0;

/* Returns the calculated adjustment according to desired speed
 * It is applied on top of the NORMAL_SPEED
 */
int speedPIDController(int d) {
	int error = (SIZE_REFERENCE - d);
	integral += error;
	int derivative = error - prevError;
	int out = (sKp * error) + (sKi * integral) + (sKd * derivative);
	prevError = error;

	return out;
}

/* PID constants for direction control */
#define dKp 0.2
#define dKi 0
#define dKd 0

/* Returns the calculated adjustment according to desired direction
 * It is applied on top of the NORMAL_SPEED + new_speed
 */
int directionPIDController(int d) {
	int error = (POSITION_REFERENCE - d);
	integral += error;
	int derivative = error - prevError;
	int out = (dKp * error) + (dKi * integral) + (dKd * derivative);
	prevError = error;

	return out;
}



	//The current object data
	object_data_t data;

	//Distance to the object
	int ctl_position, ctl_size;
	int new_speed, direction_adjustment;
	int speed_deviation;

	//Motor speed values
	int leftMotorValue;
	int rightMotorValue;

/* Apply control algorithm on the acquired data and then output to motors */
TASK(ControlTask) {}
// jump to
// jump_ControlTask();

void Func_ControlTask_15() {
// schedule_init(ControlTask, 15);
	current_pc[ControlTask] = 16;
}
void Func_ControlTask_16() {
// schedule(ControlTask, 16);
	GetResource(dataMutex);
	current_pc[ControlTask] = 17;
}
void Func_ControlTask_17() {
// schedule(ControlTask, 17);
	data = objData;
	current_pc[ControlTask] = 18;
}
void Func_ControlTask_18() {
	ReleaseResource(dataMutex);
	current_pc[ControlTask] = 19;
}
void Func_ControlTask_19() {
	ctl_size = data.size;
	ctl_position = data.position;

	printf("\t\t- ctl_size: %d\n", ctl_size); fflush(stdout);
	printf("\t\t- ctl_position: %d\n", ctl_position); fflush(stdout);

	current_pc[ControlTask] = 20;
}
void Func_ControlTask_20() {
// schedule(ControlTask, 20);
// schedule(ControlTask, 21); // DW: this node is not necessery but does not affect anything
	if (ctl_size > 0 && ctl_position > 0) {
		// to 22
		current_pc[ControlTask] = 21;
	} else {
		// to 36
		current_pc[ControlTask] = 29;
	}
}
void Func_ControlTask_21() {
// schedule(ControlTask, 22);
		//If an object was detected we apply the PID control
		speed_deviation = speedPIDController(ctl_size);

		devspeedLCD = speed_deviation;

		new_speed = NORMAL_SPEED + speed_deviation;

		printf("\t\t- new_speed: %d\n", new_speed); fflush(stdout);

		current_pc[ControlTask] = 22;
}
void Func_ControlTask_22() {
	new_speed = (new_speed > MAX_SPEED) ? MAX_SPEED : new_speed;
	current_pc[ControlTask] = 23;
}
void Func_ControlTask_23() {
// schedule(ControlTask, 25);
		//Calculate speed adjustment to adjust direction
		direction_adjustment = directionPIDController(ctl_position);
		
		printf("\t\t- direction_adjustment: %d\n", direction_adjustment); fflush(stdout);

		//Update display values
		speedLCD = new_speed;
		diradjLCD = direction_adjustment;

		leftMotorValue = new_speed - direction_adjustment;

		printf("\t\t- leftMotorValue: %d\n", leftMotorValue); fflush(stdout);
		
		current_pc[ControlTask] = 24;
}
void Func_ControlTask_24() {
// schedule(ControlTask, 26);
		leftMotorValue = (leftMotorValue > MAX_SPEED) ? MAX_SPEED : leftMotorValue;
		current_pc[ControlTask] = 25;
}
void Func_ControlTask_25() {
// schedule(ControlTask, 28);

		rightMotorValue = new_speed + direction_adjustment;

		printf("\t\t- rightMotorValue: %d\n", rightMotorValue); fflush(stdout);
		
	current_pc[ControlTask] = 26;
}
void Func_ControlTask_26() {
		rightMotorValue = (rightMotorValue > MAX_SPEED) ? MAX_SPEED : rightMotorValue;
		current_pc[ControlTask] = 27;
}
void Func_ControlTask_29() {
// schedule(ControlTask, 36);
		//If no object is detected, we enter spinning mode
		rightMotorValue = SPIN_SPEED;
		leftMotorValue = -SPIN_SPEED;

		//Update display values
		speedLCD = 0;
		diradjLCD = 0;
		devspeedLCD = 0;
	current_pc[ControlTask] = 27;
}
void Func_ControlTask_27() {
// schedule(ControlTask, 31);
	//Update display values
	lmotLCD = leftMotorValue;
	rmotLCD = rightMotorValue;

	//Setting the appropriate speed to the motors
	nxt_motor_set_speed(PORT_MOTOR_LEFT, leftMotorValue, 0);
	nxt_motor_set_speed(PORT_MOTOR_RIGHT, rightMotorValue, 0);

	current_pc[ControlTask] = 28;
}
void Func_ControlTask_28() {
	TerminateTask();
	current_pc[ControlTask] = 15;
}
// schedule(ControlTask, 32);
// schedule(ControlTask, 37); // omited
// schedule_reset(ControlTask, 15);
// }

/*********** Functions used by the Display Task **************/

void display_values(void) {
	char *message = NULL;
	U8 line = 0;

	display_goto_xy(0, line++);
	message = "size:";
	display_string(message);
	display_int(sizeLCD, 4);

	display_goto_xy(0, line++);
	message = "area:";
	display_string(message);
	display_int(areaLCD, 5);

	display_goto_xy(0, line++);
	message = "pos:";
	display_string(message);
	display_int(xLCD, 4);

	display_goto_xy(0, line++);
	message = "speed:";
	display_string(message);
	display_int(speedLCD, 4);

	display_goto_xy(0, line++);
	message = "dev speed:";
	display_string(message);
	display_int(devspeedLCD, 4);

	display_goto_xy(0, line++);
	message = "dir dev:";
	display_string(message);
	display_int(diradjLCD, 4);

	display_goto_xy(0, line++);
	message = "left motor:";
	display_string(message);
	display_int(lmotLCD, 4);

	display_goto_xy(0, line++);
	message = "right motor:";
	display_string(message);
	display_int(rmotLCD, 4);

	display_update();
}

/*Display useful information on the screen*/
// TASK(DisplayTask) {
// // jump to
// jump_DisplayTask();
// schedule_init(DisplayTask, 39);
// 	display_values();
// schedule(DisplayTask, 40);
// 	TerminateTask();
// schedule_reset(DisplayTask, 39);
// }
