#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

/**
 * 2023-06-26 Sohee Jung
 * Microwave Code (Modification)
*/

#include <stdio.h>
#include <stdbool.h>
#include <time.h>
#include <stdlib.h>

/**
 * <System variable>
 * 1. state variable (Door_closed, Door_open, Cooking, Cooking_complete, Cooking_interrupt)
 * 2. event variable (Time_select, Open, Close)
 * 3. micro_time
*/

// enum variable definition
enum _MICRO_STATE { Waiting, FullPower, HalfPower, SetTime, Enabled, Disabled, Operation };
typedef enum _MICRO_STATE MICRO_STATE;
enum _EVENT { None, full, half, openDoor, cancel, timer, closeDoor, StartPress };
typedef enum _EVENT EVENT;

// variable initialization
MICRO_STATE micro_state = Waiting;
EVENT event = None;
int micro_time = 0;
int power = 0;

int main() {
    // srand(time(NULL));

    // while(true) {
        event = rand() % 4;

        if(event == full) {
            if(micro_state == Waiting || micro_state == HalfPower) micro_state = FullPower;
        }
        else if(event == half) {
            if(micro_state == Waiting || micro_state == FullPower) micro_state = HalfPower;
        }
        else if(event == timer) {
            if(micro_state == FullPower || micro_state == HalfPower) micro_state = SetTime;
        }
        else if(event == openDoor) {
            if(micro_state == SetTime || micro_state == Operation) micro_state = Disabled;
        }
        else if(event == closeDoor) {
            if(micro_state == SetTime || micro_state == Disabled) micro_state = Enabled;
        }
        else if(event == StartPress) {
            if(micro_state == Enabled) micro_state = Operation;
        }
        else if(event == cancel) {
            if(micro_state == Operation) micro_state = Waiting;
        }
        else {
            if(micro_state == SetTime) micro_time = rand() % 10;
            else if(micro_state == HalfPower) power = 300;
            else if(micro_state == FullPower) power = 600;
            else if(micro_state == Operation) {
                micro_time--;
                if(micro_time <= 0) {
                    micro_time = 0;
                    micro_state = Waiting;
                }
            }
        }        
    // }
}