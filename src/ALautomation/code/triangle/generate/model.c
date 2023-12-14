#define TASK(n) void TASK_##n
#define FUNC(n,m) k n FUNC_##k
#define triangleaSK(n) void triangleaSK_##n

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "model.h"

INPUT rt_input;
STATE rt_state;
OUTPUT rt_output;



void model_step() {
	printf("%d %d %d %d %d\n", rt_state.match, rt_input.trianglea, rt_input.triangleb, rt_input.trianglec, rt_output.result);
	if(rt_input.trianglea == rt_input.triangleb){
		rt_state.match = rt_state.match + 1;
	}
	if(rt_input.trianglea == rt_input.trianglec){
		rt_state.match = rt_state.match + 2;
	}
	if(rt_input.triangleb == rt_input.trianglec){
		rt_state.match = rt_state.match + 3;
	}
	if(rt_state.match == 0){
		if(rt_input.trianglea + rt_input.triangleb <= rt_input.trianglec){
			rt_output.result = 2;
		}
		else if(rt_input.triangleb + rt_input.trianglec <= rt_input.trianglea){
			rt_output.result = 2;
		}
		else if(rt_input.trianglea + rt_input.trianglec <= rt_input.triangleb){
			rt_output.result = 2;
		}
		else{
			rt_output.result = 3;
		}
	}
	else if(rt_state.match == 1){
		if(rt_input.trianglea + rt_input.triangleb <= rt_input.trianglec){
			rt_output.result = 2;
		}
		else{
			rt_output.result = 1;
		}
	}
	else if(rt_state.match == 2){
		if(rt_input.trianglea + rt_input.trianglec <= rt_input.triangleb){
			rt_output.result = 2;
		}
		else{
			rt_output.result = 1;
		}
	}
	else if(rt_state.match == 3){
		if(rt_input.triangleb + rt_input.trianglec <= rt_input.trianglea){
			rt_output.result = 2;
		}
		else{
			rt_output.result = 1;
		}
	}
	else{
		rt_output.result = 0;
	}
}

void model_initialize() {
	rt_input.trianglea = 0;
	rt_input.triangleb = 0;
	rt_input.trianglec = 0;
	rt_state.match = 0;
	rt_output.result = -1;
}


