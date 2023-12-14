/*
osek.h contains definitions of macros, data structues of OSEK system.
config-dependent macros are defined in oil.h and invariant macros are defined in this file.
Author: Yoohee
Date: 08/09/2016
*/
#ifndef OSEK_H_
#define OSEK_H_

/* get configuration from oil.h file */
#include <time.h>
#include "oil.h"
#include "kernel.h"

/* define ERROR codes -- from OSEK/VDX OS specification */
#define E_OK 0
#define E_OS_ACCESS 1
#define E_OS_CALLLEVEL 2
#define E_OS_ID 3
#define E_OS_LIMIT 4
#define E_OS_NOFUNC 5
#define E_OS_RESOURCE 6
#define E_OS_STATE 7
#define E_OS_VALUE 8
#define E_OS_DISABLEDINT 9

/* define ERROR code -- additional ones for managing ready queue. */
#define E_OVER_QUEUE_SIZE 21
#define E_QUEUE_MANAGE 22
#define TEST 23

#define TICK_RESOLUTION 0.001

/* Task states */
typedef enum state{
	Suspended, Ready, Running, Waiting
}State;

/* Task states */
typedef enum evt_state{
	Evt_Clear, Evt_Set, Evt_Wait
} Evt_State;

typedef enum API{
	API_ActivateTask, API_TerminateTask, API_ChainTask, API_Schedule, API_GetResource, API_ReleaseResource, API_SetEvent,
	API_WaitEvent, API_ClearEvent
}API;
API api;
/* Data structure that store initial configurations */
typedef struct 
{
	unsigned char prio; 
	unsigned char preemptable; 
	unsigned char autostart;
	unsigned char extended;
	unsigned char event_owned;
	unsigned char resource_owned;
	unsigned char max_act_cnt;
}task_static_config;

/* DS to store dynamic state information */
typedef struct {
	unsigned char act_cnt;
	unsigned char dyn_prio;
}task_dynamic_stat;

/*task list of resource&event table. 
If ResourceTable[resid].task_alloc[2] is true, it means resource resid is allocated to task2.*/
typedef struct{
	unsigned int c_prio;
	unsigned char alloc;  
}Resource;

typedef struct{
	unsigned char owner;  	// DW: what is this variable? Event does not owned by only one task.
							//     not even used in anywhere
	Evt_State task_alloc[NUM_OF_TASKS];
}Event;

typedef struct{
	unsigned int cycle;
	unsigned int next_alarm_tick;
	API api;
	unsigned char param1;
	unsigned char param2;
} Alarm;
#define CANCELED 0
#define SET 1
#define ALARMED 2
int alarm_state[NUM_OF_ALARMS];
Alarm alarm_info[NUM_OF_ALARMS];
//current prio and current tid
extern unsigned char current_prio;
extern unsigned char current_tid;
extern int current_pc[NUM_OF_TASKS];

//error code
unsigned char e_code;

/* 
Arrays to save attributes.
They are actually declared in osek.h
*/
task_static_config task_static_info[NUM_OF_TASKS]; //static configuration of tasks
task_dynamic_stat task_dyn_info[NUM_OF_TASKS]; //dynamic state of tasks
unsigned char Ceiling_Prio[NUM_RESOURCES]; //celing priority of resources
unsigned char task_state[NUM_OF_TASKS]; //state of tasks
Event Event_Table[NUM_EVENTS]; //Table to save event allocation
Resource  Resource_Table[NUM_RESOURCES]; //Table to save resource allocation
/* inline functions to get current information of the model. (allocation, event set/clear) */ 

/* returns whether res is allocated or not. */
static __inline int is_alloc(unsigned char res){
	return Resource_Table[res].alloc;
}
 /* returns whether task tid has a resource or not. */
static __inline int has_resource(unsigned char tid){
	int has_resource = 0;
	int i;
	for(i = 0; i < NUM_RESOURCES; i++){
		if(Resource_Table[i].alloc == tid)
			has_resource = 1;
	}
	return has_resource;
}


/* returns whether task tid's event is all cleared. */
static __inline int event_cleared(unsigned char eid)
{
	int is_cleared = 1;
	int  j;
	for(j = 1; j <= NUM_OF_TASKS;j++)
	{
		if(Event_Table[eid].task_alloc[j])
		{
			is_cleared = 0;
			break;
		}
	}
	return is_cleared;
}
void initialize();
void app();
void startup_process();

/**************************************************************
 * scheduling info
 *************************************************************/
// printf("INIT %d, %d, %d\n", curr_step.cnt, n, t);
#define schedule_init(t, n)\
L_##n:;
// printf("schedule (%d, %d)\n", t, n);

// printf("SCHEDULE %d, %d, %d\n", curr_step.cnt, n, t);
#define schedule(t, n)\
current_pc[t] = n;\
return;\
L_##n:;
// printf("schedule (%d, %d)\n", t, n);
// assert(888 && curr_step.cnt == n);
// printf("next will be %d\n", n);\
// printf("jumpted to %d expected %d\n", n, curr_step.cnt);\

#define schedule_reset(t, n)\
current_pc[t] = n;\
return;
// printf("schedule (%d, %d)\n", t, n);\

#endif
