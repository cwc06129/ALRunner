#ifndef _ENUM_DEFINE
//enum information
typedef enum _Item {COKE, PEPSI, WATER, JUICE} Item;
typedef enum _Machine_state {Initial, Insert, ButtonEnabled, Emit, Return} Machine_state;
#define _ENUM_DEFINE
#endif

typedef struct {
    int item_num;
    int amount;
    int buttonPress;
} INPUT;

typedef struct {
    int timer;
    Machine_state machine_state;
} STATE;

typedef struct {
} OUTPUT;

extern INPUT rt_input;
extern STATE rt_state;
extern OUTPUT rt_output;
extern void model_initialize(void);
extern void model_step(void);
