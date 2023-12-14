#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

/**
 * 2022-12-29(Thu) Sohee Jung
 * ActiveLearning Example에서 code_run_random_sample.c 코드를 이용해서 trace을 random으로 generation하는 코드
*/
#include <stddef.h>
#include <stdio.h> 
#include <time.h>
#include <stdlib.h>                     /* This ert_main.c example uses printf/fflush */
#include "model.h"                     /* Model's header file */

INPUT get_rand_values()
{
	INPUT in;
  for(int i=0; i<4; i++) {
    in.obsDistance[i] = rand() % 60;
  }
	return in;
}

int main()
{
  srand(time(NULL));
  int inp[10][20];

  int header = 1;
  if(header)
  {
    printf("types\n");
    printf("On_spot rt_state.speed_status:E rt_input.obsDistance_0:N rt_input.obsDistance_1:N rt_input.obsDistance_2:N rt_input.obsDistance_3:N rt_state.zone_0:N rt_state.zone_1:N rt_state.zone_2:N rt_state.zone_3:N\n");
    printf("Turn_left rt_state.speed_status:E rt_input.obsDistance_0:N rt_input.obsDistance_1:N rt_input.obsDistance_2:N rt_input.obsDistance_3:N rt_state.zone_0:N rt_state.zone_1:N rt_state.zone_2:N rt_state.zone_3:N\n");
    printf("Turn_right rt_state.speed_status:E rt_input.obsDistance_0:N rt_input.obsDistance_1:N rt_input.obsDistance_2:N rt_input.obsDistance_3:N rt_state.zone_0:N rt_state.zone_1:N rt_state.zone_2:N rt_state.zone_3:N\n");
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