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
									
		assert(!(state.direction_status == 1 && rt_state.direction_status == 1 && rt_state.speed_status==1 && rt_input.obsDistance[0]==50 && rt_input.obsDistance[1]==50 && rt_input.obsDistance[2]==27 && rt_input.obsDistance[3]==30 && rt_state.zone[0]==2 && rt_state.zone[1]==2 && rt_state.zone[2]==1 && rt_state.zone[3]==0));
;
	}
}