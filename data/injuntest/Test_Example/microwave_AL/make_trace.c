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

INPUT get_rand_values()
{
	INPUT in;
	in.event = rand() % 4;
  in.micro_time = rand() % 9 + 1;
	return in;
}

int main()
{

  int inp[10][10];

  int header = 1;
  if(header)
  {
    printf("types\n");
    printf("Door_closed rt_input.event:E rt_state.micro_time:N\n");
    printf("Door_open rt_input.event:E rt_state.micro_time:N\n");
    printf("Cooking rt_input.event:E rt_state.micro_time:N\n");
    printf("Cooking_complete rt_input.event:E rt_state.micro_time:N\n");
    printf("Cooking_interrupt rt_input.event:E rt_state.micro_time:N\n");
    printf("trace\n");
  }

  /* Initialize model */
  int i = 0;
  while(i < sizeof(inp)/sizeof(inp[0]))
  {
    int j = 0;
    STATE init_state;
    INPUT init_input;

    rt_state = init_state;
  	rt_input = init_input;
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