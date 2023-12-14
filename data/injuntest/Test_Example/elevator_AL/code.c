#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

#include<stdio.h>
#include <stddef.h>
#include <stdio.h> 
#include <stdbool.h> 
#include "model.h"
#include "model.c"
#include "timer.h"
#include "timer.c"
#include "orders.h"
#include "orders.c"
int main()
{						
	model_initialize();
	initStates();
	while(1)			
	{								
		OUTPUT output = rt_output;	
		STATE state = rt_state;		
		INPUT input;				
		rt_input = input;			
		model_step();				
									
		assert(!(state.previousState == -1 && rt_state.previousState == 1 && rt_state.currentState==-1 && rt_state.motorDirection==0 && rt_state.previousMainFloor==3 && rt_state.lastFloorAfterEmergency==3 && rt_output.door_lamp==0 && rt_output.stop_lamp==1 && rt_input.currentFloorLocation==0 && rt_input.stopSignal==1 && rt_input.orderMatrix[0][0]==0 && rt_input.orderMatrix[0][1]==1 && rt_input.orderMatrix[0][2]==1 && rt_input.orderMatrix[0][3]==0 && rt_input.orderMatrix[1][0]==1 && rt_input.orderMatrix[1][1]==0 && rt_input.orderMatrix[1][2]==0 && rt_input.orderMatrix[1][3]==0 && rt_input.orderMatrix[2][0]==1 && rt_input.orderMatrix[2][1]==0 && rt_input.orderMatrix[2][2]==1 && rt_input.orderMatrix[2][3]==0));
;
	}
}