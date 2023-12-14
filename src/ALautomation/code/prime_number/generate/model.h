#ifndef _ENUM_DEFINE
//enum information
#define _ENUM_DEFINE
#endif

typedef struct {
    int n;
} INPUT;

typedef struct {
    int result;
} STATE;

typedef struct {
} OUTPUT;

extern INPUT rt_input;
extern STATE rt_state;
extern OUTPUT rt_output;
extern void model_initialize(void);
extern void model_step(void);
