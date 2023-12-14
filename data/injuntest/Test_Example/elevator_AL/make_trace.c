#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

/**
 * 2022-12-29(Thu) Sohee Jung
 * ActiveLearning Example에서 code_run_random_sample.c 코드를 이용해서 trace을 random으로 generation하는 코드
*/
#include <stddef.h>
#include <stdio.h> 
#include <stdlib.h>                     /* This ert_main.c example uses printf/fflush */
#include "model.h"                     /* Model's header file */
#include <time.h>

INPUT get_rand_values()
{
	INPUT in;
  for(int i=0; i<3; i++) {
    for(int j=0; j<4; j++) {
      in.orderMatrix[i][j] = rand() % 2;
    }
  }
  in.currentFloorLocation = rand() % 5 - 1;
  in.stopSignal = rand() % 2;
	return in;
}

int main()
{
  srand(time(NULL));
  int inp[10][10];

  int header = 1;
  if(header)
  {
    printf("types\n");
    // printf("INIT rt_state.motorDirection:N rt_state.lastFloorAfterEmergency:N rt_state.previousMainFloor:N rt_state.floorBeyond:N rt_output.door_lamp:N rt_output.stop_lamp:N rt_input.currentFloorLocation:N rt_input.stopSignal:N rt_input.orderMatrix_0_0:N rt_input.orderMatrix_0_1:N rt_input.orderMatrix_0_2:N rt_input.orderMatrix_0_3:N rt_input.orderMatrix_1_0:N rt_input.orderMatrix_1_1:N rt_input.orderMatrix_1_2:N rt_input.orderMatrix_1_3:N rt_input.orderMatrix_2_0:N rt_input.orderMatrix_2_1:N rt_input.orderMatrix_2_2:N rt_input.orderMatrix_2_3:N\n");
    // printf("IDLE rt_state.motorDirection:N rt_state.lastFloorAfterEmergency:N rt_state.previousMainFloor:N rt_state.floorBeyond:N rt_output.door_lamp:N rt_output.stop_lamp:N rt_input.currentFloorLocation:N rt_input.stopSignal:N rt_input.orderMatrix_0_0:N rt_input.orderMatrix_0_1:N rt_input.orderMatrix_0_2:N rt_input.orderMatrix_0_3:N rt_input.orderMatrix_1_0:N rt_input.orderMatrix_1_1:N rt_input.orderMatrix_1_2:N rt_input.orderMatrix_1_3:N rt_input.orderMatrix_2_0:N rt_input.orderMatrix_2_1:N rt_input.orderMatrix_2_2:N rt_input.orderMatrix_2_3:N\n");
    // printf("RUN rt_state.motorDirection:N rt_state.lastFloorAfterEmergency:N rt_state.previousMainFloor:N rt_state.floorBeyond:N rt_output.door_lamp:N rt_output.stop_lamp:N rt_input.currentFloorLocation:N rt_input.stopSignal:N rt_input.orderMatrix_0_0:N rt_input.orderMatrix_0_1:N rt_input.orderMatrix_0_2:N rt_input.orderMatrix_0_3:N rt_input.orderMatrix_1_0:N rt_input.orderMatrix_1_1:N rt_input.orderMatrix_1_2:N rt_input.orderMatrix_1_3:N rt_input.orderMatrix_2_0:N rt_input.orderMatrix_2_1:N rt_input.orderMatrix_2_2:N rt_input.orderMatrix_2_3:N\n");
    // printf("STOP rt_state.motorDirection:N rt_state.lastFloorAfterEmergency:N rt_state.previousMainFloor:N rt_state.floorBeyond:N rt_output.door_lamp:N rt_output.stop_lamp:N rt_input.currentFloorLocation:N rt_input.stopSignal:N rt_input.orderMatrix_0_0:N rt_input.orderMatrix_0_1:N rt_input.orderMatrix_0_2:N rt_input.orderMatrix_0_3:N rt_input.orderMatrix_1_0:N rt_input.orderMatrix_1_1:N rt_input.orderMatrix_1_2:N rt_input.orderMatrix_1_3:N rt_input.orderMatrix_2_0:N rt_input.orderMatrix_2_1:N rt_input.orderMatrix_2_2:N rt_input.orderMatrix_2_3:N\n");
    // printf("EMERGENCY rt_state.motorDirection:N rt_state.lastFloorAfterEmergency:N rt_state.previousMainFloor:N rt_state.floorBeyond:N rt_output.door_lamp:N rt_output.stop_lamp:N rt_input.currentFloorLocation:N rt_input.stopSignal:N rt_input.orderMatrix_0_0:N rt_input.orderMatrix_0_1:N rt_input.orderMatrix_0_2:N rt_input.orderMatrix_0_3:N rt_input.orderMatrix_1_0:N rt_input.orderMatrix_1_1:N rt_input.orderMatrix_1_2:N rt_input.orderMatrix_1_3:N rt_input.orderMatrix_2_0:N rt_input.orderMatrix_2_1:N rt_input.orderMatrix_2_2:N rt_input.orderMatrix_2_3:N\n");
    printf("INIT rt_state.currentState:E rt_state.motorDirection:N rt_state.previousMainFloor:N rt_state.lastFloorAfterEmergency:N rt_output.door_lamp:N rt_output.stop_lamp:N rt_input.currentFloorLocation:N rt_input.stopSignal:N rt_input.orderMatrix_0_0:N rt_input.orderMatrix_0_1:N rt_input.orderMatrix_0_2:N rt_input.orderMatrix_0_3:N rt_input.orderMatrix_1_0:N rt_input.orderMatrix_1_1:N rt_input.orderMatrix_1_2:N rt_input.orderMatrix_1_3:N rt_input.orderMatrix_2_0:N rt_input.orderMatrix_2_1:N rt_input.orderMatrix_2_2:N rt_input.orderMatrix_2_3:N\n");
    printf("IDLE rt_state.currentState:E rt_state.motorDirection:N rt_state.previousMainFloor:N rt_state.lastFloorAfterEmergency:N rt_output.door_lamp:N rt_output.stop_lamp:N rt_input.currentFloorLocation:N rt_input.stopSignal:N rt_input.orderMatrix_0_0:N rt_input.orderMatrix_0_1:N rt_input.orderMatrix_0_2:N rt_input.orderMatrix_0_3:N rt_input.orderMatrix_1_0:N rt_input.orderMatrix_1_1:N rt_input.orderMatrix_1_2:N rt_input.orderMatrix_1_3:N rt_input.orderMatrix_2_0:N rt_input.orderMatrix_2_1:N rt_input.orderMatrix_2_2:N rt_input.orderMatrix_2_3:N\n");
    printf("RUN rt_state.currentState:E rt_state.motorDirection:N rt_state.previousMainFloor:N rt_state.lastFloorAfterEmergency:N rt_output.door_lamp:N rt_output.stop_lamp:N rt_input.currentFloorLocation:N rt_input.stopSignal:N rt_input.orderMatrix_0_0:N rt_input.orderMatrix_0_1:N rt_input.orderMatrix_0_2:N rt_input.orderMatrix_0_3:N rt_input.orderMatrix_1_0:N rt_input.orderMatrix_1_1:N rt_input.orderMatrix_1_2:N rt_input.orderMatrix_1_3:N rt_input.orderMatrix_2_0:N rt_input.orderMatrix_2_1:N rt_input.orderMatrix_2_2:N rt_input.orderMatrix_2_3:N\n");
    printf("STOP rt_state.currentState:E rt_state.motorDirection:N rt_state.previousMainFloor:N rt_state.lastFloorAfterEmergency:N rt_output.door_lamp:N rt_output.stop_lamp:N rt_input.currentFloorLocation:N rt_input.stopSignal:N rt_input.orderMatrix_0_0:N rt_input.orderMatrix_0_1:N rt_input.orderMatrix_0_2:N rt_input.orderMatrix_0_3:N rt_input.orderMatrix_1_0:N rt_input.orderMatrix_1_1:N rt_input.orderMatrix_1_2:N rt_input.orderMatrix_1_3:N rt_input.orderMatrix_2_0:N rt_input.orderMatrix_2_1:N rt_input.orderMatrix_2_2:N rt_input.orderMatrix_2_3:N\n");
    printf("EMERGENCY rt_state.currentState:E rt_state.motorDirection:N rt_state.previousMainFloor:N rt_state.lastFloorAfterEmergency:N rt_output.door_lamp:N rt_output.stop_lamp:N rt_input.currentFloorLocation:N rt_input.stopSignal:N rt_input.orderMatrix_0_0:N rt_input.orderMatrix_0_1:N rt_input.orderMatrix_0_2:N rt_input.orderMatrix_0_3:N rt_input.orderMatrix_1_0:N rt_input.orderMatrix_1_1:N rt_input.orderMatrix_1_2:N rt_input.orderMatrix_1_3:N rt_input.orderMatrix_2_0:N rt_input.orderMatrix_2_1:N rt_input.orderMatrix_2_2:N rt_input.orderMatrix_2_3:N\n");
    printf("trace\n");
  }

  /* Initialize model */
  int i = 0;
  while(i < sizeof(inp)/sizeof(inp[0]))
  {
    int j = 0;
    STATE init_state;
    INPUT init_input;
    OUTPUT init_output;

    rt_state = init_state;
  	rt_input = init_input;
  	rt_output = init_output;
    model_initialize();
    initStates();
    
    while(j < sizeof(inp[0])/sizeof(inp[0][0]))
    {
      rt_input = get_rand_values();
      model_step();
      j++;
    }
    printf("trace\n");
    i++;
  }
  return 0;
}