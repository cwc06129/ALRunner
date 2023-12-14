#pragma once
#ifndef _ENUM_DEFINE
//enum information
typedef enum _RadioRequestMode {OFF, CD, FM, AM} RadioRequestMode;
#define _ENUM_DEFINE
#endif


typedef struct {
	boolean_T DiscEject;
	RadioRequestMode RadioReq;
} INPUT;

typedef struct {
	uint8_T was_ModeManager;
	uint32_T temporalCounter_i1;
	uint8_T is_ModeManager;
} STATE;

typedef struct {
} OUTPUT;

extern INPUT rt_input;
extern STATE rt_state;
extern OUTPUT rt_output;
extern void model_initialize(void);
extern void model_step(void);
