#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

#include <stddef.h>
#include <stdio.h> 
#include <stdbool.h> 
#include "model.h"
#include "model.c"
int main()
{					
	extern char* state_str[];	
	STATE state;				
	STATE prev_state;			
	INPUT input;				
	INPUT prev_input;			
	OUTPUT output;				
	OUTPUT prev_output;			

	// valid range assume 
	__CPROVER_assume(prev_state.mode >= 0 && prev_state.mode < 4); 
	__CPROVER_assume(prev_state.counter >= 0 && prev_state.counter <= 10); 
	__CPROVER_assume(prev_output.mode >= 0 && prev_output.mode < 4); 
	
	// input assume	
	__CPROVER_assume(prev_input.power_switch >= 0 && prev_input.power_switch < 2); 
	__CPROVER_assume(input.power_switch >= 0 && input.power_switch < 2); 
	
	rt_state = prev_state;		
	rt_input = prev_input;		
	rt_output = prev_output;	
	model_step();				
	state = rt_state;			
	output = rt_output;			

	// auto generated assume 
	__CPROVER_assume((state.mode == OFF)  && (!(2 >= rt_state.counter) && !prev_input.power_switch));
	__CPROVER_assume(!(prev_state.mode == 2 && state.mode == 0 && input.power_switch==1 && state.counter==10));
	__CPROVER_assume(!(prev_state.mode == 2 && state.mode == 0 && input.power_switch==1 && state.counter==11));
	__CPROVER_assume(!(prev_state.mode == 2 && state.mode == 0 && input.power_switch==1 && state.counter==4));
	__CPROVER_assume(!(prev_state.mode == 2 && state.mode == 0 && input.power_switch==1 && state.counter==5));
	__CPROVER_assume(!(prev_state.mode == 2 && state.mode == 0 && input.power_switch==1 && state.counter==6));
	__CPROVER_assume(!(prev_state.mode == 2 && state.mode == 0 && input.power_switch==1 && state.counter==7));
	__CPROVER_assume(!(prev_state.mode == 2 && state.mode == 0 && input.power_switch==1 && state.counter==8));
	__CPROVER_assume(!(prev_state.mode == 2 && state.mode == 0 && input.power_switch==1 && state.counter==9));
	__CPROVER_assume(!(prev_state.mode == 3 && state.mode == 2 && input.power_switch==0 && state.counter==0));
	__CPROVER_assume(!(prev_state.mode == 3 && state.mode == 2 && input.power_switch==0 && state.counter==1));
	__CPROVER_assume(!(prev_state.mode == 3 && state.mode == 2 && input.power_switch==0 && state.counter==2));

	while(1)	
	{						
		bool found = false;	
							
		rt_state = state;	
		rt_input = input;	
		rt_output = output;	
							
		model_step();		
							
		// input assume		
		INPUT new_input;	
		__CPROVER_assume(new_input.power_switch >= 0 && new_input.power_switch < 2); 
			printf("OUTPUT: %d %d %d\n", 
state.mode, input.power_switch, state.counter);
	printf("OUTPUT: %d %d %d\n", 
		rt_state.mode, new_input.power_switch, rt_state.counter);
	printf("PROP: assert(!(state.mode == %d && rt_state.mode == %d && rt_input.power_switch==%d && rt_state.counter==%d));", 
prev_state.mode, state.mode, input.power_switch, state.counter);
	printf("ASSUME: __CPROVER_assume(!(prev_state.mode == %d && state.mode == %d && input.power_switch==%d && state.counter==%d));", 
prev_state.mode, state.mode, input.power_switch, state.counter);
		assert((rt_state.mode == OFF));
	}
}