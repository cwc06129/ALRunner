#include <stdlib.h>
#include <assert.h>
#include "annotate.h"
#include "../c_os_model/os.h"

extern void check_property();

int main() {

  srand((unsigned int)time(NULL));
  StartOS(1);
  return 0;
}

int activation_time[NUM_OF_TASKS];

void app(){

  int i;
  int count = 0;
  current_pc[AcquisitionTask] = 1;
  current_pc[ControlTask] = 15;
  // current_pc[DisplayTask] = 29;

  while (is_active_obj_exists() && os_on == ON) {
    // Loop limitation.
    if (count >= 300) break;

    // 1. there is one task is running, at most.
    int run_cnt = 0;
    for (i = 0 ; i < NUM_OF_TASKS ; ++i) {
      if (task_state[i] == Running) {
        run_cnt ++;
      }
      if (task_state[i] != Suspended) {
        activation_time[i] ++;
      } else {
        activation_time[i] = 0;
      }
    }
    assert(run_cnt < 2);

    if(current_tid == AcquisitionTask) {
      //TASK_CALLED(AcquisitionTask);
      jump_AcquisitionTask();
    } else if(current_tid == ControlTask) {
      //TASK_CALLED(ControlTask);
      jump_ControlTask();
    }
    // else if(current_tid == DisplayTask) {
    //   TASK_CALLED(DisplayTask);
    //   Func_DisplayTask();
    // }
    
    postjob();
    check_property();
    count += 1;
  }
}