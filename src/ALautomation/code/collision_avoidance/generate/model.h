#ifndef _ENUM_DEFINE
//enum information
typedef enum _Direction_status {Straight, Turn_left, Turn_right} Direction_status;
typedef enum _Speed_status {Stop, Slow_speed, Midium_speed, Fast_speed} Speed_status;
#define _ENUM_DEFINE
#endif


typedef struct {
	int obsDistance[4];
} INPUT;

typedef struct {
	Direction_status motor_direction_status;
	Speed_status motor_speed_status;
	int motor_speed[2];
} STATE;

typedef struct {
} OUTPUT;

extern INPUT rt_input;
extern STATE rt_state;
extern OUTPUT rt_output;
extern void model_initialize(void);
extern void model_step(void);
