typedef unsigned char u8;
typedef signed char s8;
typedef unsigned short u16;
typedef signed short s16;
typedef unsigned long u32;
typedef signed long s32;
typedef s8 tpl_proc_id;
typedef s8 tpl_priority;
typedef u8 tpl_activate_counter;
typedef u8 tpl_event_mask;
typedef u8 tpl_alarm_id;
typedef u8 tpl_resource_id;
typedef u32 tpl_tick;
typedef u8 * tpl_buffer;
typedef u32 tpl_time;
typedef int jmp_buf[16];
 int  _setjmp (jmp_buf);
 void  longjmp (jmp_buf, int) ;
typedef int sig_atomic_t;
typedef void (*__p_sig_fn_t)(int);
 __p_sig_fn_t  signal(int, __p_sig_fn_t);
 int  raise (int);
typedef int ptrdiff_t;
typedef unsigned int size_t;
typedef short unsigned int wchar_t;
typedef long __time32_t;
typedef long long __time64_t;
typedef __time32_t time_t;
typedef long _off_t;
typedef _off_t off_t;
typedef unsigned int _dev_t;
typedef _dev_t dev_t;
typedef short _ino_t;
typedef _ino_t ino_t;
typedef int _pid_t;
typedef _pid_t pid_t;
typedef unsigned short _mode_t;
typedef _mode_t mode_t;
typedef int _sigset_t;
typedef _sigset_t sigset_t;
typedef long _ssize_t;
typedef _ssize_t ssize_t;
typedef long long fpos64_t;
typedef long long off64_t;
typedef unsigned int useconds_t;
  typedef int intptr_t;
typedef unsigned long _fsize_t;
struct _finddata_t
{
 unsigned attrib;
 time_t time_create;
 time_t time_access;
 time_t time_write;
 _fsize_t size;
 char name[(260)];
};
struct _finddatai64_t {
    unsigned attrib;
    time_t time_create;
    time_t time_access;
    time_t time_write;
    long long size;
    char name[(260)];
};
struct _wfinddata_t {
     unsigned attrib;
     time_t time_create;
     time_t time_access;
     time_t time_write;
     _fsize_t size;
     wchar_t name[(260)];
};
struct _wfinddatai64_t {
    unsigned attrib;
    time_t time_create;
    time_t time_access;
    time_t time_write;
    long long size;
    wchar_t name[(260)];
};
 long  _findfirst (const char*, struct _finddata_t*);
 int  _findnext (long, struct _finddata_t*);
 int  _findclose (long);
 int  _chdir (const char*);
 char*  _getcwd (char*, int);
 int  _mkdir (const char*);
 char*  _mktemp (char*);
 int  _rmdir (const char*);
 int  _chmod (const char*, int);
 long long  _filelengthi64(int);
 long  _findfirsti64(const char*, struct _finddatai64_t*);
 int  _findnexti64(long, struct _finddatai64_t*);
 long long  _lseeki64(int, long long, int);
 long long  _telli64(int);
extern __inline__ off64_t lseek64 (int, off64_t, int);
extern __inline__ off64_t lseek64 (int fd, off64_t offset, int whence)
{
  return _lseeki64(fd, (long long) offset, whence);
}
 int  chdir (const char*);
 char*  getcwd (char*, int);
 int  mkdir (const char*);
 char*  mktemp (char*);
 int  rmdir (const char*);
 int  chmod (const char*, int);
 int  _access (const char*, int);
 int  _chsize (int, long);
 int  _close (int);
 int  _commit(int);
 int  _creat (const char*, int);
 int  _dup (int);
 int  _dup2 (int, int);
 long  _filelength (int);
 long  _get_osfhandle (int);
 int  _isatty (int);
 int  _eof (int);
 int  _locking (int, int, long);
 long  _lseek (int, long, int);
 int  _open (const char*, int, ...);
 int  _open_osfhandle (intptr_t, int);
 int  _pipe (int *, unsigned int, int);
 int  _read (int, void*, unsigned int);
 int  _setmode (int, int);
 int  remove (const char*);
 int  rename (const char*, const char*);
 int  _sopen (const char*, int, int, ...);
 long  _tell (int);
 int  _umask (int);
 int  _unlink (const char*);
 int  _write (int, const void*, unsigned int);
 int  _waccess(const wchar_t*, int);
 int  _wchmod(const wchar_t*, int);
 int  _wcreat(const wchar_t*, int);
 long  _wfindfirst(const wchar_t*, struct _wfinddata_t*);
 int  _wfindnext(long, struct _wfinddata_t *);
 int  _wunlink(const wchar_t*);
 int  _wopen(const wchar_t*, int, ...);
 int  _wsopen(const wchar_t*, int, int, ...);
 wchar_t *  _wmktemp(wchar_t*);
 long  _wfindfirsti64(const wchar_t*, struct _wfinddatai64_t*);
 int  _wfindnexti64(long, struct _wfinddatai64_t*);
 int  access (const char*, int);
 int  chsize (int, long );
 int  close (int);
 int  creat (const char*, int);
 int  dup (int);
 int  dup2 (int, int);
 int  eof (int);
 long  filelength (int);
 int  isatty (int);
 long  lseek (int, long, int);
 int  open (const char*, int, ...);
 int  read (int, void*, unsigned int);
 int  setmode (int, int);
 int  sopen (const char*, int, int, ...);
 long  tell (int);
 int  umask (int);
 int  unlink (const char*);
 int  write (int, const void*, unsigned int);
typedef short unsigned int wint_t;
typedef signed char int8_t;
typedef unsigned char uint8_t;
typedef short int16_t;
typedef unsigned short uint16_t;
typedef int int32_t;
typedef unsigned uint32_t;
typedef long long int64_t;
typedef unsigned long long uint64_t;
typedef signed char int_least8_t;
typedef unsigned char uint_least8_t;
typedef short int_least16_t;
typedef unsigned short uint_least16_t;
typedef int int_least32_t;
typedef unsigned uint_least32_t;
typedef long long int_least64_t;
typedef unsigned long long uint_least64_t;
typedef signed char int_fast8_t;
typedef unsigned char uint_fast8_t;
typedef short int_fast16_t;
typedef unsigned short uint_fast16_t;
typedef int int_fast32_t;
typedef unsigned int uint_fast32_t;
typedef long long int_fast64_t;
typedef unsigned long long uint_fast64_t;
  typedef unsigned int uintptr_t;
typedef long long intmax_t;
typedef unsigned long long uintmax_t;
 void  _cexit(void);
 void  _c_exit(void);
 int  _cwait (int*, _pid_t, int);
 _pid_t  _getpid(void);
 intptr_t  _execl (const char*, const char*, ...);
 intptr_t  _execle (const char*, const char*, ...);
 intptr_t  _execlp (const char*, const char*, ...);
 intptr_t  _execlpe (const char*, const char*, ...);
 intptr_t  _execv (const char*, const char* const*);
 intptr_t  _execve (const char*, const char* const*, const char* const*);
 intptr_t  _execvp (const char*, const char* const*);
 intptr_t  _execvpe (const char*, const char* const*, const char* const*);
 intptr_t  _spawnl (int, const char*, const char*, ...);
 intptr_t  _spawnle (int, const char*, const char*, ...);
 intptr_t  _spawnlp (int, const char*, const char*, ...);
 intptr_t  _spawnlpe (int, const char*, const char*, ...);
 intptr_t  _spawnv (int, const char*, const char* const*);
 intptr_t  _spawnve (int, const char*, const char* const*, const char* const*);
 intptr_t  _spawnvp (int, const char*, const char* const*);
 intptr_t  _spawnvpe (int, const char*, const char* const*, const char* const*);
 unsigned long 
 _beginthread (void (*)(void *), unsigned, void*);
 void  _endthread (void);
 unsigned long 
 _beginthreadex (void *, unsigned, unsigned  (void *),
    void*, unsigned, unsigned*);
 void  _endthreadex (unsigned);
 int  cwait (int*, pid_t, int);
 pid_t  getpid (void);
 intptr_t  execl (const char*, const char*, ...);
 intptr_t  execle (const char*, const char*, ...);
 intptr_t  execlp (const char*, const char*, ...);
 intptr_t  execlpe (const char*, const char*,...);
 intptr_t  execv (const char*, const char* const*);
 intptr_t  execve (const char*, const char* const*, const char* const*);
 intptr_t  execvp (const char*, const char* const*);
 intptr_t  execvpe (const char*, const char* const*, const char* const*);
 intptr_t  spawnl (int, const char*, const char*, ...);
 intptr_t  spawnle (int, const char*, const char*, ...);
 intptr_t  spawnlp (int, const char*, const char*, ...);
 intptr_t  spawnlpe (int, const char*, const char*, ...);
 intptr_t  spawnv (int, const char*, const char* const*);
 intptr_t  spawnve (int, const char*, const char* const*, const char* const*);
 intptr_t  spawnvp (int, const char*, const char* const*);
 intptr_t  spawnvpe (int, const char*, const char* const*, const char* const*);
extern int optind;
extern int optopt;
extern int opterr;
extern char *optarg;
extern int getopt( int, char * const [], const char * );
int  usleep(useconds_t useconds);
int ftruncate(int, off_t);
typedef u32 tpl_stack_word;
typedef u16 tpl_stack_size;
struct TPL_STACK {
    tpl_stack_word *stack_zone;
    tpl_stack_size stack_size;
};
typedef struct TPL_STACK *tpl_stack;
extern struct TPL_STACK idle_task_stack;
struct TPL_CONTEXT {
    jmp_buf initial;
    jmp_buf current;
};
typedef struct TPL_CONTEXT *tpl_context;
extern struct TPL_CONTEXT idle_task_context;
extern void tpl_sleep(void);
typedef tpl_proc_id tpl_task_id ;
typedef tpl_proc_id tpl_isr_id ;
typedef u8 tpl_bool;
typedef u8 tpl_status;
typedef u8 tpl_proc_state;
struct ALARM_BASE_TYPE {
    tpl_tick maxallowedvalue;
    tpl_tick ticksperbase;
    tpl_tick mincycle;
};
typedef struct ALARM_BASE_TYPE tpl_alarm_base;
typedef u8 tpl_application_mode;
typedef void (* tpl_callback_func)(void);
typedef tpl_status StatusType;
typedef tpl_proc_state TaskStateType;
typedef tpl_proc_state * TaskStateRefType;
typedef tpl_task_id TaskType;
typedef tpl_task_id * TaskRefType;
typedef tpl_tick TickType;
typedef tpl_tick * TickRefType;
StatusType ActivateTask(
    const TaskType task_id);
StatusType TerminateTask(void);
StatusType ChainTask(
    const TaskType task_id);
StatusType Schedule(void);
StatusType GetTaskID(
    TaskRefType task_id);
StatusType GetTaskState(
    const TaskType task_id,
    TaskStateRefType state);
typedef tpl_event_mask EventMaskType;
typedef tpl_event_mask * EventMaskRefType;
StatusType SetEvent(
    const TaskType task_id,
    const EventMaskType event);
StatusType ClearEvent(
    const EventMaskType event);
StatusType GetEvent(
    const TaskType task_id,
    const EventMaskRefType event);
StatusType WaitEvent(
    const EventMaskType event);
typedef tpl_alarm_base AlarmBaseType;
typedef tpl_alarm_base * AlarmBaseRefType;
typedef tpl_alarm_id AlarmType;
extern const tpl_tick OSMAXALLOWEDVALUE;
extern const tpl_tick OSTICKSPERBASE;
extern const tpl_tick OSMINCYCLE;
StatusType GetAlarmBase(
    const AlarmType alarm_id,
    AlarmBaseRefType info);
StatusType GetAlarm(
    const AlarmType alarm_id,
    TickRefType tick);
StatusType SetRelAlarm(
    const AlarmType alarm_id,
    const TickType increment,
    const TickType cycle);
StatusType SetAbsAlarm(
    const AlarmType alarm_id,
    const TickType start,
    const TickType cycle);
StatusType CancelAlarm(
  const AlarmType alarm_id);
typedef tpl_resource_id ResourceType;
extern const ResourceType RES_SCHEDULER;
StatusType GetResource(
    const ResourceType res_id);
StatusType ReleaseResource(
    const ResourceType res_id);
void EnableAllInterrupts(void);
void DisableAllInterrupts(void);
void ResumeAllInterrupts(void);
void SuspendAllInterrupts(void);
void ResumeOSInterrupts(void);
void SuspendOSInterrupts(void);
StatusType TerminateISR(void);
extern tpl_bool tpl_get_interrupt_lock_status(void);
extern void tpl_call_error_hook(const tpl_status error);
union ID_PARAM_BLOCK {
    tpl_task_id task_id;
    tpl_task_id *
                                    task_id_ref;
    tpl_resource_id res_id;
    tpl_alarm_id alarm_id;
};
union PARAM_PARAM_BLOCK {
    tpl_proc_state *
                                    state;
    tpl_tick tick;
    tpl_tick *
                                    tick_ref;
    tpl_alarm_base *
                                    alarm_base_ref;
    tpl_event_mask mask;
    tpl_event_mask *
                                    mask_ref;
};
union PARAM_PARAM_BLOCK_2 {
  tpl_tick tick;
  tpl_tick *
                                  tick_ref;
};
struct PARAM_BLOCK {
    union ID_PARAM_BLOCK id;
    union PARAM_PARAM_BLOCK param;
    union PARAM_PARAM_BLOCK_2 param2;
};
struct SERVICE_CALL_DESCRIPTOR {
    struct PARAM_BLOCK parameters;
    u8 service_id;
};
typedef struct SERVICE_CALL_DESCRIPTOR tpl_service_call_desc;
extern tpl_service_call_desc tpl_service;
typedef tpl_application_mode AppModeType;
AppModeType GetActiveApplicationMode(void);
extern void StartOS(
    const AppModeType mode
);
void ShutdownOS(
    const StatusType error
);
