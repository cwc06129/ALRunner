#define TASK(n) void TASK_##n

#define TASK(n) TASK_##n

# 1 "code.c"
# 1 "<built-in>"
# 1 "<command-line>"
# 1 "/usr/include/stdc-predef.h" 1 3 4
# 1 "<command-line>" 2
# 1 "code.c"
# 72 "code.c"
# 1 "model/oil.h" 1
# 73 "code.c" 2
# 1 "model/kernel.h" 1







int ActivateTask(unsigned char);
int Schedule();
int SetEvent(unsigned char, unsigned char);
int GetResource(unsigned char);
int ReleaseResource(unsigned char);
int ChainTask( unsigned char);
int TerminateTask();
int WaitEvent(unsigned char);
int ClearEvent(unsigned char);
int SetRelAlarm(unsigned char, int, int);
int CancelAlarm(unsigned char);
extern int os_on;
extern const int ON;
extern const int OFF;
void StartOS(unsigned char app_mode);
void ShutDownOS();
# 74 "code.c" 2
typedef int EE_ASSERTIONS;
const int EE_ASSERT_FIN = 0;
const int EE_ASSERT_INIT = 1;
const int EE_ASSERT_TASK1_FIRED = 2;
const int EE_ASSERT_TIMER_ISR_FIRED = 3;
const int EE_ASSERT_SYSTICK_ISR_FIRED = 4;
const int EE_ASSERT_TASK1_ENDED = 5;
const int EE_ASSERT_TASK2_FIRED = 6;
const int EE_ASSERT_TASK2_ENDED = 7;
const int EE_ASSERT_TASKS_ENDED = 8;
const int EE_ASSERT_DIM = 9;
EE_TYPEASSERTVALUE EE_assertions[EE_ASSERT_DIM];







EE_TYPEASSERTVALUE result;


volatile int task1_fired = 0;
volatile int task2_fired = 0;
volatile int task1_ended = 0;
volatile int task2_ended = 0;
volatile int isr1_fired = 0;
volatile int isr2_fired = 0;
volatile int counter = 0;







ISR(TEST_IRQ_2) {
  isr1_fired++;
  printf("~~~~Test ISR cnt: %d~~~~\n", isr1_fired);

  if (isr1_fired == 1) {
    EE_assert(EE_ASSERT_TIMER_ISR_FIRED, isr1_fired == 1, EE_ASSERT_TASK1_FIRED);
  }

  printf("~~~~Waiting for 10 activations of Timer ISR~~~~\n");





  SetRelAlarm(1, 10, 20);




  printf("~~~~End of Test ISR~~~~\n");
}

void EE_pit_handler() {
  isr2_fired++;
  printf("\t~~Timer ISR cnt: %d~~\n", isr2_fired);

  if (isr2_fired == 1) {
    EE_assert(
      EE_ASSERT_SYSTICK_ISR_FIRED, isr2_fired == 1, EE_ASSERT_TIMER_ISR_FIRED
    );
  }

  printf("\t~~Activating Task2~~ --> ");
  ActivateTask(2);
}

TASK(1)
{

  task1_fired++;
  printf("\tTask1 activated. cnt: %d, triggering Test ISR\n", task1_fired);
  if (task1_fired == 1) {
    EE_assert(EE_ASSERT_TASK1_FIRED, task1_fired == 1, EE_ASSERT_INIT);
  }






  ISR();
  return;



  printf("\tTask1 finished.\n");

  task1_ended++;

  if (task1_ended == 1) {
    EE_assert(
      EE_ASSERT_TASK1_ENDED, task1_ended == 1, EE_ASSERT_SYSTICK_ISR_FIRED
    );
  }
}


TASK(2)
{
  task2_fired++;
  printf("Task2 activated. cnt: %d\n", task2_fired);
  if (task2_fired == 1) {
    EE_assert(EE_ASSERT_TASK2_FIRED, task2_fired == 1, EE_ASSERT_TASK1_ENDED);
  }

  task2_ended++;
  if (task2_ended == 1) {
    EE_assert(EE_ASSERT_TASK2_ENDED, task2_ended == 1, EE_ASSERT_TASK2_FIRED);
  }
}





TASK(0)
{
    printf("Starting Erika\n");

    EE_assert(EE_ASSERT_INIT, (1), -1);




    ActivateTask(1);

    EE_assert(
      EE_ASSERT_TASKS_ENDED, task1_ended && task2_ended, EE_ASSERT_TASK2_ENDED
    );
    EE_assert_range(EE_ASSERT_FIN, EE_ASSERT_INIT, EE_ASSERT_TASKS_ENDED);
    result = EE_assert_last();


    if(result == 1) {
      printf("TEST SUCCESSFUL FINISHED!\n");
    } else {




      printf("TEST FAILED!\n");
    }




}

int testFor(){
	int a = 1;
	int i;
	for (i = 1; i < 10; i++){
		a = a + i;
		a = a * a;
	}
	return a;
}