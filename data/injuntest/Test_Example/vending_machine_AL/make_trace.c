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
	in.coin = rand() % 2;
  in.event = rand() % 6;
	return in;
}

int main()
{
  int inp[10][30];

  int header = 1;
  if(header)
  {
    printf("types rt_input.event:E rt_input.coin:N rt_state.amount:N rt_state.vending_time:N rt_output.emitted:N\n");
    printf("Initial rt_input.event:E rt_input.coin:N rt_state.amount:N rt_state.vending_time:N rt_output.emitted:N\n");
    printf("Ready rt_input.event:E rt_input.coin:N rt_state.amount:N rt_state.vending_time:N rt_output.emitted:N\n");
    printf("Inserting rt_input.event:E rt_input.coin:N rt_state.amount:N rt_state.vending_time:N rt_output.emitted:N\n");
    printf("Enabled rt_input.event:E rt_input.coin:N rt_state.amount:N rt_state.vending_time:N rt_output.emitted:N\n");
    printf("Emitting rt_input.event:E rt_input.coin:N rt_state.amount:N rt_state.vending_time:N rt_output.emitted:N\n");
    printf("Returning rt_input.event:E rt_input.coin:N rt_state.amount:N rt_state.vending_time:N rt_output.emitted:N\n");
    printf("Final rt_input.event:E rt_input.coin:N rt_state.amount:N rt_state.vending_time:N rt_output.emitted:N\n");
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