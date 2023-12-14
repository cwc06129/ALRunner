#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

/*
model.c has the hook routines function of the model. The module do these procedures.
1)initialize attributes
2)start operating system with startup_process
3)call app() and launch tasks. We assume StartOS is already called.
4)tasks will execute in app(), until ShutDownOS() is called. We assume it is never called.
Author: Yoohee
Date: 08/09/2016
*/
#include "osek.h"
#include "kernel.h"
#include "readyQ.h"
#include <assert.h>
#include <stdio.h>
#include "mylib.h"


unsigned char e_code = 0;

//local var for ceiling priority
unsigned char current_prio = 0;
unsigned char current_tid = 0;
int current_pc[NUM_OF_TASKS];

/*functions*/


void startup_process()
{
	unsigned int i = 0;
	for ( i = 0 ; i < NUM_OF_TASKS ; ++ i )
	{
		if(task_static_info[i].autostart)
		{
			task_dyn_info[i].act_cnt++;
			current_prio = task_static_info[i].prio;
			push_task_into_readyQ(i, current_prio, 0, ACTIVATE);
			task_state[i] = Ready;
		}
	}

	if(is_empty())
	{
		e_code = TEST;
	}
	else
	{
		get_task_from_readyQ();

		task_state[current_tid] = Running;
	}	
}
