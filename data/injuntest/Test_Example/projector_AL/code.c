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
									
		assert(!(state.mode == 2 && rt_state.mode == 2 && rt_input.power_switch==0 && rt_state.counter==2));
;
	}
}