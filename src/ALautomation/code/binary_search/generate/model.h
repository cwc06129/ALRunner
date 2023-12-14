#pragma once
#ifndef _ENUM_DEFINE
//enum information
#define _ENUM_DEFINE
#endif


typedef struct {
	int arr[5];
	int key;
} INPUT;

typedef struct {
} STATE;

typedef struct {
	int result;
} OUTPUT;

extern INPUT rt_input;
extern STATE rt_state;
extern OUTPUT rt_output;
extern void model_initialize(void);
extern void model_step(void);
