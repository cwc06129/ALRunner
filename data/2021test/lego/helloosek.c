#define TASK(n) void TASK_##n

#define TASK(n) TASK_##n

/* helloworld.c */
#include "kernel.h"
#include "ecrobot_interface.h"

/* nxtOSEK hook to be invoked from an ISR in category 2 */
void user_1ms_isr_type2(void){ /* do nothing */ }

TASK(OSEK_Task_Background)
{
  while(1){
    ecrobot_status_monitor("Hello, World!");
    systick_wait_ms(500); /* 500msec wait */
  }
}