#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

// #include "elev.h"
#include <stdlib.h>
#include <stdio.h>
#include "model.h"
#include "orders.h"
#include "timer.h"

//----------------VARIABLER-------------
// elevState currentState = INIT;
// int lastFloorAfterEmergency = -1;
// int previousMainFloor = -1;
// int motorDirection = 0;
			
INPUT rt_input;
STATE rt_state;
OUTPUT rt_output;

char * state_str[] = {"INIT", "IDLE", "RUN", "STOP", "EMERGENCY"};

//----------------FUNKSJONER-------------
void model_step() {
	int previndex = 0;
	int currentindex = 0;
	// setOrdersHigh(); //oppdater ordre
	if(rt_state.previousState == -1) {
		previndex = 4;
	} else {
		previndex = rt_state.previousState;
	}

	if(rt_state.currentState == -1) {
		currentindex = 4;
	} else {
		currentindex = rt_state.currentState;
	}

	printf("%s %s %d %d %d %d %d %d %d", state_str[previndex], state_str[currentindex], rt_state.motorDirection, rt_state.previousMainFloor, rt_state.lastFloorAfterEmergency, rt_output.door_lamp, rt_output.stop_lamp, rt_input.currentFloorLocation, rt_input.stopSignal);
	
	for(int i=0; i<3; i++) {
		for(int j=0; j<4; j++) {
			printf(" %d", rt_input.orderMatrix[i][j]);
		}
	}
	printf("\n");	
	//sjekk ordre, viss case emergency eller stopp skal alt ignoreres
	if ((rt_input.currentFloorLocation != -1) || (rt_state.lastFloorAfterEmergency != -1)) {    
		if (rt_state.lastFloorAfterEmergency != -1) {
			rt_state.previousMainFloor = rt_state.lastFloorAfterEmergency;
		}
		else {
			rt_state.previousMainFloor = rt_input.currentFloorLocation;
		}
		
		// elev_set_floor_indicator(rt_state.previousMainFloor);

		switch(rt_state.previousState) {

			case INIT:
				break; // vil ikke skje

			case IDLE:
				// 만약 버튼이 눌렸다면, motorDirection 세팅해주고, RUN 상태로 전이
				if(isButtonPressed()) {
					rt_state.motorDirection = setDirection(rt_state.previousMainFloor, rt_state.motorDirection);
					// elev_set_motor_direction(rt_state.motorDirection); // set inn motor direction
					rt_state.previousState = RUN;
				}		
				break;

			case RUN:
				// 만약 현재 층수와 눌린 버튼의 층수가 같다면, STOP 상태로 전이
				// 만약 그렇지 않다면, 상태를 전이하지 않음.
				if(floorIsOrdered(rt_state.previousMainFloor, rt_state.motorDirection)) {
					rt_state.previousState = STOP;
				}
				rt_state.lastFloorAfterEmergency = -1; // gyldig tilstand	
				break;

			case STOP:

				// elev_set_motor_direction(0); 
				rt_state.motorDirection = 0;
				
				//sjekk timer, om den er gått ut skal state settes til IDLE
				if(!isTimerActive() && (rt_input.currentFloorLocation!=-1)) {
					startTimer(3);
					// elev_set_door_open_lamp(1);
					rt_output.door_lamp = 1;
				}
				if(isTimerActive() == 1){
					if(floorIsOrdered(rt_state.previousMainFloor, rt_state.motorDirection)){
						startTimer(3);
						removeFromOrderMatrix(rt_state.previousMainFloor);
					}	
				}
				if(getTimerStatus()) {
					rt_state.previousState = IDLE;
					timerDeactivate();
					// elev_set_door_open_lamp(0);
					rt_output.door_lamp = 0;
				}	
				break;

			case EMERGENCY:
				// do nuthin', skal ikke måtte implementeres
				break;
		}
	}

	// Stopp-knapp skal sette til emergency
	// if(elev_get_stop_signal()){
	if (rt_input.stopSignal) {
		if(emergencyStopHandler() == -1){ // viss stopp mellom etasjer
			rt_state.previousState = IDLE;
			rt_state.lastFloorAfterEmergency = rt_state.previousMainFloor;
		}
		else {
			rt_state.previousState = STOP;
		}
	}
}

void initStates() {
	/* 2023-01-26 불필요한 코드 제거. */
	// //kjør til en etasje
	// if (elev_get_floor_sensor_signal() == -1) {
	// 	elev_set_motor_direction(1);
	// 	while(elev_get_floor_sensor_signal() == -1) {
	// 	}
	// }
	// // stopp, sett i IDLE
	// elev_set_motor_direction(0);
	/**/

	rt_state.previousState = IDLE;
	rt_state.currentState = IDLE;
}

void model_initialize() {
	for(int i=0; i<3; i++) {
		for(int j=0; j<4; j++) {
			rt_input.orderMatrix[i][j] = 0;
		}
	}
	rt_input.currentFloorLocation = -1;
	rt_input.stopSignal = 0;
	rt_state.previousState = INIT;
	rt_state.currentState = IDLE;
	rt_state.previousMainFloor = -1;
	rt_state.lastFloorAfterEmergency = -1;
	rt_state.motorDirection = 0;
	rt_output.door_lamp = 0;
	rt_output.stop_lamp = 0;
}

int emergencyStopHandler(){	
	
	switch(rt_state.currentState) {

	case INIT:
		return 0;

	case IDLE:
		rt_state.currentState = EMERGENCY;
		// elev_set_stop_lamp(1);
		rt_output.stop_lamp = 1;
		// elev_set_motor_direction(0);
	
		break;
	case RUN:
	
		rt_state.currentState = EMERGENCY;
		// elev_set_stop_lamp(1);
		rt_output.stop_lamp = 1;
		// elev_set_motor_direction(0);

		if(rt_input.currentFloorLocation != -1) {
			// elev_set_door_open_lamp(1);
			rt_output.door_lamp = 1;
		}

		else {
			rt_state.currentState = IDLE;
		}
		break;
	case STOP:
		rt_state.currentState = EMERGENCY;
		// elev_set_stop_lamp(1);
		rt_output.stop_lamp = 1;

		rt_state.currentState = IDLE;
		break;

	case EMERGENCY:
	    flushOrders();	// slett ordre
		// elev_set_motor_direction(0);
		// while(elev_get_stop_signal()) {
		// 	// busy loop mens kanppen holdes
		// }

		// elev_set_stop_lamp(0);
		rt_output.stop_lamp = 0;
		
		if(rt_input.currentFloorLocation == -1) { //viss stopp mellom etasjer
			rt_state.currentState = IDLE;
			// elev_set_door_open_lamp(0);
			rt_output.door_lamp = 0;
			return -1;	
		}
		else{ // viss stopp i etasje
			// elev_set_stop_lamp(0);
			rt_output.stop_lamp = 0;
			rt_state.currentState = STOP;
			// elev_set_door_open_lamp(1);
			rt_output.door_lamp = 1;
			return 1;
		}
	}
	return -1; // safety
}