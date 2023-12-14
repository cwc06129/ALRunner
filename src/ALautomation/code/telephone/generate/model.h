#ifndef _ENUM_DEFINE
//enum information
typedef enum _teleState {Idle, DialTone, TimeOut, Dialing, Connecting, Invalid, Busy, Ringing, Talking} teleState;
typedef enum _teleEvent {None, CallerLift, busy, Connected, CalleeAnswer, CallerHangUp, DialButton} teleEvent;
#define _ENUM_DEFINE
#endif

typedef struct {
    teleEvent teleevent;
    int dialDigit;
} INPUT;

typedef struct {
    teleState telestate;
    int timer;
} STATE;

typedef struct {
} OUTPUT;

extern INPUT rt_input;
extern STATE rt_state;
extern OUTPUT rt_output;
extern void model_initialize(void);
extern void model_step(void);
