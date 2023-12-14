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

typedef enum _MODE { OFF=0, READY_TO_ON=1, READY_TO_OFF=2, ON=3 } MODE;

MODE mode = OFF;
int power_switch = 0;
int counter = 0;

int main() {
    // get projector's power switch
    if(power_switch){
		if(mode == OFF){
			// 2023-06-23(Fri) SoheeJung
			// counter initialization
			mode = READY_TO_ON;
			counter = 0;
		}
		else if(mode == ON){
			// 2023-06-23(Fri) SoheeJung
			// counter initialization
			mode = READY_TO_OFF;
			counter = 0;
		}
		else{
			counter++;
		}
	}
	else if(mode == READY_TO_ON){
		counter++;
		if(counter > 3){
			mode = ON;
		}
	}
	else if(mode == READY_TO_OFF){
		counter++;
		if(counter > 3){
			mode = OFF;
		}
	}
	else{
		counter = 0;
	}
}