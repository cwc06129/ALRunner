#ifndef _ENUM_DEFINE
//enum information
#define _ENUM_DEFINE
#endif

typedef struct {
    int trianglea;
    int triangleb;
    int trianglec;
} INPUT;

typedef struct {
    int match;
} STATE;

typedef struct {
    int result;
} OUTPUT;

extern INPUT rt_input;
extern STATE rt_state;
extern OUTPUT rt_output;
extern void model_initialize(void);
extern void model_step(void);
