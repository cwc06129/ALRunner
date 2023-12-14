#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

/*
readyQ.c implements functions related to ready queue.
We assume that ready queue is a circular queue, with queue size 10.
MAX QUEUE LENGTH can be changed.
Author: Yoohee
Date: 08/09/2016
*/
#include <assert.h>
#include <stdio.h>
#include "readyQ.h"
#include "osek.h"
#include "mylib.h"
#include <stdlib.h>

unsigned char idx;
unsigned char k;
int front[MAX_PRIORITY+1];
int rear[MAX_PRIORITY+1];
unsigned char max_prio = 0;
Queue readyQ[MAX_PRIORITY+1][MAX_QUEUE_LENGTH]; //this queue keeps the ready list in the order of prority and arrival time
int size[MAX_PRIORITY+1];
int wholesize;

extern unsigned char current_tid, current_prio;

extern void eventually(int);
void push_task_into_readyQ(unsigned char t, unsigned char p, int pc, push_type pushkind)
{
	printf("push tid %d, prio %d\n", t, p); fflush(stdout);
	if(is_full(p)) //queue  is full
	{
		printf("Queue is full!\n"); fflush(stdout);
	}
	else
	{

		if(pushkind == PREEMPT)
		{ //preemption, insert in front of front.
			task_state[t] = Ready;
			front[p] = (MAX_QUEUE_LENGTH+front[p]-1)%MAX_QUEUE_LENGTH;
			k = front[p];
			readyQ[p][k].tid = t;
		}
		else
		{
			task_state[t] = Ready;
			k = rear[p];
			readyQ[p][k].tid = t;
			rear[p] = (MAX_QUEUE_LENGTH+k+1)%MAX_QUEUE_LENGTH;

		}
		size[p]++; //increase size to check out whether it is full or empty.
		wholesize++;
		//reassign max_prio if current prio is bigger.
		if(p > max_prio)
		{ 
			max_prio = p;
		}
	}
}

void get_task_from_readyQ()
{
	int i = 0;

	if(is_empty())
	{
		printf("Queue is empty\n"); fflush(stdout);
		current_tid = 0;
		current_prio = 0;
	}
	else
	{
		current_tid = readyQ[max_prio][front[max_prio]].tid;
		current_prio = max_prio;
		//truncate popped index
		readyQ[max_prio][front[max_prio]].tid = 0; 
		//redefine front variable
		front[max_prio] = (front[max_prio]+1)%MAX_QUEUE_LENGTH;
		size[current_prio]--;
		wholesize--;

		//re-calculate max_prio.
		max_prio = current_prio;
		while(!size[max_prio] && max_prio != 0)
		{
			max_prio--;
		}
		task_state[current_tid] = Running;
	}
	printf("pop tid %d, prio %d\n", current_tid, current_prio); fflush(stdout);
}

int reschedule(API api, unsigned char tid){
	if(api == API_TerminateTask || api == API_ChainTask || api == API_WaitEvent)
	{
		get_task_from_readyQ();

		// DW modified it to make it reschedule always
		return 1;
	}
	else if((!task_static_info[tid].preemptable) && api != API_Schedule)
		return 0;
	else if(task_dyn_info[tid].dyn_prio < max_prio)
	{
		push_task_into_readyQ(tid, task_dyn_info[tid].dyn_prio, current_pc[tid], PREEMPT);
		get_task_from_readyQ();
		if(current_tid == tid) 
			return 0;
		else
			return 1;
	}
	else {
		return 0;
	}
}