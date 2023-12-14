/*
readyQ.h includes functions and attributes relevant to ready queue of OSEK OS.
Author: Yoohee
Date: 08/09/2016
*/
#include "oil.h"
#include "osek.h"
#ifndef READY_Q_
#define READY_Q_
#define MAX_QUEUE_LENGTH 4

typedef enum{
	ACTIVATE, PREEMPT, RELEASE
}push_type;

typedef struct queue{
	unsigned char tid; 
	push_type type;
}Queue;

extern unsigned char idx;
extern int front[MAX_PRIORITY+1];
extern unsigned char k;
extern int rear[MAX_PRIORITY+1];
// extern unsigned char max_prio;
extern int size[MAX_PRIORITY+1];
extern int wholesize;
#define is_full(p) (size[(p)] == MAX_QUEUE_LENGTH-1)
#define is_empty() (wholesize == 0)
extern Queue readyQ[MAX_PRIORITY+1][MAX_QUEUE_LENGTH];
int reschedule(API api, unsigned char tid);
void push_task_into_readyQ(unsigned char t, unsigned char p, int pc, push_type flag);
void get_task_from_readyQ();
void initializeQueue();

#endif
