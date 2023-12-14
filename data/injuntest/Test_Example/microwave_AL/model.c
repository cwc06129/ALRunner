#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

/**
 * 2023-01-12 Sohee Jung
 * Microwave Code
*/

#include <stdio.h>
#include <stdbool.h>
#include <time.h>
#include <stdlib.h>
#include "model.h"

/**
 * <System variable>
 * 1. state variable (Door_closed, Door_open, Cooking, Cooking_complete, Cooking_interrupt)
 * 2. event variable (Time_select, Open, Close)
 * 3. micro_time
*/

/**
 * STATE : micro_state / micro_time
 * INPUT : event / micro_time
 * OUTPUT : 
 * internal variable :  
*/
STATE rt_state;
INPUT rt_input;

/**
 * enum variable string matching을 위한 string array 선언
*/
char* micro_str[] = {"Door_closed", "Door_open", "Cooking", "Cooking_complete", "Cooking_interrupt"};
char* event_str[] = {"NONE", "Time_select", "Open", "Close"};

void model_step() {
    printf("%s %s %d\n", micro_str[rt_state.micro_state], event_str[rt_input.event], rt_state.micro_time);
    switch(rt_state.micro_state) {
        case Door_closed :
            if (rt_input.event == Open) {
                rt_state.micro_state = Door_open;
            } else if (rt_input.event == Time_select) {
                rt_state.micro_time = rt_input.micro_time;
                rt_state.micro_state = Cooking;
            }
            break;
        case Door_open :
            if (rt_input.event == Close) {
                rt_state.micro_state = Door_closed;
            }
            break;
        case Cooking : 
            rt_state.micro_time--;
            if (rt_state.micro_time == 0) {
                rt_state.micro_state = Cooking_complete;
            } else if ((rt_state.micro_time > 0) && (rt_input.event == Open)) {
                rt_state.micro_state = Cooking_interrupt;
            }
            break;
        case Cooking_interrupt :
            if (rt_input.event == Close) {
                rt_state.micro_state = Cooking;
            }
            break;
    }
}

void model_initialize() {
    rt_input.event = NONE;
    rt_input.micro_time = 1;
    rt_state.micro_state = Door_closed;
    rt_state.micro_time = 0;
}