#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

/**
 * 2023-06-26(Mon) SoheeJung
 * telephone code
 * referece : https://www.site.uottawa.ca/~tcl/gradtheses/mnojoumian/ThesisFiles/FinalSpec/UML/15.3.12.html (except Pinned state)
*/

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <stdbool.h>

typedef enum teleState { Idle, DialTone, TimeOut, Dialing, Connecting, Invalid, Busy, Ringing, Talking } teleState;
typedef enum teleEvent { None, CallerLift, busy, Connected, CalleeAnswer, CallerHangUp, DialButton } teleEvent;

teleState telestate = Idle;
teleEvent teleevent = None;
int timer = 0;
int dialDigit = 0;

int main() {
    srand(time(NULL));

    while(true) {
        teleevent = rand() % 6;

        if(teleevent == CallerLift) {
            if(telestate == Idle) telestate = DialTone;
        }
        else if(teleevent == busy) {
            if(telestate == Connecting) telestate = Busy;
        }
        else if(teleevent == Connected) {
            if(telestate == Connecting) telestate = Ringing;
        }
        else if(teleevent == CalleeAnswer) {
            if(telestate == Ringing) telestate = Talking;
        }
        else if(teleevent == CallerHangUp) {
            timer = 0;
            telestate = Idle;
        }
        else if(teleevent == DialButton) {
            if(telestate == DialTone) {
                telestate = Dialing;
            }
            else if(telestate == Dialing) {
                if(dialDigit < 2){
                telestate = Dialing;
                }
                else if(dialDigit % 2){
                    telestate = Connecting;
                }
                else{
                    telestate = Invalid;
                }
            }
        }
        else if(telestate == DialTone){
            if(timer >= 5){
                telestate = TimeOut;
            }
            timer++;
        }
        else if(telestate == Dialing){
            if(timer >= 5){
                telestate = TimeOut;
            }
            timer++;
        }
    }
}