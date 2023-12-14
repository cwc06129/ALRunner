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

	// valid range assume 
	__CPROVER_assume(prev_state.micro_state >= 0 && prev_state.micro_state < 5); 
	__CPROVER_assume(prev_state.micro_time >= 0 && prev_state.micro_time < 10); 
	
	// input assume	
	__CPROVER_assume(prev_input.event >= 0 && prev_input.event < 4); 
	__CPROVER_assume(prev_input.micro_time >= 1 && prev_input.micro_time < 10); 
	__CPROVER_assume(input.event >= 0 && input.event < 4); 
	__CPROVER_assume(input.micro_time >= 1 && input.micro_time < 10); 
	
	rt_state = prev_state;		
	rt_input = prev_input;		
	model_step();				
	state = rt_state;			

	// auto generated assume 
	__CPROVER_assume((state.micro_state == Cooking_interrupt));
	__CPROVER_assume(!(prev_state.micro_state == 0 && state.micro_state == 2 && input.event==0 && state.micro_time==1));
	__CPROVER_assume(!(prev_state.micro_state == 0 && state.micro_state == 2 && input.event==0 && state.micro_time==2));
	__CPROVER_assume(!(prev_state.micro_state == 0 && state.micro_state == 2 && input.event==2 && state.micro_time==2));
	__CPROVER_assume(!(prev_state.micro_state == 0 && state.micro_state == 2 && input.event==2 && state.micro_time==8));
	__CPROVER_assume(!(prev_state.micro_state == 0 && state.micro_state == 2 && input.event==2 && state.micro_time==9));
	__CPROVER_assume(!(prev_state.micro_state == 1 && state.micro_state == 0 && input.event==0 && state.micro_time==2));
	__CPROVER_assume(!(prev_state.micro_state == 1 && state.micro_state == 0 && input.event==0 && state.micro_time==3));
	__CPROVER_assume(!(prev_state.micro_state == 1 && state.micro_state == 0 && input.event==0 && state.micro_time==7));
	__CPROVER_assume(!(prev_state.micro_state == 1 && state.micro_state == 0 && input.event==0 && state.micro_time==8));
	__CPROVER_assume(!(prev_state.micro_state == 1 && state.micro_state == 0 && input.event==0 && state.micro_time==9));
	__CPROVER_assume(!(prev_state.micro_state == 1 && state.micro_state == 0 && input.event==3 && state.micro_time==3));
	__CPROVER_assume(!(prev_state.micro_state == 1 && state.micro_state == 0 && input.event==3 && state.micro_time==8));
	__CPROVER_assume(!(prev_state.micro_state == 1 && state.micro_state == 0 && input.event==3 && state.micro_time==9));
	__CPROVER_assume(!(prev_state.micro_state == 2 && state.micro_state == 2 && input.event==2 && state.micro_time==2));
	__CPROVER_assume(!(prev_state.micro_state == 2 && state.micro_state == 2 && input.event==2 && state.micro_time==3));
	__CPROVER_assume(!(prev_state.micro_state == 2 && state.micro_state == 2 && input.event==2 && state.micro_time==4));
	__CPROVER_assume(!(prev_state.micro_state == 2 && state.micro_state == 2 && input.event==2 && state.micro_time==8));
	__CPROVER_assume(!(prev_state.micro_state == 2 && state.micro_state == 4 && input.event==3 && state.micro_time==1));
	__CPROVER_assume(!(prev_state.micro_state == 2 && state.micro_state == 4 && input.event==3 && state.micro_time==2));
	__CPROVER_assume(!(prev_state.micro_state == 2 && state.micro_state == 4 && input.event==3 && state.micro_time==3));
	__CPROVER_assume(!(prev_state.micro_state == 2 && state.micro_state == 4 && input.event==3 && state.micro_time==4));
	__CPROVER_assume(!(prev_state.micro_state == 2 && state.micro_state == 4 && input.event==3 && state.micro_time==5));
	__CPROVER_assume(!(prev_state.micro_state == 2 && state.micro_state == 4 && input.event==3 && state.micro_time==6));
	__CPROVER_assume(!(prev_state.micro_state == 2 && state.micro_state == 4 && input.event==3 && state.micro_time==7));
	__CPROVER_assume(!(prev_state.micro_state == 2 && state.micro_state == 4 && input.event==3 && state.micro_time==8));
	__CPROVER_assume(!(prev_state.micro_state == 4 && state.micro_state == 2 && input.event==0 && state.micro_time==0));
	__CPROVER_assume(!(prev_state.micro_state == 4 && state.micro_state == 2 && input.event==0 && state.micro_time==1));
	__CPROVER_assume(!(prev_state.micro_state == 4 && state.micro_state == 2 && input.event==0 && state.micro_time==2));
	__CPROVER_assume(!(prev_state.micro_state == 4 && state.micro_state == 2 && input.event==1 && state.micro_time==0));
	__CPROVER_assume(!(prev_state.micro_state == 4 && state.micro_state == 2 && input.event==2 && state.micro_time==0));
	__CPROVER_assume(!(prev_state.micro_state == 4 && state.micro_state == 2 && input.event==3 && state.micro_time==0));
	__CPROVER_assume(!(prev_state.micro_state == 4 && state.micro_state == 4 && input.event==3 && state.micro_time==0));
	__CPROVER_assume(!(prev_state.micro_state == 4 && state.micro_state == 4 && input.event==3 && state.micro_time==1));
	__CPROVER_assume(!(prev_state.micro_state == 4 && state.micro_state == 4 && input.event==3 && state.micro_time==2));
	__CPROVER_assume(!(prev_state.micro_state == 4 && state.micro_state == 4 && input.event==3 && state.micro_time==3));
	__CPROVER_assume(!(prev_state.micro_state == 4 && state.micro_state == 4 && input.event==3 && state.micro_time==4));
	__CPROVER_assume(!(prev_state.micro_state == 4 && state.micro_state == 4 && input.event==3 && state.micro_time==5));
	__CPROVER_assume(!(prev_state.micro_state == 4 && state.micro_state == 4 && input.event==3 && state.micro_time==6));
	__CPROVER_assume(!(prev_state.micro_state == 4 && state.micro_state == 4 && input.event==3 && state.micro_time==7));
	__CPROVER_assume(!(prev_state.micro_state == 4 && state.micro_state == 4 && input.event==3 && state.micro_time==8));
	__CPROVER_assume(!(prev_state.micro_state == 4 && state.micro_state == 4 && input.event==3 && state.micro_time==9));

	while(1)	
	{						
		bool found = false;	
							
		rt_state = state;	
		rt_input = input;	
							
		model_step();		
							
		// input assume		
		INPUT new_input;	
		__CPROVER_assume(new_input.event >= 0 && new_input.event < 4); 
		__CPROVER_assume(new_input.micro_time >= 1 && new_input.micro_time < 10); 
			printf("OUTPUT: %d %d %d\n", 
state.micro_state, input.event, state.micro_time);
	printf("OUTPUT: %d %d %d\n", 
		rt_state.micro_state, new_input.event, rt_state.micro_time);
	printf("PROP: assert(!(state.micro_state == %d && rt_state.micro_state == %d && rt_input.event==%d && rt_state.micro_time==%d));", 
prev_state.micro_state, state.micro_state, input.event, state.micro_time);
	printf("ASSUME: __CPROVER_assume(!(prev_state.micro_state == %d && state.micro_state == %d && input.event==%d && state.micro_time==%d));", 
prev_state.micro_state, state.micro_state, input.event, state.micro_time);
		if ((input.event == Close))
		{	assert(rt_state.micro_state == Cooking);
			found = true;}
		if (!found)
			assert((rt_state.micro_state == Cooking_interrupt));
	}
}