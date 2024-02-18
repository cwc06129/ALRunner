#pragma once
#ifndef _ENUM_DEFINE
//enum information
typedef enum _MICRO_STATE {Waiting, FullPower, HalfPower, SetTime, Enabled, Disabled, Operation} MICRO_STATE;
typedef enum _EVENT {None, full, half, openDoor, cancel, timer, closeDoor, StartPress} EVENT;
#define _ENUM_DEFINE
#endif


typedef struct {
	EVENT event;
	int micro_time;
} INPUT;

typedef struct {
	int micro_time;
	MICRO_STATE micro_state;
} STATE;

typedef struct {
} OUTPUT;

extern INPUT rt_input;
extern STATE rt_state;
extern OUTPUT rt_output;
extern void model_initialize(void);
extern void model_step(void);
