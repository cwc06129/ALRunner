#define TASK (n) void TASK_##n
#define FUNC (n,m) k n FUNC_##k

#include <stdio.h>
#include <stdlib.h>
#include "model.h"

INPUT rt_input;
STATE rt_state;
OUTPUT rt_output;



void model_step() {
	printf("%d %d %d\n", rt_output.result, rt_input.n1, rt_input.n2);
	;
	;
	while(rt_input.n1 != rt_input.n2){
		if(rt_input.n1 > rt_input.n2){
			rt_input.n1 -= rt_input.n2;
		}
		else{
			rt_input.n2 -= rt_input.n1;
		}
	}
	rt_output.result = rt_input.n1;
}


void model_initialize() {
	rt_input.n1 = 2;
	rt_input.n2 = 2;
	rt_output.result = 1;
}


