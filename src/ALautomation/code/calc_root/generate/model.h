#ifndef _ENUM_DEFINE
//enum information
#define _ENUM_DEFINE
#endif

typedef struct {
    int calca;
    int calcb;
    int calcc;
} INPUT;

typedef struct {
    int determinant;
    int result;
} STATE;

typedef struct {
} OUTPUT;

extern INPUT rt_input;
extern STATE rt_state;
extern OUTPUT rt_output;
extern void model_initialize(void);
extern void model_step(void);
