#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

#define TASK(n) TASK_##n

# 1 "code.c"
# 1 "<built-in>"
# 1 "<command-line>"
# 1 "/usr/include/stdc-predef.h" 1 3 4
# 1 "<command-line>" 2
# 1 "code.c"
# 61 "code.c"
# 1 "model/kernel_deguc.c" 1







int ActivateTask(unsigned char);
int Schedule();
int SetEvent(unsigned char, unsigned char);
int GetResource(unsigned char);
int ReleaseResource(unsigned char);
int ChainTask( unsigned char);
int TerminateTask();
int WaitEvent(unsigned char);
int ClearEvent(unsigned char);
extern int os_on;
extern const int ON;
extern const int OFF;
void StartOS(unsigned char app_mode);
void ShutDownOS();
# 62 "code.c" 2
# 1 "model/oil.h" 1
# 63 "code.c" 2

typedef int EE_ASSERTIONS;
const int EE_ASSERT_FIN = 0;
const int EE_ASSERT_INIT = 1;
const int EE_ASSERT_TASK1_FIRED = 2;
const int EE_ASSERT_ISR_FIRED = 3;
const int EE_ASSERT_TASK2_FIRED = 4;
const int EE_ASSERT_TASKS_ENDED = 5;
const int EE_ASSERT_DIM = 6;

EE_TYPEASSERTVALUE EE_assertions[EE_ASSERT_DIM];







volatile EE_TYPEASSERTVALUE result;


volatile int task1_fired = 0;
volatile int task2_fired = 0;
volatile int task1_ended = 0;
volatile int task2_ended = 0;
volatile int isr1_fired = 0;
volatile int counter = 0;





ISR(IOReadKeypadISR) {
    isr1_fired++;

    printf("----------Test ISR (irq %d), cnt: %d----------\n", 0, isr1_fired);

    if (isr1_fired == 1) {
        EE_assert(EE_ASSERT_ISR_FIRED, isr1_fired == 1, EE_ASSERT_TASK1_FIRED);
    }

    ActivateTask(2);
    TerminateTask();
}

TASK(1)
{
  task1_fired++;

  printf("\tTask1 activated. cnt: %d\n", task1_fired);

  if (task1_fired == 1) {
    EE_assert(EE_ASSERT_TASK1_FIRED, task1_fired == 1, EE_ASSERT_INIT);
  }



   //ISR();
   return;



  printf("\tTask1 finished.\n");
  task1_ended++;
  TerminateTask();
}

TASK(2)
{
  task2_fired++;

  printf("\t\tTask2 activated. cnt: %d\n", task2_fired);

  if (task2_fired == 1) {
    EE_assert(EE_ASSERT_TASK2_FIRED, task2_fired == 1, EE_ASSERT_ISR_FIRED);
  }
  task2_ended++;

  printf("\t\tTask2 finished.\n");
  TerminateTask();
}





TASK(3)
{
    printf("Starting ERIKA\n");

    EE_assert(EE_ASSERT_INIT, (1), -1);




    ActivateTask(1);

    EE_assert(
      EE_ASSERT_TASKS_ENDED, task1_ended && task2_ended, EE_ASSERT_TASK2_FIRED
    );
    EE_assert_range(EE_ASSERT_FIN, EE_ASSERT_INIT, EE_ASSERT_TASKS_ENDED);
    result = EE_assert_last();

    if(result == 1) {
      printf("TEST SUCCESSFUL FINISHED!\n");
    } else {




      printf("TEST FAILED!\n");
    }





}

void main()
{
StartOS(1);
}