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
		STATE state = rt_state;		
		INPUT input;				
		rt_input = input;			
		model_step();				
									
		assert(!(state.micro_state == 2 && rt_state.micro_state == 2 && rt_input.event==0 && rt_state.micro_time==1));
;
	}
}