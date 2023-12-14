#ifndef _ENUM_DEFINE
typedef enum {
        INIT = 0,
        IDLE = 1,
        RUN = 2,
        STOP = 3,
        EMERGENCY = -1,
} elevState;
#define _ENUM_DEFINE
#endif

typedef struct {
        elevState previousState;
        elevState currentState;
        int motorDirection;
        int lastFloorAfterEmergency;
        int previousMainFloor;
        // int floorBeyond;
} STATE;

typedef struct {
	int orderMatrix[3][4];
	int currentFloorLocation;
	int stopSignal;
} INPUT;

typedef struct {
        int door_lamp;
        int stop_lamp;
} OUTPUT;

extern STATE rt_state;
extern INPUT rt_input;
extern OUTPUT rt_output;
extern void model_initialize(void);
extern void model_step(void);
extern void initStates(void);
int emergencyStopHandler();
