#ifndef OIL_H_
#define OIL_H_
// set max infos
#define NUM_OF_TASKS (2+2)
#define MIN_TASK_ID 0
#define MAX_TASK_ID (NUM_OF_TASKS-1)
#define NUM_RESOURCES (1+2)
#define MIN_RESOURCE_ID 0
#define MAX_RESOURCE_ID (NUM_RESOURCES-1)
#define NUM_EVENTS (1+2)
#define MIN_EVENT_ID 0
#define MAX_EVENT_ID (NUM_EVENTS-1)
#define NUM_OF_ALARMS (3+2)
#define MAX_PRIORITY (NUM_OF_TASKS-1)

// tasks
#define AcquisitionTask 1
#define ControlTask 2
// #define DisplayTask 3
//RESs
#define dataMutex 1
//EVTs
#define dummyEvent 1
//Alarms
// #define cyclic_display_alarm_100ms 3
#define cyclic_control_alarm_50ms 2
#define cyclic_acquisition_alarm_30ms 1

#endif
