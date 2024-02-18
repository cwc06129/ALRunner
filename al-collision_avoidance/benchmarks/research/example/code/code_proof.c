#include <stddef.h>
#include <stdio.h> 
#include <stdbool.h> 
#include "model.h"
#include "model.c"
#include "ecrobot_interface.h"
int main()
{					
	STATE state;				
	STATE prev_state;			
	INPUT input;				
	INPUT prev_input;			
	OUTPUT output;				
	OUTPUT prev_output;			

	// STATE / OUTPUT assume 
	__CPROVER_assume(prev_state.motor_direction_status >= 0 && prev_state.motor_direction_status <= 2); 
	__CPROVER_assume(prev_state.motor_speed_status >= 0 && prev_state.motor_speed_status <= 3); 
	__CPROVER_assume(prev_state.motor_speed[0] >= -2 && prev_state.motor_speed[0] <= 23); 
	__CPROVER_assume(prev_state.motor_speed[1] >= -2 && prev_state.motor_speed[1] <= 23); 
	
	// INPUT assume 
	__CPROVER_assume(prev_input.obsDistance[0] >= 0 && prev_input.obsDistance[0] <= 25); 
	__CPROVER_assume(input.obsDistance[0] >= 0 && input.obsDistance[0] <= 25); 
	__CPROVER_assume(prev_input.obsDistance[1] >= 0 && prev_input.obsDistance[1] <= 25); 
	__CPROVER_assume(input.obsDistance[1] >= 0 && input.obsDistance[1] <= 25); 
	__CPROVER_assume(prev_input.obsDistance[2] >= 0 && prev_input.obsDistance[2] <= 25); 
	__CPROVER_assume(input.obsDistance[2] >= 0 && input.obsDistance[2] <= 25); 
	__CPROVER_assume(prev_input.obsDistance[3] >= 0 && prev_input.obsDistance[3] <= 25); 
	__CPROVER_assume(input.obsDistance[3] >= 0 && input.obsDistance[3] <= 25); 
	
	rt_state = prev_state;		
	rt_input = prev_input;		
	rt_output = prev_output;	
	model_step();				
	state = rt_state;			
	output = rt_output;			

   __CPROVER_assume((state.motor_direction_status == Turn_right));
	while(1)	
	{						
		bool found = false;	
							
		rt_state = state;	
		rt_input = input;	
		rt_output = output;	
							
		model_step();		
							
		// new input assume		
		INPUT new_input;	
		__CPROVER_assume(new_input.obsDistance[0] >= 0 && new_input.obsDistance[0] <= 25); 
		__CPROVER_assume(new_input.obsDistance[1] >= 0 && new_input.obsDistance[1] <= 25); 
		__CPROVER_assume(new_input.obsDistance[2] >= 0 && new_input.obsDistance[2] <= 25); 
		__CPROVER_assume(new_input.obsDistance[3] >= 0 && new_input.obsDistance[3] <= 25); 
			printf("OUTPUT: %d %d %d %d %d %d %d %d\n", 
		state.motor_direction_status, state.motor_speed_status, state.motor_speed[0], state.motor_speed[1], input.obsDistance[0], input.obsDistance[1], input.obsDistance[2], input.obsDistance[3]);
	printf("OUTPUT: %d %d %d %d %d %d %d %d\n", 
		rt_state.motor_direction_status, rt_state.motor_speed_status, rt_state.motor_speed[0], rt_state.motor_speed[1], new_input.obsDistance[0], new_input.obsDistance[1], new_input.obsDistance[2], new_input.obsDistance[3]);
	printf("PROP: assert(!(state.motor_direction_status == %d && rt_state.motor_direction_status == %d && state.motor_speed_status==%d && state.motor_speed[0]==%d && state.motor_speed[1]==%d && input.obsDistance[0]==%d && input.obsDistance[1]==%d && input.obsDistance[2]==%d && input.obsDistance[3]==%d));", 
prev_state.motor_direction_status, state.motor_direction_status, prev_state.motor_speed_status, prev_state.motor_speed[0], prev_state.motor_speed[1], prev_input.obsDistance[0], prev_input.obsDistance[1], prev_input.obsDistance[2], prev_input.obsDistance[3]);
	printf("ASSUME: __CPROVER_assume(!(prev_state.motor_direction_status == %d && state.motor_direction_status == %d && prev_state.motor_speed_status==%d && prev_state.motor_speed[0]==%d && prev_state.motor_speed[1]==%d && prev_input.obsDistance[0]==%d && prev_input.obsDistance[1]==%d && prev_input.obsDistance[2]==%d && prev_input.obsDistance[3]==%d));", 
prev_state.motor_direction_status, state.motor_direction_status, prev_state.motor_speed_status, prev_state.motor_speed[0], prev_state.motor_speed[1], prev_input.obsDistance[0], prev_input.obsDistance[1], prev_input.obsDistance[2], prev_input.obsDistance[3]);
		if (!(1 > input.obsDistance[1]))
		{	assert(rt_state.motor_direction_status == Turn_left);
			found = true;}
		if (!found)
			assert(false);
	}
}
