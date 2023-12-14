#ifndef MY_STUB
#define MY_STUB

#define TASK(n) void Func_##n()
#define ISR(n) void Func_##n()
#define ISR1(n) void Func_##n()
#define ISR2(n) void Func_##n()
// #define NULL 0
#define true 1
#define false 0
#define SysTimerCnt 0
#define NXT_PORT_A 0
#define NXT_PORT_B 1
#define NXT_PORT_C 2
#define NXT_PORT_S1 1
#define NXT_PORT_S2 2
#define NXT_PORT_S3 3
#define NXT_PORT_S4 4
#define PORT_SONAR 4
#define RUNTIME_CONNECTION
typedef unsigned int uint8;
typedef unsigned int TaskType;
typedef unsigned int * TaskRefType;
typedef unsigned int StatusType;
typedef unsigned int * StatusRefType;
typedef unsigned int AlarmType;
typedef unsigned int * AlarmRefType;
typedef unsigned int TickType;
typedef unsigned int * TickRefType;
typedef unsigned int TaskStateType;
typedef unsigned int * TaskStateRefType;
typedef unsigned int TaskStateType;
typedef unsigned int * TaskStateRefType;
typedef unsigned int EventMaskType;
typedef unsigned int * EventMaskRefType;
typedef unsigned int NetworkStatusType;
typedef unsigned int * NetworkStatusRefType;
typedef unsigned int ConfigType;
typedef unsigned int * ConfigRefType;
typedef unsigned int AppModeType;
typedef unsigned int * AppModeRefType;
typedef unsigned int MessageType;
typedef unsigned int * MessageRefType;
typedef unsigned int bool;
#define INVALID_RESOURCE 0
#define INVALID_ALARM 0
#define INVALID_TASK 0
#define RUNNING 0
#define SUSPENDED 0
#define READY 0
#define WAITING 0
#define OSServiceId_SetEvent 0

#define BT_NO_INIT 4
#define BT_INITIALIZED 5
#define BT_CONNECTED 6
#define BT_STREAM 7

typedef unsigned char U8;
typedef char S8;
typedef unsigned short U16;
typedef unsigned int U32;
typedef int S32;
typedef int SINT;

typedef char CHAR;
typedef unsigned int UINT;
typedef float F32;
typedef double F64;
// typedef int uint8_t;
#define gpioLed 0
#define UART5 1
#define uartStatusTxComplete 2
#define uartIntRxDataReady 2
#define uartStatusRxReady 2

#endif