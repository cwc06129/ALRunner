#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

// #include "elev.h"
#include <stdlib.h>
#include "states.h"
#include "orders.h"
#include "timer.h"

//----------------VARIABLER-------------

elevState currentState = INIT;
elevState previousState = INIT;

int currentFloorLocation = -1;
int previousMainFloor = -1;
int lastFloorAfterEmergency = -1;
int motorDirection = 0;


//----------------FUNKSJONER-------------
void stateController() {
		// setOrdersHigh(); //oppdater ordre
		currentFloorLocation = rand() % 5 - 1;

		//sjekk ordre, viss case emergency eller stopp skal alt ignoreres
		if ((currentFloorLocation != -1) || (lastFloorAfterEmergency != -1)) {    
			if (lastFloorAfterEmergency != -1) {
				previousMainFloor = lastFloorAfterEmergency;
			}
			else {
				previousMainFloor = currentFloorLocation;
			}
			
			// elev_set_floor_indicator(previousMainFloor);

			switch(previousState) {
	
				case INIT:
					break; // vil ikke skje

				case IDLE:
					if(isButtonPressed()) {
						motorDirection = setDirection(previousMainFloor, motorDirection);
						// elev_set_motor_direction(motorDirection); // set inn motor direction
						previousState = RUN;
					}		
					break;

				case RUN:
					if(floorIsOrdered(previousMainFloor, motorDirection)) {
						previousState = STOP;
					}
					lastFloorAfterEmergency = -1; // gyldig tilstand	
					break;

				case STOP:

					// elev_set_motor_direction(0); 
					motorDirection = 0;
					
					//sjekk timer, om den er gått ut skal state settes til IDLE
					if(!isTimerActive() && (currentFloorLocation!=-1)) {
						startTimer(3);
						// elev_set_door_open_lamp(1);
					}
					if(isTimerActive() == 1){
						if(floorIsOrdered(previousMainFloor, motorDirection)){
							startTimer(3);
							removeFromOrderMatrix(previousMainFloor);
						}	
					}
					if(getTimerStatus()) {
						previousState = IDLE;
						timerDeactivate();
						// elev_set_door_open_lamp(0);
					}	
					break;

				case EMERGENCY:
					// do nuthin', skal ikke måtte implementeres
					break;
			}
		}

		// Stopp-knapp skal sette til emergency
		// if(elev_get_stop_signal()){
		if (rand() % 2) {
			if(emergencyStopHandler() == -1){ // viss stopp mellom etasjer
				previousState = IDLE;
				lastFloorAfterEmergency = previousMainFloor;
			}
			else {
				previousState = STOP;
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

	previousState = IDLE;
	currentState = IDLE;
}


int emergencyStopHandler(){	
	
	switch(currentState) {

	case INIT:
		return 0;

	case IDLE:
		currentState = EMERGENCY;
		// elev_set_stop_lamp(1);
		// elev_set_motor_direction(0);
	
		break;
	case RUN:
	
		currentState = EMERGENCY;
		// elev_set_stop_lamp(1);
		// elev_set_motor_direction(0);

		if(currentFloorLocation != -1) {
			// elev_set_door_open_lamp(1);
		}

		else {
			currentState = IDLE;
		}
		break;
	case STOP:
	
		currentState = EMERGENCY;
		// elev_set_stop_lamp(1);
		// 2023-02-15(Wed) : 관심변수에 output 으로 lamp 추가할 수도 있음.

		currentState = IDLE;
		break;

	case EMERGENCY:
	    flushOrders();	// slett ordre
		// elev_set_motor_direction(0);
		// while(elev_get_stop_signal()) {
		// 	// busy loop mens kanppen holdes
		// }

		// elev_set_stop_lamp(0);
		
		if(currentFloorLocation == -1) { //viss stopp mellom etasjer
			currentState = IDLE;
			// elev_set_door_open_lamp(0);
			return -1;	
		}
		else{ // viss stopp i etasje
			// elev_set_stop_lamp(0);
			currentState = STOP;
			// elev_set_door_open_lamp(1);
			return 1;
		}
	}
	return -1; // safety
}