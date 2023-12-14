#ifndef _ENUM_DEFINE
#define _ENUM_DEFINE
enum _VENDING_STATE { Initial=0, Ready=1, Inserting=2, Enabled=3, Emitting=4, Returning=5, Final=6 };
typedef enum _VENDING_STATE VENDING_STATE;
enum _EVENT { NONE=0, ON=1, OFF=2, Insert=3, Choose=4, Complete=4 };
typedef enum _EVENT EVENT;
#endif
typedef struct {
	VENDING_STATE vending_state;
	int amount;
	int vending_time;
} STATE;

typedef struct {
	signed int coin;
	EVENT event;
} INPUT;

typedef struct {
	int emitted;
} OUTPUT;

extern STATE rt_state;
extern INPUT rt_input;
extern OUTPUT rt_output;
extern void model_initialize(void);
extern void model_step(void);
