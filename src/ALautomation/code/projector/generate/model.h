#ifndef _ENUM_DEFINE
//enum information
typedef enum _MODE {OFF, READY_TO_ON, READY_TO_OFF, ON} MODE;
#define _ENUM_DEFINE
#endif

typedef struct {
    int power_switch;
} INPUT;

typedef struct {
    MODE mode;
    int counter;
} STATE;

typedef struct {
} OUTPUT;

extern INPUT rt_input;
extern STATE rt_state;
extern OUTPUT rt_output;
extern void model_initialize(void);
extern void model_step(void);
