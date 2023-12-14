/**
 * 2023-01-06 Sohee Jung
 * Vending Machine Model.c Code
*/

#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <time.h>
#include "model.h"

// state를 trace에 출력하기 위한 string 배열 정의
char* state_str[] = { "Initial", "Ready", "Inserting", "Enabled", "Emitting", "Returning", "Final" };
// event를 trace에 출력하기 위한 string 배열 정의
char* event_str[] = { "NONE", "ON", "OFF", "Insert", "Choose", "Complete" };

/**
 * Vending Machine에서 쓰일 system variable들 정의
 * 1. VENDING_STATE : vending machine의 상태를 나타낼 수 있는 enum variable
 * 2. EVENT : 외부 trigger나 특정 이벤트를 나타낼 수 있는 enum variable
 * 3. amount : vending machine에 insert된 돈 (int variable)
 * 4. vending_tvending_timeime : vending machine의 동작을 제어하기 위해 사용되는 시간 (int variable)
 *    (inserting 상태에서 10초동안 아무 동작도 이루어지지 않으면, 돈을 반환한다.)
 * 5. coin_opt : vending machine에 들어올 수 있는 동전의 종류(0일 경우 = 100, 1일 경우 = 500)
*/
#ifndef _ENUM_DEFINE
#define _ENUM_DEFINE
enum _VENDING_STATE { Initial=0, Ready=1, Inserting=2, Enabled=3, Emitting=4, Returning=5, Final=6 };
typedef enum _VENDING_STATE VENDING_STATE;
enum _EVENT { NONE=0, ON=1, OFF=2, Insert=3, Choose=4, Complete=5 };
typedef enum _EVENT EVENT;
#endif

// variable initialize
VENDING_STATE vending_state = Initial;
EVENT event = NONE;
int amount = 0;
int vending_time = 0;
int coin = 0;

/**
 * rt_state : vending_state / amount / vending_time
 * rt_input : event / coin_opt
 * rt_output : vending_state
*/
STATE rt_state;
INPUT rt_input;
OUTPUT rt_output;
// output은 그냥 yes / no 로 정해주는 것도 좋을 듯. (초콜릿이 나왔다. 안 나왔다.) -> 이걸로 변경해주기 = 외부에서 볼 수 있는 사항으로
// amount 단위 : 1~5
void model_step() {
    printf("%s %s %d %d %d ", state_str[rt_state.vending_state], event_str[rt_input.event], rt_input.coin, rt_state.amount, rt_state.vending_time);
    rt_output.emitted = 0;
    if ((rt_state.vending_state == Initial) && (rt_input.event == ON)) {
        rt_state.amount = 0;
        rt_state.vending_time = 0;
        rt_state.vending_state = Ready;
    } else if (rt_state.vending_state == Ready) {
        if (rt_input.event == Insert) {
            rt_state.amount += rt_input.coin;
            rt_state.vending_state = Inserting;
        } else if (rt_input.event == OFF) {
            rt_state.vending_state = Final;
        }       
    } else if (rt_state.vending_state == Inserting) {
        rt_state.vending_time++;
        if ((rt_input.event == Insert) && (rt_state.amount < 5) && (rt_state.vending_time < 3)) {
            rt_state.amount += rt_input.coin;
            rt_state.vending_time = 0;
        } else if (rt_state.amount >= 5) {
            rt_state.vending_time = 0;
            rt_state.vending_state = Enabled;
        } else if ((rt_state.amount < 5) && (rt_state.vending_time >= 3)) {
            rt_state.vending_state = Returning;
        }
    } else if (rt_state.vending_state == Enabled) {
        rt_state.vending_time++;
        if ((rt_input.event == Choose) && (rt_state.vending_time < 3)) {
            rt_state.amount -= 5;
            rt_state.vending_state = Emitting;
        } else if (rt_state.vending_time >= 3) {
            rt_state.vending_state = Returning;
        }
    } else if (rt_state.vending_state == Emitting) {
        if ((rt_input.event == Complete) && (rt_state.amount > 0)) {
            rt_state.vending_state = Returning;
        } else if ((rt_input.event == Complete) && (rt_state.amount == 0)) {
            rt_state.vending_state = Ready;
        }
        rt_output.emitted = 1;
    }
    printf("%d\n", rt_output.emitted);
}

/**
 * Model Initialize
 * rt_state : vending_state / amount / vending_time
 * rt_input : event / coin_opt
 * rt_output : vending_state 
*/
void model_initialize() {
    rt_input.event = NONE;
    rt_input.coin = 0;
    rt_state.vending_state = Initial;
    rt_state.amount = 0;
    rt_state.vending_time = 0;
    rt_output.emitted = 0;
}