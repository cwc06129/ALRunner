#ifndef _ENUM_DEFINE
#define _ENUM_DEFINE
enum _MICRO_STATE { Door_closed, Door_open, Cooking, Cooking_complete, Cooking_interrupt };
typedef enum _MICRO_STATE MICRO_STATE;
enum _EVENT { NONE, Time_select, Open, Close };
typedef enum _EVENT EVENT;
#endif
typedef struct {
	MICRO_STATE micro_state;
	int micro_time;
} STATE;

typedef struct {
	EVENT event;
	int micro_time;
} INPUT;

extern STATE rt_state;
extern INPUT rt_input;
extern void model_initialize(void);
extern void model_step(void);
