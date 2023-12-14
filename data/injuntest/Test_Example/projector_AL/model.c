#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

/*
It is student's made sample for program behavior check.
when power is off and switch turns on, power is on after 3 sec.
when power is on and switch turn off twice in 3 sec, power is off.
*/

#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <time.h>
#include "model.h"

char* state_str[] = {"OFF", "READY_TO_ON", "READY_TO_OFF", "ON"};
#ifndef _ENUM_DEFINE
#define _ENUM_DEFINE
enum _MODE { OFF=0, READY_TO_ON=1, READY_TO_OFF=2, ON=3 };
typedef enum _MODE MODE;
#endif

MODE mode = OFF;
bool power_switch = false;
int counter = 0;

STATE rt_state;
INPUT rt_input;
OUTPUT rt_output;

void model_step() {
    printf("%s %s %d\n", state_str[rt_state.mode], rt_input.power_switch ? "true":"false", rt_state.counter);
    // get projector's power switch
    rt_state.power_switch = rt_input.power_switch;
    
    if(rt_state.power_switch) {
        if(rt_state.mode == OFF) {
            rt_state.mode = READY_TO_ON;
            rt_output.mode = READY_TO_ON;
        } else if(rt_state.mode == ON) {
            rt_state.mode = READY_TO_OFF;
            rt_output.mode = READY_TO_OFF;
        } else {
            rt_state.counter++;
        }
    }
    else {
        if(rt_state.mode == READY_TO_ON) {
            rt_state.counter++;
            if(rt_state.counter > 3) {
                rt_state.mode = ON;
                rt_output.mode = ON;
            }
        } 
        else if(rt_state.mode == READY_TO_OFF) {
            rt_state.counter++;
            if(rt_state.counter > 3) {
                rt_state.mode = OFF;
                rt_output.mode = OFF;
            }
        } else {
            rt_state.counter = 0;
        }
    }
}

void model_initialize() {
	rt_state.mode = OFF;
	rt_input.power_switch = 0;
    rt_state.counter = 0;
	rt_output.mode = OFF;
}