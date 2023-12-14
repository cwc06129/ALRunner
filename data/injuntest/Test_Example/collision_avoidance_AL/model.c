#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

#include <stdbool.h>
#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include "ecrobot_interface.h"
#include "model.h"

#define BASE_SPEED_S 30
#define BASE_SPEED_M 70
#define SENSOR_P 1
#define MIN_DIST 30
#define SLOW_DIST 50

int ANGLE[2] = {-10, 40};
int obsDistance[4] = {255, 255, 255, 255};
int zone[4] = {2, 2, 2, 2};  /* 0 = < MIN_DIST, 1 = < SLOW_DIST, 2 = > SLOW_DIST */
int motor_speed[2] = {0, 0};

/* 2022-05-18 void function declaration */
void turnCar();
void turnSensor();
// void readDistSensor();
void determineSpeed();
void ControlTask();
void TerminateTask();
/**/

/* 2022-05-18 local variables and function parameters declaration */
int turnCar_l;
int turnCar_r;
int turnSensor_edge;
int turnSensor_offset;
int turnSensor_s_speed;
int turnSensor_deg;
int readDistSensor_anglePos;
int determineSpeed_j;
int FUNC_ControlTask_i;
/**/

STATE rt_state;
INPUT rt_input;
OUTPUT rt_output;

char * direction_str[] = {"On_spot", "Turn_left", "Turn_right"};
char * speed_str[] = {"Stop", "Slow_speed", "Midium_speed", "Fast_speed"};

void turnCar() {
	ecrobot_set_motor_speed_port_id = MOTOR_L;
	ecrobot_set_motor_speed_speed = turnCar_l;
	ecrobot_set_motor_speed();
	ecrobot_set_motor_speed_port_id = MOTOR_R;
	ecrobot_set_motor_speed_speed = turnCar_r;
	ecrobot_set_motor_speed();
}

void turnSensor() {
	turnSensor_offset = 10;
	while (fabs(turnSensor_offset) > 3) {
		/* 2022-05-18 use rand instead of stub function */
		// deg = nxt_motor_get_count(MOTOR_S);
		if (rand() % 2) {
			turnSensor_deg = -11;
		} else {
			turnSensor_deg = 39;
		}
		/**/

		turnSensor_offset = turnSensor_edge - turnSensor_deg;
		if (turnSensor_offset > 0) {
			turnSensor_s_speed = BASE_SPEED_S + SENSOR_P * turnSensor_offset;
		}	
		else {
			turnSensor_s_speed = -BASE_SPEED_S + SENSOR_P * turnSensor_offset;
		}
		turnSensor_s_speed = (turnSensor_s_speed > 100) ? 100 : turnSensor_s_speed; 
		turnSensor_s_speed = (turnSensor_s_speed < 100) ? -100 : turnSensor_s_speed;
		ecrobot_set_motor_speed_port_id = MOTOR_S;
		ecrobot_set_motor_speed_speed = turnSensor_s_speed;
		ecrobot_set_motor_speed();
		systick_wait_ms_a = 30;
		systick_wait_ms();
	}
	ecrobot_set_motor_speed_port_id = MOTOR_S;
	ecrobot_set_motor_speed_speed = 0;
	ecrobot_set_motor_speed();
}

// void readDistSensor() {
// 	/* 2022-05-18 use rand instead of stub function */
// 	// obsDistance[anglePos * 2] = ecrobot_get_sonar_sensor(DISTANCE_L);
// 	// obsDistance[anglePos * 2 + 1] = ecrobot_get_sonar_sensor(DISTANCE_R);	
// 	obsDistance[readDistSensor_anglePos * 2] = rand() % 256;
// 	obsDistance[readDistSensor_anglePos * 2 + 1] = rand() % 256;
// 	/**/
// }

void determineSpeed() {
	// determine which rt_state.zone the sensor is in
	for (determineSpeed_j = 0; determineSpeed_j < 4; determineSpeed_j++) {
		if (rt_input.obsDistance[determineSpeed_j] < MIN_DIST) {
			rt_state.zone[determineSpeed_j] = 0;
		}
		else if (rt_input.obsDistance[determineSpeed_j] < SLOW_DIST) {
			rt_state.zone[determineSpeed_j] = 1;
		}
		else rt_state.zone[determineSpeed_j] = 2;
	}
	// both front sensors inside min range
	if (!rt_state.zone[0] && !rt_state.zone[1]) {
		// if both side sensors inside min range, turn on spot
		if (!rt_state.zone[2] && !rt_state.zone[3]) {
			motor_speed[0] = BASE_SPEED_S;
			motor_speed[1] = -BASE_SPEED_S;
		}
		// longer distance to right side, turn right and vice versa
		else {
			motor_speed[0] = (rt_input.obsDistance[2] < rt_input.obsDistance[3]) ? BASE_SPEED_S : -10;
			motor_speed[1] = (rt_input.obsDistance[2] > rt_input.obsDistance[3]) ? BASE_SPEED_S : -10;
		}
	}
	// if left front sensor is in min range but not front right, turn right
	else if (!rt_state.zone[0] && rt_state.zone[1]) {
		motor_speed[0] = BASE_SPEED_S;
		motor_speed[1] = -10;
	}
	// if right front sensor is in min range but not front left, turn left
	else if (!rt_state.zone[1] && rt_state.zone[0]) {
		motor_speed[1] = BASE_SPEED_S;
		motor_speed[0] = -10;
	}
	// if both front sensors dont see anything
	else if (rt_state.zone[0] == 2 && rt_state.zone[1] == 2) {
		int s = (motor_speed[0] > motor_speed[1]) ? motor_speed[0] : motor_speed[1];
		motor_speed[0] = s;
		motor_speed[1] = s;
		// if one of the side sensors is inside min range
		if ((!rt_state.zone[2] || !rt_state.zone[3]) && (rt_state.zone[2] != rt_state.zone[3])) {
			// if left side sensor is inside min range, turn right and vice versa
			motor_speed[0] = (!rt_state.zone[2]) ? BASE_SPEED_S : rt_input.obsDistance[2]-20;
			motor_speed[1] = (!rt_state.zone[3]) ? BASE_SPEED_S : rt_input.obsDistance[3]-20;
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
		// if both front sensors is in rt_state.zone 1, the speed is decreased towards BASE_SPEED_S
		if (rt_state.zone[0] == rt_state.zone[1]) {
			motor_speed[0] += 0.5 * (BASE_SPEED_S - motor_speed[0]);
			motor_speed[1] += 0.5 * (BASE_SPEED_S - motor_speed[1]);
		}
		// if only front right sensor is in rt_state.zone 1, decrease speed and turn left
		else if (rt_state.zone[1] == 1) {
			motor_speed[1] += 0.3 * (BASE_SPEED_S - motor_speed[1]);
			motor_speed[0] += 0.5 * (BASE_SPEED_S - motor_speed[0]);
		}
		// if only front left sensor is in rt_state.zone 1, decrease speed and turn right
		else {
			motor_speed[1] += 0.5*(BASE_SPEED_S - motor_speed[1]);
			motor_speed[0] += 0.3*(BASE_SPEED_S - motor_speed[0]);
		}
	}
}

void TerminateTask() {};

void model_step(void) {
	FUNC_ControlTask_i = 0;
	
	turnSensor_edge = ANGLE[FUNC_ControlTask_i];
	turnSensor();
	systick_wait_ms_a = 200;
	systick_wait_ms();
	readDistSensor_anglePos = FUNC_ControlTask_i;
	// readDistSensor();
	printf("%s %s %d %d %d %d %d %d %d %d\n", direction_str[rt_state.direction_status], speed_str[rt_state.speed_status], rt_input.obsDistance[0], rt_input.obsDistance[1], rt_input.obsDistance[2], rt_input.obsDistance[3], rt_state.zone[0], rt_state.zone[1], rt_state.zone[2], rt_state.zone[3]);
	determineSpeed();
	if (motor_speed[0] == motor_speed[1]) {
        rt_state.direction_status = On_spot;
    } else if (motor_speed[0] > motor_speed[1]) {
        rt_state.direction_status = Turn_right;
    } else {
        rt_state.direction_status = Turn_left;
    }

    if ((motor_speed[0] + motor_speed[1]) == 0) {
        rt_state.speed_status = Stop;
    } else if ((motor_speed[0] <= BASE_SPEED_S) && (motor_speed[1] <= BASE_SPEED_S)) {
        rt_state.speed_status = Slow_speed;
    } else if ((motor_speed[0] <= BASE_SPEED_M) && (motor_speed[1] <= BASE_SPEED_M)) {
        rt_state.speed_status = Midium_speed;
    } else {
        rt_state.speed_status = Fast_speed;
    }
	turnCar_l = motor_speed[0];
	turnCar_r = motor_speed[1];
	turnCar();
	FUNC_ControlTask_i = !FUNC_ControlTask_i;

	//TerminateTask();
}

void model_initialize(void) {
	for(int i=0; i<4; i++) {
		rt_input.obsDistance[i] = 0;
	}
	rt_state.direction_status = On_spot;
	rt_state.speed_status = Stop;
	for(int i=0; i<4; i++) {
		rt_state.zone[i] = 0;
	}
	determineSpeed_j = 0;
}

