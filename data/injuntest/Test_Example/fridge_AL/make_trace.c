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
	in.emode = rand() % 3;
  in.door = rand() % 3;
  in.button = rand() % 2;
	return in;
}

int main()
{

  int inp[10][10];

  int header = 1;
  if(header)
  {
    printf("types\n");
    printf("Initial rt_input.button:S rt_input.emode:E rt_input.door:E rt_state.temp:N\n");
    printf("Normal_Mode rt_input.button:S rt_input.emode:E rt_input.door:E rt_state.temp:N\n");
    printf("Door_Open_Mode rt_input.button:S rt_input.emode:E rt_input.door:E rt_state.temp:N\n");
    printf("Energy_Safe_Mode rt_input.button:S rt_input.emode:E rt_input.door:E rt_state.temp:N\n");
    printf("OFF rt_input.button:S rt_input.emode:E rt_input.door:E rt_state.temp:N\n");
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