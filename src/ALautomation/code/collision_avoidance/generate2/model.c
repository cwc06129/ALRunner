#define TASK(n) void TASK_##n
#define FUNC(n,m) k n FUNC_##k
#define BASE_SPEED_S30
#define BASE_SPEED_M70
#define SENSOR_P1
#define MIN_DIST30
#define SLOW_DIST50
#define MOTOR_SNXT_PORT_B
#define MOTOR_LNXT_PORT_C
#define MOTOR_RNXT_PORT_A

#include <stdbool.h>
#include <stdlib.h>
#include <math.h>
#include "ecrobot_interface.h"
#include "model.h"

INPUT rt_input;
STATE rt_state;
OUTPUT rt_output;

int turnSensor_edge;
int turnSensor_offset;
int turnSensor_s_speed;
int turnSensor_deg;
int readDistSensor_anglePos;
int* determineSpeed_obsDistance;
int determineSpeed_j;
int determineSpeed_s;
int turnCar_l;
int turnCar_r;
int FUNC_controlTask_i;
int ANGLE[2] = {-10, 40};
int zone[4] = {2, 2, 2, 2};
 ecrobot_set_motor_speed_port_id;
 ecrobot_set_motor_speed_speed;
 systick_wait_ms_a;

char * motor_direction_status_str[] = {"Straight","Turn_left","Turn_right"};
char * motor_speed_status_str[] = {"Stop","Slow_speed","Midium_speed","Fast_speed"};

void turnSensor() {
	turnSensor_offset = 10;
	while(fabs(turnSensor_offset) > 3){
		turnSensor_deg = rand();
		turnSensor_offset = turnSensor_edge - turnSensor_deg;
		if(turnSensor_offset > 0){
			turnSensor_s_speed = BASE_SPEED_S + SENSOR_P * turnSensor_offset;
		}
		else{
			turnSensor_s_speed = -BASE_SPEED_S + SENSOR_P * turnSensor_offset;
		}
		turnSensor_s_speed = (turnSensor_s_speed > 100) ? 100 : turnSensor_s_speed;
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

void readDistSensor() {
	;
	;
}

void determineSpeed() {
	determineSpeed_j = 0;
	while(determineSpeed_j < 4){
		if(determineSpeed_obsDistance[determineSpeed_j] < MIN_DIST){
			zone[determineSpeed_j] = 0;
		}
		else if(determineSpeed_obsDistance[determineSpeed_j] < SLOW_DIST){
			zone[determineSpeed_j] = 1;
		}
		else{
			zone[determineSpeed_j] = 2;
		}
		determineSpeed_j++;
	}
	if(!zone[0] && !zone[1]){
		if(!zone[2] && !zone[3]){
			rt_state.motor_speed[0] = BASE_SPEED_S;
			rt_state.motor_speed[1] = -BASE_SPEED_S;
		}
		else{
			rt_state.motor_speed[0] = (determineSpeed_obsDistance[2] < determineSpeed_obsDistance[3]) ? BASE_SPEED_S : -10;
			rt_state.motor_speed[1] = (determineSpeed_obsDistance[2] > determineSpeed_obsDistance[3]) ? BASE_SPEED_S : -10;
		}
	}
	else if(!zone[0] && zone[1]){
		rt_state.motor_speed[0] = BASE_SPEED_S;
		rt_state.motor_speed[1] = -10;
	}
	else if(!zone[1] && zone[0]){
		rt_state.motor_speed[1] = BASE_SPEED_S;
		rt_state.motor_speed[0] = -10;
	}
	else if(zone[0] == 2 && zone[1] == 2){
		determineSpeed_s = (rt_state.motor_speed[0] > rt_state.motor_speed[1]) ? rt_state.motor_speed[0] : rt_state.motor_speed[1];
		rt_state.motor_speed[0] = determineSpeed_s;
		rt_state.motor_speed[1] = determineSpeed_s;
		if((!zone[2] || !zone[3]) && (zone[2] != zone[3])){
			rt_state.motor_speed[0] = (!zone[2]) ? BASE_SPEED_S : determineSpeed_obsDistance[2] - 20;
			rt_state.motor_speed[1] = (!zone[3]) ? BASE_SPEED_S : determineSpeed_obsDistance[3] - 20;
		}
		else if(rt_state.motor_speed[0] < BASE_SPEED_M){
			rt_state.motor_speed[0] += 0.5 * (BASE_SPEED_M - rt_state.motor_speed[0]);
			rt_state.motor_speed[1] += 0.5 * (BASE_SPEED_M - rt_state.motor_speed[1]);
		}
		else{
			rt_state.motor_speed[0] = BASE_SPEED_M;
			rt_state.motor_speed[1] = BASE_SPEED_M;
		}
	}
	else if(zone[0] == zone[1]){
		rt_state.motor_speed[0] += 0.5 * (BASE_SPEED_S - rt_state.motor_speed[0]);
		rt_state.motor_speed[1] += 0.5 * (BASE_SPEED_S - rt_state.motor_speed[1]);
	}
	else if(zone[1] == 1){
		rt_state.motor_speed[1] += 0.3 * (BASE_SPEED_S - rt_state.motor_speed[1]);
		rt_state.motor_speed[0] += 0.5 * (BASE_SPEED_S - rt_state.motor_speed[0]);
	}
	else{
		rt_state.motor_speed[1] += 0.5 * (BASE_SPEED_S - rt_state.motor_speed[1]);
		rt_state.motor_speed[0] += 0.3 * (BASE_SPEED_S - rt_state.motor_speed[0]);
	}
	if(rt_state.motor_speed[0] == rt_state.motor_speed[1]){
		rt_state.motor_direction_status = Straight;
	}
	else if(rt_state.motor_speed[0] > rt_state.motor_speed[1]){
		rt_state.motor_direction_status = Turn_right;
	}
	else{
		rt_state.motor_direction_status = Turn_left;
	}
	if((rt_state.motor_speed[0] + rt_state.motor_speed[1]) == 0){
		rt_state.motor_speed_status = Stop;
	}
	else if((rt_state.motor_speed[0] <= BASE_SPEED_S) && (rt_state.motor_speed[1] <= BASE_SPEED_S)){
		rt_state.motor_speed_status = Slow_speed;
	}
	else if((rt_state.motor_speed[0] <= BASE_SPEED_M) && (rt_state.motor_speed[1] <= BASE_SPEED_M)){
		rt_state.motor_speed_status = Midium_speed;
	}
	else{
		rt_state.motor_speed_status = Fast_speed;
	}
}

void turnCar() {
	ecrobot_set_motor_speed_port_id = MOTOR_L;
	ecrobot_set_motor_speed_speed = turnCar_l;
	ecrobot_set_motor_speed();
	ecrobot_set_motor_speed_port_id = MOTOR_R;
	ecrobot_set_motor_speed_speed = turnCar_r;
	ecrobot_set_motor_speed();
}

void model_step() {
	printf("%s %s %d %d %d %d %d %d\n", motor_direction_status_str[rt_state.motor_direction_status], motor_speed_status_str[rt_state.motor_speed_status], rt_state.motor_speed[0], rt_state.motor_speed[1], rt_input.obsDistance[0], rt_input.obsDistance[1], rt_input.obsDistance[2], rt_input.obsDistance[3]);
	FUNC_controlTask_i = 0;
	for(int i = 0 ; i < 100 ; i++){
		turnSensor_edge = ANGLE[FUNC_controlTask_i];
		systick_wait_ms_a = 200;
		systick_wait_ms();
		readDistSensor_anglePos = FUNC_controlTask_i;
		readDistSensor();
		determineSpeed_obsDistance = rt_input.obsDistance;
		determineSpeed();
		turnCar_l = rt_state.motor_speed[0];
		turnCar_r = rt_state.motor_speed[1];
		turnCar();
		FUNC_controlTask_i = !FUNC_controlTask_i;
	}
	TerminateTask();
}

void TerminateTask() {
}





void main() {
	srand(time(NULL));
	model_step();
}



void model_initialize() {
	rt_state.motor_direction_status = Straight;
	rt_state.motor_speed_status = Stop;
	turnSensor_edge = 0;
	turnSensor_offset = 0;
	turnSensor_s_speed = 0;
	turnSensor_deg = 0;
	readDistSensor_anglePos = 0;
	determineSpeed_obsDistance = 0;
	determineSpeed_j = 0;
	determineSpeed_s = 0;
	turnCar_l = 0;
	turnCar_r = 0;
	FUNC_controlTask_i = 0;
	ANGLE[0] = -10;
	ANGLE[1] = 40;
	rt_input.obsDistance[0] = 255;
	rt_input.obsDistance[1] = 255;
	rt_input.obsDistance[2] = 255;
	rt_input.obsDistance[3] = 255;
	zone[0] = 2;
	zone[1] = 2;
	zone[2] = 2;
	zone[3] = 2;
	rt_state.motor_speed[0] = 0;
	rt_state.motor_speed[1] = 0;
	ecrobot_set_motor_speed_port_id;
	ecrobot_set_motor_speed_speed;
	systick_wait_ms_a;
}


