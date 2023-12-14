#ifndef _ENUM_DEFINE
typedef enum _Direction_status {On_spot, Turn_left, Turn_right} Direction_status;
typedef enum _Speed_status {Stop, Slow_speed, Midium_speed, Fast_speed} Speed_status;
#define _ENUM_DEFINE
#endif
typedef struct {
    Direction_status direction_status;
    Speed_status speed_status;
    int zone[4];
} STATE;

typedef struct {
    int obsDistance[4];
    int turnSensor_degree;
} INPUT;

typedef struct {
} OUTPUT;

extern STATE rt_state;
extern INPUT rt_input;
extern OUTPUT rt_output;
extern void model_initialize(void);
extern void model_step(void);
