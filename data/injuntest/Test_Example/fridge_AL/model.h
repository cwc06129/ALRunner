#ifndef _ENUM_DEFINE
#define _ENUM_DEFINE
enum _FRIDGE_STATE { Initial=0, Normal_Mode=1, Door_Open_Mode=2, Energy_Safe_Mode=3, OFF=4 };
typedef enum _FRIDGE_STATE FRIDGE_STATE;
enum _EMODE { EmodeNONE=0, EmodeON=1, EmodeOFF=2 };
typedef enum _EMODE EMODE;
enum _DOOR { DoorNONE=0, Open=1, Close=2 };
typedef enum _DOOR DOOR;
#endif
typedef struct {
	FRIDGE_STATE fridge_state;
	int temp;
} STATE;

typedef struct {
	EMODE emode;
	DOOR door;
	int button;
} INPUT;

extern STATE rt_state;
extern INPUT rt_input;
extern void model_initialize(void);
extern void model_step(void);
