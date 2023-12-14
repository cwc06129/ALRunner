#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

#include <stdlib.h>
#include <math.h>
#include "kernel.h"
#include "kernel_id.h"
#include "ecrobot_interface.h"

#define DISTANCE_L NXT_PORT_S4
#define DISTANCE_R NXT_PORT_S3
#define BUTTON_L NXT_PORT_S1
#define BUTTON_R NXT_PORT_S2
#define MOTOR_R NXT_PORT_A
#define MOTOR_L NXT_PORT_C
#define MOTOR_S NXT_PORT_B
#define BASE_SPEED_S 30
#define BASE_SPEED_M 70
#define SENSOR_P 1
#define MIN_DIST 30
#define SLOW_DIST 50

DeclareTask(controlTask);

int ANGLE[2] = {-10, 40};
int obsDistance[4] = {255, 255, 255, 255};
int zone[4] = {2, 2, 2, 2};  /* 0 = < MIN_DIST, 1 = < SLOW_DIST, 2 = > SLOW_DIST */
int motor_speed[2] = {0, 0};

void ecrobot_device_initialize() {
    ecrobot_init_sonar_sensor(DISTANCE_L);
    ecrobot_init_sonar_sensor(DISTANCE_R);
    nxt_motor_set_count(MOTOR_S, 0);
}

void ecrobot_device_terminate() {
    ecrobot_term_sonar_sensor(DISTANCE_L);
    ecrobot_term_sonar_sensor(DISTANCE_R);
    ecrobot_set_motor_speed(MOTOR_L, 0);
    ecrobot_set_motor_speed(MOTOR_R, 0);
    ecrobot_set_motor_speed(MOTOR_S, 0);
}

void user_1ms_isr_type2(void) {}

void turnCar(int l, int r) {
	ecrobot_set_motor_speed(MOTOR_L, l);
	ecrobot_set_motor_speed(MOTOR_R, r);
}

void turnSensor(int edge) {
	int offset = 10;
	int s_speed,deg;
	while (fabs(offset) > 3) {
		deg = nxt_motor_get_count(MOTOR_S);
		offset = edge - deg;
		if (offset > 0) {
			s_speed = BASE_SPEED_S + SENSOR_P * offset;			
		}	
		else {
			s_speed = -BASE_SPEED_S + SENSOR_P * offset;			
		}
		s_speed = (s_speed > 100) ? 100 : s_speed; 
		s_speed = (s_speed < 100) ? -100 : s_speed; 
		ecrobot_set_motor_speed(MOTOR_S, s_speed);
		systick_wait_ms(30);
	}
	ecrobot_set_motor_speed(MOTOR_S, 0);
}

void readDistSensor(int anglePos) {
	obsDistance[anglePos * 2] = ecrobot_get_sonar_sensor(DISTANCE_L);
	obsDistance[anglePos * 2 + 1] = ecrobot_get_sonar_sensor(DISTANCE_R);	
}
void determineSpeed() {
	// determine which zone the sensor is in
	for (int j = 0; j < 4; j++) {
		if (obsDistance[j] < MIN_DIST) {
			zone[j] = 0;
		}
		else if (obsDistance[j] < SLOW_DIST) {
			zone[j] = 1;
		}
		else zone[j] = 2;
	}
	// both front sensors inside min range
	if (!zone[0] && !zone[1]) {
		// if both side sensors inside min range, turn on spot
		if (!zone[2] && !zone[3]) {
			motor_speed[0] = BASE_SPEED_S;
			motor_speed[1] = -BASE_SPEED_S;
		}
		// longer distance to right side, turn right and vice versa
		else {
			motor_speed[0] = (obsDistance[2] < obsDistance[3]) ? BASE_SPEED_S : -10;
			motor_speed[1] = (obsDistance[2] > obsDistance[3]) ? BASE_SPEED_S : -10;
		}
	}
	// if left front sensor is in min range but not front right, turn right
	else if (!zone[0] && zone[1]) {
		motor_speed[0] = BASE_SPEED_S;
		motor_speed[1] = -10;
	}
	// if right front sensor is in min range but not front left, turn left
	else if (!zone[1] && zone[0]) {
		motor_speed[1] = BASE_SPEED_S;
		motor_speed[0] = -10;
	}
	// if both front sensors dont see anything
	else if (zone[0] == 2 && zone[1] == 2) {
		int s = (motor_speed[0] > motor_speed[1]) ? motor_speed[0] : motor_speed[1];
		motor_speed[0] = s;
		motor_speed[1] = s;
		// if one of the side sensors is inside min range
		if ((!zone[2] || !zone[3]) && (zone[2] != zone[3])) {
			// if left side sensor is inside min range, turn right and vice versa
			motor_speed[0] = (!zone[2]) ? BASE_SPEED_S : obsDistance[2] - 20;
			motor_speed[1] = (!zone[3]) ? BASE_SPEED_S : obsDistance[3] - 20;
		}
		// if nothing on the side increase speed towards BASE_SPEED_M
		else {
			if (motor_speed[0] < BASE_SPEED_M) {
				motor_speed[0] += 0.5 * (BASE_SPEED_M - motor_speed[0]);
				motor_speed[1] += 0.5 * (BASE_SPEED_M - motor_speed[1]);
			}
			else {
				motor_speed[0] = BASE_SPEED_M;
				motor_speed[1] = BASE_SPEED_M;
			}
		}
	}
	else {
		// if both front sensors is in zone 1, the speed is decreased towards BASE_SPEED_S
		if (zone[0] == zone[1]) {
			motor_speed[0] += 0.5 * (BASE_SPEED_S - motor_speed[0]);
			motor_speed[1] += 0.5 * (BASE_SPEED_S - motor_speed[1]);
		}
		// if only front right sensor is in zone 1, decrease speed and turn left
		else if (zone[1] == 1) {
			motor_speed[1] += 0.3 * (BASE_SPEED_S - motor_speed[1]);
			motor_speed[0] += 0.5 * (BASE_SPEED_S - motor_speed[0]);
		}
		// if only front left sensor is in zone 1, decrease speed and turn right
		else {
			motor_speed[1] += 0.5 * (BASE_SPEED_S - motor_speed[1]);
			motor_speed[0] += 0.3 * (BASE_SPEED_S - motor_speed[0]);
		}
	}
}

TASK(controlTask) {
	int i = 0;
	while(true) {
		turnSensor(ANGLE[i]);
		systick_wait_ms(200);
		readDistSensor(i);
		determineSpeed(obsDistance);
		turnCar(motor_speed[0], motor_speed[1]);
		i = !i;
	}
	TerminateTask();
}