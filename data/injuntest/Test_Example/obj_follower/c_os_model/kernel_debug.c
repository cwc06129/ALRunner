#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

/*
kernel.c includes implementation of system services(APIs) of OSEK/VDX OS spec.
Now there are only 8 APIs. APIs can be added later.
Author: Yoohee
Date: 08/09/2016
*/
#include <stdio.h>
#include <stdlib.h>
#include "osek.h"
#include "kernel.h"
#include "readyQ.h"
#include "mylib.h"

void handleISR();

API api;
unsigned char current_tid;
unsigned char current_prio;
unsigned char e_code;

int ActivateTask_Common(unsigned char reftask)
{	
	api = API_ActivateTask;
	//check whether max activation count has been reached
	if(reftask < 1 || reftask > NUM_OF_TASKS)
	{
		e_code = E_OS_ID;
		return 0;
	}
	else if(task_dyn_info[reftask].act_cnt < task_static_info[reftask].max_act_cnt)
	{
		if(task_state[reftask] == Suspended)
		{
			int i;
			//When it is transferred from suspended state, then all events are cleared.
			for(i = 0; i < NUM_EVENTS; i++)
			{
				Event_Table[i].task_alloc[reftask] = 0; //Cleared
			}
		}
		task_dyn_info[reftask].act_cnt++;
		push_task_into_readyQ(reftask, task_static_info[reftask].prio, 0, 0);
		/* background task of Erika? */
		// if(current_tid == 0){
		// 	current_tid = reftask;
		// 	return reschedule(API_ActivateTask, current_tid);
		// }
		return reschedule(API_ActivateTask, current_tid);
	}
	else
	{
		e_code = E_OS_LIMIT;
		return 0;
	}
}

int Schedule_Common()
{	
	api = API_Schedule;
	return reschedule(API_Schedule, current_tid);
}

int SetEvent_Common(unsigned char reftask, unsigned char eventid)
{
	// functionalities
	api = API_SetEvent;
	if(reftask < 1 || reftask > NUM_OF_TASKS){
		e_code = E_OS_ID;
		return 0;
	}
	//When caller task is a basic task
	else if(!task_static_info[reftask].extended)
	{
		e_code  = E_OS_ACCESS;
		return 0;
	}
	else if(task_state[reftask] == Suspended)
	{
		e_code = E_OS_STATE;
		return 0;
	}
	
	if(Event_Table[eventid].task_alloc[reftask] != 1)
	{
		//set event.
		Event_Table[eventid].task_alloc[reftask] = 1;  
		if(task_state[reftask] == Waiting ){		
			push_task_into_readyQ(reftask, task_static_info[reftask].prio, current_pc[reftask], 2);
		}
		// preempt this task if there is a task with higher priority

		return reschedule(API_SetEvent, current_tid);
	}
	return 0;
}

int GetResource_Common(unsigned char resid)
{
	api = API_GetResource;
	if(resid < MIN_RESOURCE_ID || resid > MAX_RESOURCE_ID)
	{
		e_code = E_OS_ID;
		return 0;
	}
	else if(is_alloc(resid) > 0 || task_static_info[current_tid].prio > Resource_Table[resid].c_prio)
	{
		e_code = E_OS_ACCESS;
		return 0;
	}
	else
	{
		Resource_Table[resid].alloc = current_tid;
		// change priorty if it is lower than the resource's ceiling priority
		if(Resource_Table[resid].c_prio > task_dyn_info[current_tid].dyn_prio)
		{
			task_dyn_info[current_tid].dyn_prio = Resource_Table[resid].c_prio;
		}
	}
	return 0;
}

int ReleaseResource_Common(unsigned char resid)
{
	unsigned char i;
	api = API_ReleaseResource;
	if(resid < MIN_RESOURCE_ID || resid > MAX_RESOURCE_ID)
	{
		e_code = E_OS_ID;
		return 0;
	}
	else if(is_alloc(resid) != current_tid)
	{
		e_code = E_OS_NOFUNC;
		return 0;
	}
	else if(Resource_Table[resid].c_prio < task_static_info[current_tid].prio)
	{
		e_code = E_OS_ACCESS;
		return 0;
	}
	else
	{		
		// release the resource and adjust the priority of this task  
		Resource_Table[resid].alloc = 0; 
		if(Resource_Table[resid].c_prio > task_static_info[current_tid].prio)
		{
			current_prio = task_static_info[current_tid].prio;
			i = 0;
			while(i < NUM_RESOURCES)
			{
				if(is_alloc(i) == current_tid && current_prio < Resource_Table[resid].c_prio)
				{
					current_prio = Resource_Table[i].c_prio;
				}
				i++;
			} 
		}
		
		task_dyn_info[current_tid].dyn_prio = current_prio;
		return reschedule(API_ReleaseResource, current_tid);
	}
	return 0;
}

int ChainTask_Common(unsigned char reftask)
{
	api = API_ChainTask;
	if(reftask < 1 || reftask > NUM_OF_TASKS)
	{
		e_code = E_OS_ID;
		return 0;
	}
	else if(has_resource(current_tid))
	{
		e_code = E_OS_RESOURCE;
		return 0;
	}
	else if(current_tid == reftask){}
	else if(task_dyn_info[reftask].act_cnt < task_static_info[reftask].max_act_cnt)
	{
		task_dyn_info[reftask].act_cnt++;
		push_task_into_readyQ(reftask, task_static_info[reftask].prio, 0, ACTIVATE);
	}
	else
	{
		e_code = E_OS_LIMIT;
	}
	
	//assume that max_act_cnt can never become negative value.
	//terminate this task.
	if(current_tid != reftask && task_dyn_info[current_tid].act_cnt > 0)
		task_dyn_info[current_tid].act_cnt--;

	if(task_dyn_info[current_tid].act_cnt > 0 ) //same task is in readyQ
	{
		push_task_into_readyQ(current_tid, task_dyn_info[current_tid].dyn_prio, 0, ACTIVATE);
	}
	else
	{
		task_state[current_tid] = Suspended;
	}
	return reschedule(API_ChainTask, current_tid);
}

int TerminateTask_Common()
{
	api = API_TerminateTask;
	if(has_resource(current_tid))
	{
		e_code = E_OS_RESOURCE;
		
	}
	if (task_dyn_info[current_tid].act_cnt > 0)
		task_dyn_info[current_tid].act_cnt--;
	if(task_dyn_info[current_tid].act_cnt > 0 )
	{
		task_state[current_tid] = Ready;
		//eventually(current_tid);
	}
	else
	{
		task_state[current_tid] = Suspended;
	}
	return reschedule(API_TerminateTask, current_tid);
}

int WaitEvent_Common(unsigned char eventid)
{	
	// functionalities
	api = API_WaitEvent;
	if(!task_static_info[current_tid].extended)
	{
		e_code = E_OS_ACCESS;
		return 0;
	}
	else if(has_resource(current_tid))
	{
		e_code = E_OS_RESOURCE;
		return 0;
	}
	
	//if an event is already set, the task keeps running.
	if(Event_Table[eventid].task_alloc[current_tid] == Evt_Set)
	{
		return 0;	
	}
	else
	{
		Event_Table[eventid].task_alloc[current_tid] = Evt_Wait;
		task_state[current_tid] = Waiting;
		return reschedule(API_WaitEvent, current_tid);
	}
}

int ClearEvent_Common(unsigned char eventid)
{
	// functionalities
	api = API_ClearEvent;
	if(!task_static_info[current_tid].extended)
	{
		e_code = E_OS_ACCESS;
		return 0;
	}
	if(Event_Table[eventid].task_alloc[current_tid] == 1)
		Event_Table[eventid].task_alloc[current_tid] = 0;
	return 0;
}

int SetRelAlarm_Common(unsigned char alarmID, int increment, int cycle)
{
	if(alarmID < 0 || alarmID >= NUM_OF_ALARMS){
		e_code = E_OS_ID;
		return 0;
	}
	else if(alarm_state[alarmID] != CANCELED){
		e_code = E_OS_STATE;
		return 0;
	}
	alarm_state[alarmID] = SET;
	alarm_info[alarmID].cycle = cycle;
	alarm_info[alarmID].next_alarm_tick = increment;
	return 0;
}
int CancelAlarm_Common(unsigned char alarmID)
{
	if(alarmID < 0 || alarmID >= NUM_OF_ALARMS){
		e_code = E_OS_ID;
		return 0;
	}
	else if(alarm_state[alarmID] == CANCELED){
		e_code = E_OS_NOFUNC;
		return 0;
	}
	alarm_state[alarmID] = CANCELED;
	return 0;
}

void ALARM_handler(int aid) {
	// 1. if alarm is set
	if(alarm_state[aid] == SET){
		// 2.1 check current time
		// 2.2 check cycle time arrived
		alarm_info[aid].next_alarm_tick = alarm_info[aid].next_alarm_tick - 1;
		if ( alarm_info[aid].next_alarm_tick == 0 ) {
			alarm_info[aid].next_alarm_tick = alarm_info[aid].cycle;
			alarm_state[aid] = ALARMED;
		}
	}
	// 2. if alarm is fired
	if(alarm_state[aid] == ALARMED){
		// 2.1 fire action
		if ( alarm_info[aid].api == API_ActivateTask ) {
			printf("Alarm fired: (%d) - AT(%d)\n", aid, alarm_info[aid].param1); fflush(stdout);
			ActivateTask_Common(alarm_info[aid].param1);
		} else if ( alarm_info[aid].api == API_SetEvent ) {
			printf("Alarm fired: (%d) - SE(%d, %d)\n", aid, alarm_info[aid].param1, alarm_info[aid].param2); fflush(stdout);
			SetEvent_Common(alarm_info[aid].param1, alarm_info[aid].param2);
		}
		// 2.2 transit to next state regarding isPeriodic
		if(alarm_info[aid].cycle > 0){
			// if cyclic alarm
			alarm_state[aid] = SET;
		} else {
			// if non-cyclic alarm
			alarm_state[aid] = CANCELED;
		}
	}
}

int postjob() {
	if(is_active_alarm_exists()){
		int aid = 0;
		for ( aid = 1 ; aid <= (NUM_OF_ALARMS-2) ; ++ aid ) {
			if (alarm_state[aid] != CANCELED) {
				ALARM_handler(aid);
			}
		}
		return 1;
	}
	handleISR();
	return 0;
}

int ActivateTask(unsigned char reftask)
{
	int ret = ActivateTask_Common(reftask);
	// ret |= postjob();
	return ret;
}
int Schedule()
{
	int ret = Schedule_Common();
	// ret |= postjob();
	return ret;
}
int SetEvent(unsigned char reftask, unsigned char eventid)
{
	int ret = SetEvent_Common(reftask, eventid);
	// ret |= postjob();
	return ret;
}
int GetResource(unsigned char resid)
{
	int ret = GetResource_Common(resid);
	// ret |= postjob();
	return ret;
}
int ReleaseResource(unsigned char resid)
{
	int ret = ReleaseResource_Common(resid);
	// ret |= postjob();
	return ret;
}
int ChainTask(unsigned char reftask)
{
	int ret = ChainTask_Common(reftask);
	// ret |= postjob();
	return ret;
}

int TerminateTask()
{
	int ret = TerminateTask_Common();
	// ret |= postjob();
	return ret;
}
int WaitEvent(unsigned char eventid)
{
	int ret = WaitEvent_Common(eventid);
	// ret |= postjob();
	return ret;
}
int ClearEvent(unsigned char eventid)
{
	int ret = ClearEvent_Common(eventid);
	// ret |= postjob();
	return ret;
}
int SetRelAlarm(unsigned char alarmID, int increment, int cycle)
{
	int ret = SetRelAlarm_Common(alarmID, increment, cycle);
	// ret |= postjob();
	return ret;
}
int CancelAlarm(unsigned char alarmID)
{
	int ret = CancelAlarm_Common(alarmID);
	// ret |= postjob();
	return ret;
}

int os_on;
const int ON = 1;
const int OFF = 0;

void StartOS(unsigned char app_mode)
{
	if(os_on == OFF)
	{
		os_on = ON;
		initialize();
		startup_process();
		app(); //call tasks here
	}
}

void ShutDownOS()
{
	if(os_on == ON)
	{
		os_on = OFF;
		exit(1);
	}
}