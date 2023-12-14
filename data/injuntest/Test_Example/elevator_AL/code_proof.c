#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

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
	STATE state;				
	STATE prev_state;			
	INPUT input;				
	INPUT prev_input;			
	OUTPUT output;				
	OUTPUT prev_output;			

	// enum size assume 
	__CPROVER_assume(prev_state.previousState >= -1 && prev_state.previousState < 4); 
	__CPROVER_assume(prev_state.currentState >= -1 && prev_state.currentState < 4); 
	__CPROVER_assume(prev_state.motorDirection >= -1 && prev_state.motorDirection <= 1); 
	__CPROVER_assume(prev_state.previousMainFloor >= -1 && prev_state.previousMainFloor < 4); 
	__CPROVER_assume(prev_state.lastFloorAfterEmergency >= -1 && prev_state.lastFloorAfterEmergency < 4); 
	__CPROVER_assume(prev_output.door_lamp >= 0 && prev_output.door_lamp < 2); 
	__CPROVER_assume(prev_output.stop_lamp >= 0 && prev_output.stop_lamp < 2); 
	__CPROVER_assume(state.previousState >= -1 && state.previousState < 4); 
	__CPROVER_assume(state.currentState >= -1 && state.currentState < 4); 
	__CPROVER_assume(state.motorDirection >= -1 && state.motorDirection <= 1); 
	__CPROVER_assume(state.previousMainFloor >= -1 && state.previousMainFloor < 4); 
	__CPROVER_assume(state.lastFloorAfterEmergency >= -1 &&  state.lastFloorAfterEmergency < 4); 
	__CPROVER_assume(output.door_lamp >= 0 && output.door_lamp < 2); 
	__CPROVER_assume(output.stop_lamp >= 0 && output.stop_lamp < 2); 
	
	// input assume	
	__CPROVER_assume(prev_input.currentFloorLocation >= -1 && prev_input.currentFloorLocation < 4); 
	__CPROVER_assume(prev_input.stopSignal >= 0 && prev_input.stopSignal < 2); 
	__CPROVER_assume(prev_input.orderMatrix[0][0] >= 0 && prev_input.orderMatrix[0][0] < 2); 
	__CPROVER_assume(prev_input.orderMatrix[0][1] >= 0 && prev_input.orderMatrix[0][1] < 2); 
	__CPROVER_assume(prev_input.orderMatrix[0][2] >= 0 && prev_input.orderMatrix[0][2] < 2); 
	__CPROVER_assume(prev_input.orderMatrix[0][3] >= 0 && prev_input.orderMatrix[0][3] < 2); 
	__CPROVER_assume(prev_input.orderMatrix[1][0] >= 0 && prev_input.orderMatrix[1][0] < 2); 
	__CPROVER_assume(prev_input.orderMatrix[1][1] >= 0 && prev_input.orderMatrix[1][1] < 2); 
	__CPROVER_assume(prev_input.orderMatrix[1][2] >= 0 && prev_input.orderMatrix[1][2] < 2); 
	__CPROVER_assume(prev_input.orderMatrix[1][3] >= 0 && prev_input.orderMatrix[1][3] < 2); 
	__CPROVER_assume(prev_input.orderMatrix[2][0] >= 0 && prev_input.orderMatrix[2][0] < 2); 
	__CPROVER_assume(prev_input.orderMatrix[2][1] >= 0 && prev_input.orderMatrix[2][1] < 2); 
	__CPROVER_assume(prev_input.orderMatrix[2][2] >= 0 && prev_input.orderMatrix[2][2] < 2); 
	__CPROVER_assume(prev_input.orderMatrix[2][3] >= 0 && prev_input.orderMatrix[2][3] < 2); 
	__CPROVER_assume(input.currentFloorLocation >= -1 && input.currentFloorLocation < 4); 
	__CPROVER_assume(input.stopSignal >= 0 && input.stopSignal < 2); 
	__CPROVER_assume(input.orderMatrix[0][0] >= 0 && input.orderMatrix[0][0] < 2); 
	__CPROVER_assume(input.orderMatrix[0][1] >= 0 && input.orderMatrix[0][1] < 2); 
	__CPROVER_assume(input.orderMatrix[0][2] >= 0 && input.orderMatrix[0][2] < 2); 
	__CPROVER_assume(input.orderMatrix[0][3] >= 0 && input.orderMatrix[0][3] < 2); 
	__CPROVER_assume(input.orderMatrix[1][0] >= 0 && input.orderMatrix[1][0] < 2); 
	__CPROVER_assume(input.orderMatrix[1][1] >= 0 && input.orderMatrix[1][1] < 2); 
	__CPROVER_assume(input.orderMatrix[1][2] >= 0 && input.orderMatrix[1][2] < 2); 
	__CPROVER_assume(input.orderMatrix[1][3] >= 0 && input.orderMatrix[1][3] < 2); 
	__CPROVER_assume(input.orderMatrix[2][0] >= 0 && input.orderMatrix[2][0] < 2); 
	__CPROVER_assume(input.orderMatrix[2][1] >= 0 && input.orderMatrix[2][1] < 2); 
	__CPROVER_assume(input.orderMatrix[2][2] >= 0 && input.orderMatrix[2][2] < 2); 
	__CPROVER_assume(input.orderMatrix[2][3] >= 0 && input.orderMatrix[2][3] < 2); 
	
	rt_state = prev_state;		
	rt_input = prev_input;		
	rt_output = prev_output;	
	model_step();				
	state = rt_state;			
	output = rt_output;			

	// auto generated assume 
	__CPROVER_assume((state.previousState == IDLE));
	while(1)	
	{						
		bool found = false;	
							
		rt_state = state;	
		rt_input = input;	
		rt_output = output;	
							
		model_step();		
							
		// input assume		
		INPUT new_input;	
		__CPROVER_assume(new_input.currentFloorLocation >= -1 && new_input.currentFloorLocation < 4); 
		__CPROVER_assume(new_input.stopSignal >= 0 && new_input.stopSignal < 2); 
		__CPROVER_assume(new_input.orderMatrix[0][0] >= 0 && new_input.orderMatrix[0][0] < 2); 
		__CPROVER_assume(new_input.orderMatrix[0][1] >= 0 && new_input.orderMatrix[0][1] < 2); 
		__CPROVER_assume(new_input.orderMatrix[0][2] >= 0 && new_input.orderMatrix[0][2] < 2); 
		__CPROVER_assume(new_input.orderMatrix[0][3] >= 0 && new_input.orderMatrix[0][3] < 2); 
		__CPROVER_assume(new_input.orderMatrix[1][0] >= 0 && new_input.orderMatrix[1][0] < 2); 
		__CPROVER_assume(new_input.orderMatrix[1][1] >= 0 && new_input.orderMatrix[1][1] < 2); 
		__CPROVER_assume(new_input.orderMatrix[1][2] >= 0 && new_input.orderMatrix[1][2] < 2); 
		__CPROVER_assume(new_input.orderMatrix[1][3] >= 0 && new_input.orderMatrix[1][3] < 2); 
		__CPROVER_assume(new_input.orderMatrix[2][0] >= 0 && new_input.orderMatrix[2][0] < 2); 
		__CPROVER_assume(new_input.orderMatrix[2][1] >= 0 && new_input.orderMatrix[2][1] < 2); 
		__CPROVER_assume(new_input.orderMatrix[2][2] >= 0 && new_input.orderMatrix[2][2] < 2); 
		__CPROVER_assume(new_input.orderMatrix[2][3] >= 0 && new_input.orderMatrix[2][3] < 2); 
			printf("OUTPUT: %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d\n", 
state.previousState, state.currentState, state.motorDirection, state.previousMainFloor, state.lastFloorAfterEmergency, output.door_lamp, output.stop_lamp, input.currentFloorLocation, input.stopSignal, input.orderMatrix[0][0], input.orderMatrix[0][1], input.orderMatrix[0][2], input.orderMatrix[0][3], input.orderMatrix[1][0], input.orderMatrix[1][1], input.orderMatrix[1][2], input.orderMatrix[1][3], input.orderMatrix[2][0], input.orderMatrix[2][1], input.orderMatrix[2][2], input.orderMatrix[2][3]);
	printf("OUTPUT: %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d\n", 
		rt_state.previousState, rt_state.currentState, rt_state.motorDirection, rt_state.previousMainFloor, rt_state.lastFloorAfterEmergency, rt_output.door_lamp, rt_output.stop_lamp, new_input.currentFloorLocation, new_input.stopSignal, new_input.orderMatrix[0][0], new_input.orderMatrix[0][1], new_input.orderMatrix[0][2], new_input.orderMatrix[0][3], new_input.orderMatrix[1][0], new_input.orderMatrix[1][1], new_input.orderMatrix[1][2], new_input.orderMatrix[1][3], new_input.orderMatrix[2][0], new_input.orderMatrix[2][1], new_input.orderMatrix[2][2], new_input.orderMatrix[2][3]);
	printf("PROP: assert(!(state.previousState == %d && rt_state.previousState == %d && rt_state.currentState==%d && rt_state.motorDirection==%d && rt_state.previousMainFloor==%d && rt_state.lastFloorAfterEmergency==%d && rt_output.door_lamp==%d && rt_output.stop_lamp==%d && rt_input.currentFloorLocation==%d && rt_input.stopSignal==%d && rt_input.orderMatrix[0][0]==%d && rt_input.orderMatrix[0][1]==%d && rt_input.orderMatrix[0][2]==%d && rt_input.orderMatrix[0][3]==%d && rt_input.orderMatrix[1][0]==%d && rt_input.orderMatrix[1][1]==%d && rt_input.orderMatrix[1][2]==%d && rt_input.orderMatrix[1][3]==%d && rt_input.orderMatrix[2][0]==%d && rt_input.orderMatrix[2][1]==%d && rt_input.orderMatrix[2][2]==%d && rt_input.orderMatrix[2][3]==%d));", 
prev_state.previousState, state.previousState, state.currentState, state.motorDirection, state.previousMainFloor, state.lastFloorAfterEmergency, output.door_lamp, output.stop_lamp, input.currentFloorLocation, input.stopSignal, input.orderMatrix[0][0], input.orderMatrix[0][1], input.orderMatrix[0][2], input.orderMatrix[0][3], input.orderMatrix[1][0], input.orderMatrix[1][1], input.orderMatrix[1][2], input.orderMatrix[1][3], input.orderMatrix[2][0], input.orderMatrix[2][1], input.orderMatrix[2][2], input.orderMatrix[2][3]);
	printf("ASSUME: __CPROVER_assume(!(prev_state.previousState == %d && state.previousState == %d && state.currentState==%d && state.motorDirection==%d && state.previousMainFloor==%d && state.lastFloorAfterEmergency==%d && output.door_lamp==%d && output.stop_lamp==%d && input.currentFloorLocation==%d && input.stopSignal==%d && input.orderMatrix[0][0]==%d && input.orderMatrix[0][1]==%d && input.orderMatrix[0][2]==%d && input.orderMatrix[0][3]==%d && input.orderMatrix[1][0]==%d && input.orderMatrix[1][1]==%d && input.orderMatrix[1][2]==%d && input.orderMatrix[1][3]==%d && input.orderMatrix[2][0]==%d && input.orderMatrix[2][1]==%d && input.orderMatrix[2][2]==%d && input.orderMatrix[2][3]==%d));", 
prev_state.previousState, state.previousState, state.currentState, state.motorDirection, state.previousMainFloor, state.lastFloorAfterEmergency, output.door_lamp, output.stop_lamp, input.currentFloorLocation, input.stopSignal, input.orderMatrix[0][0], input.orderMatrix[0][1], input.orderMatrix[0][2], input.orderMatrix[0][3], input.orderMatrix[1][0], input.orderMatrix[1][1], input.orderMatrix[1][2], input.orderMatrix[1][3], input.orderMatrix[2][0], input.orderMatrix[2][1], input.orderMatrix[2][2], input.orderMatrix[2][3]);
		if (((!(1 == input.stopSignal) && (input.currentFloorLocation <= -1) && !(rt_state.lastFloorAfterEmergency <= -1) && (1 > input.orderMatrix[0][1]) && (1 == input.orderMatrix[2][1])) || (!(1 == input.stopSignal) && (input.currentFloorLocation <= -1) && !(rt_state.lastFloorAfterEmergency <= -1) && (1 > input.orderMatrix[0][1]) && !(1 == input.orderMatrix[2][1]) && (1 == input.orderMatrix[1][1])) || (!(1 == input.stopSignal) && (input.currentFloorLocation <= -1) && !(rt_state.lastFloorAfterEmergency <= -1) && (1 > input.orderMatrix[0][1]) && !(1 == input.orderMatrix[2][1]) && !(1 == input.orderMatrix[1][1]) && (1 > input.orderMatrix[0][2]) && (1 > input.orderMatrix[1][3]) && (1 == input.orderMatrix[1][0])) || (!(1 == input.stopSignal) && (input.currentFloorLocation <= -1) && !(rt_state.lastFloorAfterEmergency <= -1) && (1 > input.orderMatrix[0][1]) && !(1 == input.orderMatrix[2][1]) && !(1 == input.orderMatrix[1][1]) && (1 > input.orderMatrix[0][2]) && (1 > input.orderMatrix[1][3]) && !(1 == input.orderMatrix[1][0]) && (1 > input.orderMatrix[0][3]) && (1 == input.orderMatrix[2][2])) || (!(1 == input.stopSignal) && (input.currentFloorLocation <= -1) && !(rt_state.lastFloorAfterEmergency <= -1) && (1 > input.orderMatrix[0][1]) && !(1 == input.orderMatrix[2][1]) && !(1 == input.orderMatrix[1][1]) && (1 > input.orderMatrix[0][2]) && (1 > input.orderMatrix[1][3]) && !(1 == input.orderMatrix[1][0]) && (1 > input.orderMatrix[0][3]) && !(1 == input.orderMatrix[2][2]) && (1 > input.orderMatrix[2][3]) && (1 > input.orderMatrix[1][2]) && (1 == input.orderMatrix[0][0])) || (!(1 == input.stopSignal) && (input.currentFloorLocation <= -1) && !(rt_state.lastFloorAfterEmergency <= -1) && (1 > input.orderMatrix[0][1]) && !(1 == input.orderMatrix[2][1]) && !(1 == input.orderMatrix[1][1]) && (1 > input.orderMatrix[0][2]) && (1 > input.orderMatrix[1][3]) && !(1 == input.orderMatrix[1][0]) && (1 > input.orderMatrix[0][3]) && !(1 == input.orderMatrix[2][2]) && (1 > input.orderMatrix[2][3]) && (1 > input.orderMatrix[1][2]) && !(1 == input.orderMatrix[0][0]) && (1 == input.orderMatrix[2][0])) || (!(1 == input.stopSignal) && (input.currentFloorLocation <= -1) && !(rt_state.lastFloorAfterEmergency <= -1) && (1 > input.orderMatrix[0][1]) && !(1 == input.orderMatrix[2][1]) && !(1 == input.orderMatrix[1][1]) && (1 > input.orderMatrix[0][2]) && (1 > input.orderMatrix[1][3]) && !(1 == input.orderMatrix[1][0]) && (1 > input.orderMatrix[0][3]) && !(1 == input.orderMatrix[2][2]) && (1 > input.orderMatrix[2][3]) && !(1 > input.orderMatrix[1][2])) || (!(1 == input.stopSignal) && (input.currentFloorLocation <= -1) && !(rt_state.lastFloorAfterEmergency <= -1) && (1 > input.orderMatrix[0][1]) && !(1 == input.orderMatrix[2][1]) && !(1 == input.orderMatrix[1][1]) && (1 > input.orderMatrix[0][2]) && (1 > input.orderMatrix[1][3]) && !(1 == input.orderMatrix[1][0]) && (1 > input.orderMatrix[0][3]) && !(1 == input.orderMatrix[2][2]) && !(1 > input.orderMatrix[2][3])) || (!(1 == input.stopSignal) && (input.currentFloorLocation <= -1) && !(rt_state.lastFloorAfterEmergency <= -1) && (1 > input.orderMatrix[0][1]) && !(1 == input.orderMatrix[2][1]) && !(1 == input.orderMatrix[1][1]) && (1 > input.orderMatrix[0][2]) && (1 > input.orderMatrix[1][3]) && !(1 == input.orderMatrix[1][0]) && !(1 > input.orderMatrix[0][3])) || (!(1 == input.stopSignal) && (input.currentFloorLocation <= -1) && !(rt_state.lastFloorAfterEmergency <= -1) && (1 > input.orderMatrix[0][1]) && !(1 == input.orderMatrix[2][1]) && !(1 == input.orderMatrix[1][1]) && (1 > input.orderMatrix[0][2]) && !(1 > input.orderMatrix[1][3])) || (!(1 == input.stopSignal) && (input.currentFloorLocation <= -1) && !(rt_state.lastFloorAfterEmergency <= -1) && (1 > input.orderMatrix[0][1]) && !(1 == input.orderMatrix[2][1]) && !(1 == input.orderMatrix[1][1]) && !(1 > input.orderMatrix[0][2])) || (!(1 == input.stopSignal) && (input.currentFloorLocation <= -1) && !(rt_state.lastFloorAfterEmergency <= -1) && !(1 > input.orderMatrix[0][1])) || (!(1 == input.stopSignal) && !(input.currentFloorLocation <= -1) && (1 == input.orderMatrix[0][2])) || (!(1 == input.stopSignal) && !(input.currentFloorLocation <= -1) && !(1 == input.orderMatrix[0][2]) && (1 == input.orderMatrix[2][3])) || (!(1 == input.stopSignal) && !(input.currentFloorLocation <= -1) && !(1 == input.orderMatrix[0][2]) && !(1 == input.orderMatrix[2][3]) && (1 == input.orderMatrix[1][2])) || (!(1 == input.stopSignal) && !(input.currentFloorLocation <= -1) && !(1 == input.orderMatrix[0][2]) && !(1 == input.orderMatrix[2][3]) && !(1 == input.orderMatrix[1][2]) && (1 == input.orderMatrix[2][1])) || (!(1 == input.stopSignal) && !(input.currentFloorLocation <= -1) && !(1 == input.orderMatrix[0][2]) && !(1 == input.orderMatrix[2][3]) && !(1 == input.orderMatrix[1][2]) && !(1 == input.orderMatrix[2][1]) && (1 == input.orderMatrix[1][0])) || (!(1 == input.stopSignal) && !(input.currentFloorLocation <= -1) && !(1 == input.orderMatrix[0][2]) && !(1 == input.orderMatrix[2][3]) && !(1 == input.orderMatrix[1][2]) && !(1 == input.orderMatrix[2][1]) && !(1 == input.orderMatrix[1][0]) && (1 > input.orderMatrix[1][3]) && (1 == input.orderMatrix[0][0])) || (!(1 == input.stopSignal) && !(input.currentFloorLocation <= -1) && !(1 == input.orderMatrix[0][2]) && !(1 == input.orderMatrix[2][3]) && !(1 == input.orderMatrix[1][2]) && !(1 == input.orderMatrix[2][1]) && !(1 == input.orderMatrix[1][0]) && (1 > input.orderMatrix[1][3]) && !(1 == input.orderMatrix[0][0]) && (1 == input.orderMatrix[2][0])) || (!(1 == input.stopSignal) && !(input.currentFloorLocation <= -1) && !(1 == input.orderMatrix[0][2]) && !(1 == input.orderMatrix[2][3]) && !(1 == input.orderMatrix[1][2]) && !(1 == input.orderMatrix[2][1]) && !(1 == input.orderMatrix[1][0]) && (1 > input.orderMatrix[1][3]) && !(1 == input.orderMatrix[0][0]) && !(1 == input.orderMatrix[2][0]) && (1 == input.orderMatrix[2][2])) || (!(1 == input.stopSignal) && !(input.currentFloorLocation <= -1) && !(1 == input.orderMatrix[0][2]) && !(1 == input.orderMatrix[2][3]) && !(1 == input.orderMatrix[1][2]) && !(1 == input.orderMatrix[2][1]) && !(1 == input.orderMatrix[1][0]) && (1 > input.orderMatrix[1][3]) && !(1 == input.orderMatrix[0][0]) && !(1 == input.orderMatrix[2][0]) && !(1 == input.orderMatrix[2][2]) && (1 > input.orderMatrix[0][3]) && (1 > input.orderMatrix[1][1]) && (1 == input.orderMatrix[0][1])) || (!(1 == input.stopSignal) && !(input.currentFloorLocation <= -1) && !(1 == input.orderMatrix[0][2]) && !(1 == input.orderMatrix[2][3]) && !(1 == input.orderMatrix[1][2]) && !(1 == input.orderMatrix[2][1]) && !(1 == input.orderMatrix[1][0]) && (1 > input.orderMatrix[1][3]) && !(1 == input.orderMatrix[0][0]) && !(1 == input.orderMatrix[2][0]) && !(1 == input.orderMatrix[2][2]) && (1 > input.orderMatrix[0][3]) && !(1 > input.orderMatrix[1][1])) || (!(1 == input.stopSignal) && !(input.currentFloorLocation <= -1) && !(1 == input.orderMatrix[0][2]) && !(1 == input.orderMatrix[2][3]) && !(1 == input.orderMatrix[1][2]) && !(1 == input.orderMatrix[2][1]) && !(1 == input.orderMatrix[1][0]) && (1 > input.orderMatrix[1][3]) && !(1 == input.orderMatrix[0][0]) && !(1 == input.orderMatrix[2][0]) && !(1 == input.orderMatrix[2][2]) && !(1 > input.orderMatrix[0][3])) || (!(1 == input.stopSignal) && !(input.currentFloorLocation <= -1) && !(1 == input.orderMatrix[0][2]) && !(1 == input.orderMatrix[2][3]) && !(1 == input.orderMatrix[1][2]) && !(1 == input.orderMatrix[2][1]) && !(1 == input.orderMatrix[1][0]) && !(1 > input.orderMatrix[1][3]))))
		{	assert(rt_state.previousState == RUN);
			found = true;}
		if ((((rt_state.currentState == EMERGENCY) && !(1 > input.stopSignal) && (1 == rt_output.door_lamp) && !(1 > input.currentFloorLocation)) || ((rt_state.currentState == EMERGENCY) && !(1 > input.stopSignal) && !(1 == rt_output.door_lamp)) || (!(rt_state.currentState == EMERGENCY) && (rt_state.currentState == INIT) && !(1 > input.stopSignal))))
		{	assert(rt_state.previousState == STOP);
			found = true;}
		if (!found)
			assert((rt_state.previousState == IDLE));
	}
}