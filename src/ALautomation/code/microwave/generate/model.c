#define TASK(n) void TASK_##n
#define FUNC(n,m) k n FUNC_##k

#include <stdio.h>
#include <stdbool.h>
#include <time.h>
#include <stdlib.h>
#include "model.h"

INPUT rt_input;
STATE rt_state;
OUTPUT rt_output;

int power = 0;

char * micro_state_str[] = {"Waiting","FullPower","HalfPower","SetTime","Enabled","Disabled","Operation"};
char * event_str[] = {"None","full","half","openDoor","cancel","timer","closeDoor","StartPress"};

void model_step() {
	rt_state.micro_time = rt_input.micro_time;
	printf("%s %d %d %s\n", micro_state_str[rt_state.micro_state], rt_state.micro_time, rt_input.micro_time, event_str[rt_input.event]);
	srand(time(NULL));
	while(true){
		;
		if(rt_input.event == full){
			if(rt_state.micro_state == Waiting || rt_state.micro_state == HalfPower){
				rt_state.micro_state = FullPower;
			}
		}
		else if(rt_input.event == half){
			if(rt_state.micro_state == Waiting || rt_state.micro_state == FullPower){
				rt_state.micro_state = HalfPower;
			}
		}
		else if(rt_input.event == timer){
			if(rt_state.micro_state == FullPower || rt_state.micro_state == HalfPower){
				rt_state.micro_state = SetTime;
			}
		}
		else if(rt_input.event == openDoor){
			if(rt_state.micro_state == SetTime || rt_state.micro_state == Operation){
				rt_state.micro_state = Disabled;
			}
		}
		else if(rt_input.event == closeDoor){
			if(rt_state.micro_state == SetTime || rt_state.micro_state == Disabled){
				rt_state.micro_state = Enabled;
			}
		}
		else if(rt_input.event == StartPress){
			if(rt_state.micro_state == Enabled){
				rt_state.micro_state = Operation;
			}
		}
		else if(rt_input.event == cancel){
			if(rt_state.micro_state == Operation){
				rt_state.micro_state = Waiting;
			}
		}
		else if(rt_state.micro_state == SetTime){
			;
		}
		else if(rt_state.micro_state == HalfPower){
			power = 300;
		}
		else if(rt_state.micro_state == FullPower){
			power = 600;
		}
		else if(rt_state.micro_state == Operation){
			rt_state.micro_time--;
			if(rt_state.micro_time <= 0){
				rt_state.micro_time = 0;
				rt_state.micro_state = Waiting;
			}
		}
	}
}




void model_initialize() {
	rt_state.micro_state = Waiting;
	rt_input.event = None;
	rt_state.micro_time = 0;
	power = 0;
}


