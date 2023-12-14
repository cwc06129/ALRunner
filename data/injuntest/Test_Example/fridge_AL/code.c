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
									
		assert(!(state.fridge_state == 2 && rt_state.fridge_state == 3 && rt_input.button==0 && rt_input.emode==1 && rt_input.door==2 && rt_state.temp==8));
;
	}
}