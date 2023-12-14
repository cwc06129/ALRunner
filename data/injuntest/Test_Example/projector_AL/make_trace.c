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
	in.power_switch = rand() % 2;
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
    printf("OFF rt_input.power_switch:S rt_state.counter:N\n");
    printf("READY_TO_OFF rt_input.power_switch:S rt_state.counter:N\n");
    printf("READY_TO_ON rt_input.power_switch:S rt_state.counter:N\n");
    printf("ON rt_input.power_switch:S rt_state.counter:N\n");
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