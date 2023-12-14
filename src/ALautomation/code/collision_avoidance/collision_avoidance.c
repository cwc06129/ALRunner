#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

#include <stdbool.h>
#include <stdlib.h>
#include <math.h>
#include "ecrobot_interface.h"

#define BASE_SPEED_S 30
#define BASE_SPEED_M 70
#define SENSOR_P 1
#define MIN_DIST 30
#define SLOW_DIST 50

typedef enum _Direction_status{Straight, Turn_left, Turn_right} Direction_status;
typedef enum _Speed_status{Stop, Slow_speed, Midium_speed, Fast_speed} Speed_status;
Direction_status motor_direction_status = Straight;
Speed_status motor_speed_status = Stop;

/**/

/* 2022-05-18 void function declaration */
void turnSensor();
void readDistSensor();
void determineSpeed();
void turnCar();
void TASK_controlTask();
void TerminateTask();
/**/

/* 2022-05-18 local variables and function parameters declaration */
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
/**/

// application part.
int ANGLE[2] = {-10, 40};
int obsDistance[4] = {255, 255, 255, 255};
int zone[4] = {2, 2, 2, 2};  /*0 = < MIN_DIST, 1 = < SLOW_DIST, 2 = > SLOW_DIST*/
int motor_speed[2] = {0, 0};

void turnSensor() {
    turnSensor_offset = 10;
    while(fabs(turnSensor_offset) > 3) {
        /* 2022-05-18 use rand instead of stub function */
		// deg = nxt_motor_get_count(MOTOR_S);
		turnSensor_deg = rand();
		/**/
        
        turnSensor_offset = turnSensor_edge - turnSensor_deg;
        if (turnSensor_offset > 0) {
            turnSensor_s_speed = BASE_SPEED_S + SENSOR_P*turnSensor_offset;
        }   
        else {
            turnSensor_s_speed = -BASE_SPEED_S + SENSOR_P*turnSensor_offset;
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
	/* 2022-05-18 use rand instead of stub function */
	// obsDistance[anglePos * 2] = ecrobot_get_sonar_sensor(DISTANCE_L);
	// obsDistance[anglePos * 2 + 1] = ecrobot_get_sonar_sensor(DISTANCE_R);	
	obsDistance[readDistSensor_anglePos * 2] = rand() % 256;
	obsDistance[readDistSensor_anglePos * 2 + 1] = rand() % 256;
	/**/
}


void determineSpeed() {
    // determine which zone the sensor is in
    determineSpeed_j = 0;
    while (determineSpeed_j < 4) {
        //@ assert 0 <= determineSpeed_j < 4;
        if (determineSpeed_obsDistance[determineSpeed_j] < MIN_DIST) {
            //@ assert 0 <= determineSpeed_j < 4;
            zone[determineSpeed_j] = 0;
        }
        else if(determineSpeed_obsDistance[determineSpeed_j] < SLOW_DIST) {
            //@ assert 0 <= determineSpeed_j < 4;
            zone[determineSpeed_j] = 1;
        }
        else zone[determineSpeed_j] = 2;
        determineSpeed_j++;
    }

    // both front sensors inside min range
    if (!zone[0] && !zone[1]) {
        // if both side sensors inside min range, turn on spot
        if (!zone[2] && !zone[3]) {
            motor_speed[0] = BASE_SPEED_S;
            motor_speed[1] = -BASE_SPEED_S;
        }
        // longer distance to right side, turn right and vice versa
        else{
            motor_speed[0] = (determineSpeed_obsDistance[2] < determineSpeed_obsDistance[3]) ? BASE_SPEED_S : -10;
            motor_speed[1] = (determineSpeed_obsDistance[2] > determineSpeed_obsDistance[3]) ? BASE_SPEED_S : -10;
        }
    }
    // if left front sensor is in min range but not front right, turn right
    else if(!zone[0] && zone[1]) {
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
        determineSpeed_s = (motor_speed[0] > motor_speed[1]) ? motor_speed[0] : motor_speed[1];
        motor_speed[0] = determineSpeed_s;
        motor_speed[1] = determineSpeed_s;
        // if one of the side sensors is inside min range
        if ((!zone[2] || !zone[3]) && (zone[2] != zone[3])) {
            // if left side sensor is inside min range, turn right and vice versa
            motor_speed[0] = (!zone[2]) ? BASE_SPEED_S : determineSpeed_obsDistance[2]-20;
            motor_speed[1] = (!zone[3]) ? BASE_SPEED_S : determineSpeed_obsDistance[3]-20;
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

    /* 2022-12-20 output인 motor_speed를 추상화 한 변수 할당 */
    if (motor_speed[0] == motor_speed[1]) {
        motor_direction_status = Straight;
    } else if (motor_speed[0] > motor_speed[1]) {
        motor_direction_status = Turn_right;
    } else {
        motor_direction_status = Turn_left;
    }

    if ((motor_speed[0] + motor_speed[1]) == 0) {
        motor_speed_status = Stop;
    } else if ((motor_speed[0] <= BASE_SPEED_S) && (motor_speed[1] <= BASE_SPEED_S)) {
        motor_speed_status = Slow_speed;
    } else if ((motor_speed[0] <= BASE_SPEED_M) && (motor_speed[1] <= BASE_SPEED_M)) {
        motor_speed_status = Midium_speed;
    } else {
        motor_speed_status = Fast_speed;
    }
    /**/
}

void turnCar() {
    ecrobot_set_motor_speed_port_id = MOTOR_L;
    ecrobot_set_motor_speed_speed = turnCar_l;
    ecrobot_set_motor_speed();
    ecrobot_set_motor_speed_port_id = MOTOR_R;
    ecrobot_set_motor_speed_speed = turnCar_r;
    ecrobot_set_motor_speed();
}

void TASK_controlTask() {
    FUNC_controlTask_i = 0;
    // while (true) {
    for(int i=0; i<100; i++) {
        //@ assert 0 <= FUNC_controlTask_i <= 1;
        turnSensor_edge = ANGLE[FUNC_controlTask_i];
        // turnSensor();
        systick_wait_ms_a = 200;
        systick_wait_ms();
        readDistSensor_anglePos = FUNC_controlTask_i;
        readDistSensor();
        determineSpeed_obsDistance = obsDistance;
        determineSpeed();
        turnCar_l = motor_speed[0];
        turnCar_r = motor_speed[1];
        turnCar();
        FUNC_controlTask_i = !FUNC_controlTask_i;
    }
    TerminateTask();
}

void TerminateTask() {}

// main part.
void main() {
    srand(time(NULL));
    TASK_controlTask();
}