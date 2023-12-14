/*
kernel.h includes prototype of system service(API) of OSEK OS.
Author: Yoohee
Date: 08/09/2016
*/
#ifndef KERNEL_H_
#define KERNEL_H_
#define OSDEFAULTAPPMODE 0

#define DeclareTask(n) 
#define DeclareResource(n) 
#define DeclareEvent(n) 
#define DeclareAlarm(n) 

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

int postjob();

int ActivateTask_Common(unsigned char);
int Schedule_Common();
int SetEvent_Common(unsigned char, unsigned char);
int GetResource_Common(unsigned char);
int ReleaseResource_Common(unsigned char);
int ChainTask_Common(unsigned char);
int TerminateTask_Common();
int WaitEvent_Common(unsigned char);
int ClearEvent_Common(unsigned char);
int SetRelAlarm_Common(unsigned char, int, int);
int CancelAlarm_Common(unsigned char);
extern int os_on;
extern const int ON;
extern const int OFF;
void StartOS(unsigned char app_mode);
void ShutDownOS();
#endif
