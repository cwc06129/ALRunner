#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

#define TASK(n) TASK_##n

TASK(1)
{
  task1_fired++;

  printf("\tTask1 activated. cnt: %d\n", task1_fired);

  if (task1_fired == 1) {
    EE_assert(EE_ASSERT_TASK1_FIRED, task1_fired == 1, EE_ASSERT_INIT);
  }



   ISR();
   return;



  printf("\tTask1 finished.\n");
  task1_ended++;
  TerminateTask();
}