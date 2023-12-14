#define TASK(n) void TASK_##n
#define FUNC(n,m) k n FUNC_##k

#include <stdio.h>
#include "model.h"

INPUT rt_input;
STATE rt_state;
OUTPUT rt_output;



void model_step() {
	printf("%d %d\n", rt_state.result, rt_input.n);
	int i;
	int flag = 0;
	;
	if(rt_input.n == 0 || rt_input.n == 1){
		flag = 1;
	}
	for(i = 2 ; i <= rt_input.n / 2 ; ++i){
		if(rt_input.n % i == 0){
			flag = 1;
			break;
		}
	}
	if(flag == 0){
		rt_state.result = 1;
	}
	else{
		rt_state.result = 0;
	}
}


void model_initialize() {
	rt_input.n = 0;
	rt_state.result = 0;
}


