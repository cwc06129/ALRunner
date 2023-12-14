#include "osek.h"
task_static_config task_static_info[NUM_OF_TASKS];
task_dynamic_stat task_dyn_info[NUM_OF_TASKS];
unsigned char task_state[NUM_OF_TASKS];
unsigned char Ceiling_Prio[NUM_RESOURCES];
Resource Resource_Table[NUM_RESOURCES];
Event Event_Table[NUM_EVENTS];
Alarm alarm_info[NUM_OF_ALARMS];
int alarm_state[NUM_OF_ALARMS];
void initialize(){
	task_static_info[0].max_act_cnt = 1;
	task_static_info[0].preemptable = 1;
	task_static_info[0].prio = 0;
	task_static_info[0].autostart = 0;
	task_static_info[0].extended = 0;
	
	task_static_info[AcquisitionTask].max_act_cnt = 1;
	task_static_info[AcquisitionTask].preemptable = 1;
	task_static_info[AcquisitionTask].prio = 3;
	task_static_info[AcquisitionTask].autostart = 0;
	task_static_info[AcquisitionTask].extended = 0;

	task_static_info[ControlTask].max_act_cnt = 1;
	task_static_info[ControlTask].preemptable = 1;
	task_static_info[ControlTask].prio = 2;
	task_static_info[ControlTask].autostart = 1;
	task_static_info[ControlTask].extended = 0;

	// task_static_info[DisplayTask].max_act_cnt = 1;
	// task_static_info[DisplayTask].preemptable = 1;
	// task_static_info[DisplayTask].prio = 1;
	// task_static_info[DisplayTask].autostart = 0;
	// task_static_info[DisplayTask].extended = 0;

	task_dyn_info[AcquisitionTask].dyn_prio = task_static_info[AcquisitionTask].prio;
	task_dyn_info[AcquisitionTask].act_cnt = 0;
	task_dyn_info[ControlTask].dyn_prio = task_static_info[ControlTask].prio;
	task_dyn_info[ControlTask].act_cnt = 0;
	// task_dyn_info[DisplayTask].dyn_prio = task_static_info[DisplayTask].prio;
	// task_dyn_info[DisplayTask].act_cnt = 0;

	alarm_state[cyclic_acquisition_alarm_30ms] = SET;
	alarm_info[cyclic_acquisition_alarm_30ms].cycle = 60;
	alarm_info[cyclic_acquisition_alarm_30ms].next_alarm_tick = 1;
	alarm_info[cyclic_acquisition_alarm_30ms].api = API_ActivateTask;
	alarm_info[cyclic_acquisition_alarm_30ms].param1 = AcquisitionTask;
	alarm_info[cyclic_acquisition_alarm_30ms].param2 = 0;

	alarm_state[cyclic_control_alarm_50ms] = SET;
	alarm_info[cyclic_control_alarm_50ms].cycle = 100;
	alarm_info[cyclic_control_alarm_50ms].next_alarm_tick = 1;
	alarm_info[cyclic_control_alarm_50ms].api = API_ActivateTask;
	alarm_info[cyclic_control_alarm_50ms].param1 = ControlTask;
	alarm_info[cyclic_control_alarm_50ms].param2 = 0;

	// alarm_state[cyclic_display_alarm_100ms] = SET;
	// alarm_info[cyclic_display_alarm_100ms].cycle = 200;
	// alarm_info[cyclic_display_alarm_100ms].next_alarm_tick = 1;
	// alarm_info[cyclic_display_alarm_100ms].api = API_ActivateTask;
	// alarm_info[cyclic_display_alarm_100ms].param1 = DisplayTask;
	// alarm_info[cyclic_display_alarm_100ms].param2 = 0;

	Resource_Table[dataMutex].c_prio = 4;
}