#define TASK(n) void TASK_##n
#define FUNC(n,m) k n FUNC_##k

#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <time.h>
#include "model.h"

INPUT rt_input;
STATE rt_state;
OUTPUT rt_output;


char * mode_str[] = {"OFF","READY_TO_ON","READY_TO_OFF","ON"};

void model_step() {
	printf("%s %d %d\n", mode_str[rt_state.mode], rt_state.counter, rt_input.power_switch);
	if(rt_input.power_switch){
		if(rt_state.mode == OFF){
			rt_state.mode = READY_TO_ON;
			rt_state.counter = 0;
		}
		else if(rt_state.mode == ON){
			rt_state.mode = READY_TO_OFF;
			rt_state.counter = 0;
		}
		else{
			rt_state.counter++;
		}
	}
	else if(rt_state.mode == READY_TO_ON){
		rt_state.counter++;
		if(rt_state.counter > 3){
			rt_state.mode = ON;
		}
	}
	else if(rt_state.mode == READY_TO_OFF){
		rt_state.counter++;
		if(rt_state.counter > 3){
			rt_state.mode = OFF;
		}
	}
	else{
		rt_state.counter = 0;
	}
}

void model_initialize() {
	rt_state.mode = OFF;
	rt_input.power_switch = 0;
	rt_state.counter = 0;
}


