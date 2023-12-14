#define TASK(n) void TASK_##n
#define FUNC(n,m) k n FUNC_##k

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <stdbool.h>
#include "model.h"

INPUT rt_input;
STATE rt_state;
OUTPUT rt_output;


char * telestate_str[] = {"Idle","DialTone","TimeOut","Dialing","Connecting","Invalid","Busy","Ringing","Talking"};
char * teleevent_str[] = {"None","CallerLift","busy","Connected","CalleeAnswer","CallerHangUp","DialButton"};

void model_step() {
	printf("%s %d %s %d\n", telestate_str[rt_state.telestate], rt_state.timer, teleevent_str[rt_input.teleevent], rt_input.dialDigit);
	srand(time(NULL));
	while(true){
		;
		if(rt_input.teleevent == CallerLift){
			if(rt_state.telestate == Idle){
				rt_state.telestate = DialTone;
			}
		}
		else if(rt_input.teleevent == busy){
			if(rt_state.telestate == Connecting){
				rt_state.telestate = Busy;
			}
		}
		else if(rt_input.teleevent == Connected){
			if(rt_state.telestate == Connecting){
				rt_state.telestate = Ringing;
			}
		}
		else if(rt_input.teleevent == CalleeAnswer){
			if(rt_state.telestate == Ringing){
				rt_state.telestate = Talking;
			}
		}
		else if(rt_input.teleevent == CallerHangUp){
			rt_state.timer = 0;
			rt_state.telestate = Idle;
		}
		else if(rt_input.teleevent == DialButton){
			if(rt_state.telestate == DialTone){
				rt_state.telestate = Dialing;
			}
			else if(rt_state.telestate == Dialing){
				if(rt_input.dialDigit < 2){
					rt_state.telestate = Dialing;
				}
				else if(rt_input.dialDigit % 2){
					rt_state.telestate = Connecting;
				}
				else{
					rt_state.telestate = Invalid;
				}
			}
		}
		else if(rt_state.telestate == DialTone){
			if(rt_state.timer >= 5){
				rt_state.telestate = TimeOut;
			}
			rt_state.timer++;
		}
		else if(rt_state.telestate == Dialing){
			if(rt_state.timer >= 5){
				rt_state.telestate = TimeOut;
			}
			rt_state.timer++;
		}
	}
}




void model_initialize() {
	rt_state.telestate = Idle;
	rt_input.teleevent = None;
	rt_state.timer = 0;
	rt_input.dialDigit = 0;
}


