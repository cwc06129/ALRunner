#ifndef _ENUM_DEFINE
#define _ENUM_DEFINE
enum _MODE { OFF=0, READY_TO_ON=1, READY_TO_OFF=2, ON=3, START=4 };
typedef enum _MODE MODE;
#endif
typedef struct {
	MODE mode;
	unsigned char power_switch;
	int counter;
} STATE;

typedef struct {
	unsigned char power_switch;
} INPUT;

typedef struct {
	MODE mode;
} OUTPUT;

extern STATE rt_state;
extern INPUT rt_input;
extern OUTPUT rt_output;
extern void model_initialize(void);
extern void model_step(void);
