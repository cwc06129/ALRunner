#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

#include<stdio.h>
#include <stddef.h>
#include <stdio.h> 
#include <stdbool.h> 
#include"model.c"
int main()
{						
	model_initialize();
	while(1)			
	{								
		OUTPUT output = rt_output;	
		STATE state = rt_state;		
		INPUT input;				
		rt_input = input;			
		model_step();				
									
		assert(!(state.vending_state == 3 && rt_state.vending_state == 3 && rt_input.event==2 && rt_input.coin==2 && rt_state.amount==5 && rt_state.vending_time==1 && rt_output.emitted==0));
;
	}
}