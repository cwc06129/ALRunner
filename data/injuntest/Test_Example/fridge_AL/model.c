#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

/**
 * 2023-01-11 Sohee Jung
 * Fridge Model Code
*/

#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <time.h>
#include "model.h"

/**
 * <System variable>
 * 1. state variable (Initial, Normal Mode, Door Open Mode, Energy Safe Mode, OFF)
 * 2. Emode variable (NONE, EmodeON, EmodeOFF)
 * 3. Door variable (NONE, Open, Close)
 * 4. Button : button을 통해 냉동고를 켤 수도 있고, 끌 수도 있음.
 * 5. temp : 냉동고의 온도
 * 6. cool : 냉동고의 cool 수준을 설정할 수 있도록 함. 
*/

/**
 * STATE : fridge_state / temp
 * INPUT : emode / door / button
 * OUTPUT : 
 * internal variable : cool
*/

INPUT rt_input;
STATE rt_state;

char* state_str[] = {"Initial", "Normal_Mode", "Door_Open_Mode", "Energy_Safe_Mode", "OFF"};
char* emode_str[] = {"EmodeNONE", "EmodeON", "EmodeOFF"};
char* door_str[] = {"DoorNONE", "Open", "Close"};
char* bool_str[] = {"false", "true"};

int cool = 0;

void model_step() {
    printf("%s %s %s %s %d\n", state_str[rt_state.fridge_state], bool_str[rt_input.button], emode_str[rt_input.emode], door_str[rt_input.door], rt_state.temp);

    switch(rt_state.fridge_state) {
        case Initial : 
            if (rt_input.button) {
                cool = 4;
                rt_state.fridge_state = Normal_Mode;
            }
            break;
        case Normal_Mode :
            if (rt_state.temp > cool) rt_state.temp--;
            if (rt_input.button) {
                rt_state.fridge_state = OFF;
            } else if ((rt_input.emode == EmodeON) || (rt_state.temp > 6))  {
                cool = 6;
                rt_state.fridge_state = Energy_Safe_Mode;
            } else if (rt_input.door == Open) {
                rt_state.fridge_state = Door_Open_Mode;
            }
            break; 
        case Energy_Safe_Mode :
            if (rt_state.temp > cool) rt_state.temp--;
            if (rt_input.button) {
                rt_state.fridge_state = OFF;
            } else if ((rt_input.emode == EmodeOFF) || (rt_state.temp <= 6)) {
                cool = 4;
                rt_state.fridge_state = Normal_Mode;
            } else if (rt_input.door == Open) {
                rt_state.fridge_state = Door_Open_Mode;
            }
            break;
        case Door_Open_Mode :
            rt_state.temp++;
            if ((rt_input.door == Close) && (rt_state.temp <= 6)) {
                cool = 4;
                rt_state.fridge_state = Normal_Mode;
            } else if ((rt_input.door == Close) && (rt_state.temp > 6)) {
                cool = 6;
                rt_state.fridge_state = Energy_Safe_Mode;
            }
            break;
    }
}

void model_initialize() {
    rt_input.button = false;
    rt_input.emode = EmodeNONE;
    rt_input.door = DoorNONE;
    rt_state.fridge_state = Initial;
    rt_state.temp = 0;
}