#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

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
//typedef int jmp_buf[(13 * 4)];
typedef signed char __int8_t ;
typedef unsigned char __uint8_t ;
typedef signed short __int16_t;
typedef unsigned short __uint16_t;
typedef __int16_t __int_least16_t;
typedef __uint16_t __uint_least16_t;
typedef signed int __int32_t;
typedef unsigned int __uint32_t;
typedef __int32_t __int_least32_t;
typedef __uint32_t __uint_least32_t;
typedef signed long long __int64_t;
typedef unsigned long long __uint64_t;
typedef void *_LOCK_T;
void __cygwin_lock_init(_LOCK_T *);
void __cygwin_lock_init_recursive(_LOCK_T *);
void __cygwin_lock_fini(_LOCK_T *);
void __cygwin_lock_lock(_LOCK_T *);
int __cygwin_lock_trylock(_LOCK_T *);
void __cygwin_lock_unlock(_LOCK_T *);
typedef long _off_t;
typedef short __dev_t;
typedef unsigned short __uid_t;
typedef unsigned short __gid_t;
 typedef long long _off64_t;
typedef long _fpos_t;
typedef _off64_t _fpos64_t;
typedef int _ssize_t;
//typedef unsigned int wint_t;
typedef struct
{
  int __count;
  union
  {
    wint_t __wch;
    unsigned char __wchb[4];
  } __value;
} _mbstate_t;
typedef _LOCK_T _flock_t;
typedef void *_iconv_t;
typedef int ptrdiff_t;
typedef unsigned int size_t;
typedef short unsigned int wchar_t;
typedef long int __off_t;
typedef int __pid_t;
 typedef long long int __loff_t;
typedef unsigned char u_char;
typedef unsigned short u_short;
typedef unsigned int u_int;
typedef unsigned long u_long;
typedef unsigned short ushort;
typedef unsigned int uint;
typedef unsigned long ulong;
typedef unsigned long clock_t;
//typedef long time_t;
struct timespec {
  time_t tv_sec;
  long tv_nsec;
};
struct itimerspec {
  struct timespec it_interval;
  struct timespec it_value;
};
typedef long daddr_t;
typedef char * caddr_t;
typedef int pid_t;
typedef _ssize_t ssize_t;
typedef unsigned short nlink_t;
typedef long fd_mask;
typedef struct _types_fd_set {
 fd_mask fds_bits[(((64)+(((sizeof (fd_mask) * 8))-1))/((sizeof (fd_mask) * 8)))];
} _types_fd_set;
typedef unsigned long clockid_t;
typedef unsigned long timer_t;
typedef unsigned long useconds_t;
typedef long suseconds_t;
typedef signed char int8_t;
typedef short int16_t;
typedef int int32_t;
typedef long long int64_t;
typedef unsigned char uint8_t;
typedef unsigned short uint16_t;
typedef unsigned int uint32_t;
typedef unsigned long long uint64_t;
typedef signed char int_least8_t;
typedef short int_least16_t;
typedef int int_least32_t;
typedef long long int_least64_t;
typedef unsigned char uint_least8_t;
typedef unsigned short uint_least16_t;
typedef unsigned int uint_least32_t;
typedef unsigned long long uint_least64_t;
typedef signed char int_fast8_t;
typedef int int_fast16_t;
typedef int int_fast32_t;
typedef long long int_fast64_t;
typedef unsigned char uint_fast8_t;
typedef unsigned int uint_fast16_t;
typedef unsigned int uint_fast32_t;
typedef unsigned long long uint_fast64_t;
typedef int intptr_t;
typedef unsigned int uintptr_t;
typedef long long intmax_t;
typedef unsigned long long uintmax_t;
/*static __inline unsigned short
bswap_16 (unsigned short __x)
{
  return (__x >> 8) | (__x << 8);
}
static __inline unsigned int
bswap_32 (unsigned int __x)
{
  return (bswap_16 (__x & 0xffff) << 16) | (bswap_16 (__x >> 16));
}
static __inline unsigned long long
bswap_64 (unsigned long long __x)
{
  return (((unsigned long long) bswap_32 (__x & 0xffffffffull)) << 32) | (bswap_32 (__x >> 32));
}*/
typedef struct timespec timespec_t;
typedef struct timespec timestruc_t;
typedef _off64_t off_t;
typedef __loff_t loff_t;
typedef short __dev16_t;
typedef unsigned long __dev32_t;
typedef __dev32_t dev_t;
typedef long blksize_t;
typedef long __blkcnt32_t;
typedef long long __blkcnt64_t;
typedef __blkcnt64_t blkcnt_t;
typedef unsigned long fsblkcnt_t;
typedef unsigned long fsfilcnt_t;
typedef unsigned short __uid16_t;
typedef unsigned long __uid32_t;
typedef __uid32_t uid_t;
typedef unsigned short __gid16_t;
typedef unsigned long __gid32_t;
typedef __gid32_t gid_t;
typedef unsigned long __ino32_t;
typedef unsigned long long __ino64_t;
typedef __ino64_t ino_t;
typedef unsigned long id_t;
struct flock {
 short l_type;
 short l_whence;
 off_t l_start;
 off_t l_len;
 pid_t l_pid;
};
typedef long long key_t;
typedef unsigned long vm_offset_t;
typedef unsigned long vm_size_t;
typedef void *vm_object_t;
typedef unsigned char u_int8_t;
typedef __uint16_t u_int16_t;
typedef __uint32_t u_int32_t;
typedef __uint64_t u_int64_t;
typedef __int32_t register_t;
typedef char *addr_t;
typedef unsigned mode_t;
typedef struct __pthread_t {char __dummy;} *pthread_t;
typedef struct __pthread_mutex_t {char __dummy;} *pthread_mutex_t;
typedef struct __pthread_key_t {char __dummy;} *pthread_key_t;
typedef struct __pthread_attr_t {char __dummy;} *pthread_attr_t;
typedef struct __pthread_mutexattr_t {char __dummy;} *pthread_mutexattr_t;
typedef struct __pthread_condattr_t {char __dummy;} *pthread_condattr_t;
typedef struct __pthread_cond_t {char __dummy;} *pthread_cond_t;
typedef struct
{
  pthread_mutex_t mutex;
  int state;
}
pthread_once_t;
typedef struct __pthread_rwlock_t {char __dummy;} *pthread_rwlock_t;
typedef struct __pthread_rwlockattr_t {char __dummy;} *pthread_rwlockattr_t;
typedef unsigned long sigset_t;
struct _fpstate
{
  unsigned long cw;
  unsigned long sw;
  unsigned long tag;
  unsigned long ipoff;
  unsigned long cssel;
  unsigned long dataoff;
  unsigned long datasel;
  unsigned char _st[80];
  unsigned long nxst;
};
struct ucontext
{
  unsigned long cr2;
  unsigned long dr0;
  unsigned long dr1;
  unsigned long dr2;
  unsigned long dr3;
  unsigned long dr6;
  unsigned long dr7;
  struct _fpstate fpstate;
  unsigned long gs;
  unsigned long fs;
  unsigned long es;
  unsigned long ds;
  unsigned long edi;
  unsigned long esi;
  unsigned long ebx;
  unsigned long edx;
  unsigned long ecx;
  unsigned long eax;
  unsigned long ebp;
  unsigned long eip;
  unsigned long cs;
  unsigned long eflags;
  unsigned long esp;
  unsigned long ss;
  unsigned char _internal;
  unsigned long oldmask;
};
typedef union sigval
{
  int sival_int;
  void *sival_ptr;
} sigval_t;
typedef struct sigevent
{
  sigval_t sigev_value;
  int sigev_signo;
  int sigev_notify;
  void (*sigev_notify_function) (sigval_t);
  pthread_attr_t *sigev_notify_attributes;
} sigevent_t;
#pragma pack(push,4)
struct _sigcommune
{
  __uint32_t _si_code;
  void *_si_read_handle;
  void *_si_write_handle;
  void *_si_process_handle;
   union
  {
    int _si_fd;
    void *_si_pipe_fhandler;
    char *_si_str;
  };
};
typedef struct
{
  int si_signo;
  int si_code;
  pid_t si_pid;
  uid_t si_uid;
  int si_errno;
   union
  {
    __uint32_t __pad[32];
    struct _sigcommune _si_commune;
     union
    {
      struct
      {
 union
 {
   struct
   {
     timer_t si_tid;
     unsigned int si_overrun;
   };
   sigval_t si_sigval;
   sigval_t si_value;
 };
      };
    };
     struct
    {
      int si_status;
      clock_t si_utime;
      clock_t si_stime;
    };
    void *si_addr;
  };
} siginfo_t;
#pragma pack(pop)
enum
{
  SI_USER = 0,
  SI_ASYNCIO = 2,
  SI_MESGQ,
  SI_TIMER,
  SI_QUEUE,
  SI_KERNEL,
  ILL_ILLOPC,
  ILL_ILLOPN,
  ILL_ILLADR,
  ILL_ILLTRP,
  ILL_PRVOPC,
  ILL_PRVREG,
  ILL_COPROC,
  ILL_BADSTK,
  FPE_INTDIV,
  FPE_INTOVF,
  FPE_FLTDIV,
  FPE_FLTOVF,
  FPE_FLTUND,
  FPE_FLTRES,
  FPE_FLTINV,
  FPE_FLTSUB,
  SEGV_MAPERR,
  SEGV_ACCERR,
  BUS_ADRALN,
  BUS_ADRERR,
  BUS_OBJERR,
  CLD_EXITED,
  CLD_KILLED,
  CLD_DUMPED,
  CLD_TRAPPED,
  CLD_STOPPED,
  CLD_CONTINUED
};
enum
{
  SIGEV_SIGNAL = 0,
  SIGEV_NONE,
  SIGEV_THREAD
};
typedef void (*_sig_func_ptr)(int);
struct sigaction
{
   union
  {
    _sig_func_ptr sa_handler;
    void (*sa_sigaction) ( int, siginfo_t *, void * );
  };
  sigset_t sa_mask;
  int sa_flags;
};
int sigwait (const sigset_t *, int *);
int sigwaitinfo (const sigset_t *, siginfo_t *);
int sighold (int);
int sigignore (int);
int sigrelse (int);
_sig_func_ptr sigset (int, _sig_func_ptr);
int sigqueue(pid_t, int, const union sigval);
int siginterrupt (int, int);
extern const char  *sys_sigabbrev[];
int  sigprocmask (int how, const sigset_t *set, sigset_t *oset);
int  pthread_sigmask (int how, const sigset_t *set, sigset_t *oset);
int  kill (pid_t, int);
int  killpg (pid_t, int);
int  sigaction (int, const struct sigaction *, struct sigaction *);
int  sigaddset (sigset_t *, const int);
int  sigdelset (sigset_t *, const int);
int  sigismember (const sigset_t *, int);
int  sigfillset (sigset_t *);
int  sigemptyset (sigset_t *);
int  sigpending (sigset_t *);
int  sigsuspend (const sigset_t *);
int  sigpause (int);
int  pthread_kill (pthread_t thread, int sig);
int  sigwaitinfo (const sigset_t *set, siginfo_t *info);
int  sigtimedwait (const sigset_t *set, siginfo_t *info, const struct timespec *timeout);
int  sigwait (const sigset_t *set, int *sig);
int  sigqueue (pid_t pid, int signo, const union sigval value);
typedef int sig_atomic_t;
typedef _sig_func_ptr sig_t;
typedef _sig_func_ptr sighandler_t;
struct _reent;
_sig_func_ptr  _signal_r (struct _reent *, int, _sig_func_ptr);
int  _raise_r (struct _reent *, int);
_sig_func_ptr  signal (int, _sig_func_ptr);
int  raise (int);
//typedef int sigjmp_buf[(13 * 4)+1+(sizeof (sigset_t)/sizeof (int))];
extern void _longjmp(jmp_buf, int);
extern int _setjmp(jmp_buf);
void  longjmp (jmp_buf __jmpb, int __retval);
int  setjmp (jmp_buf __jmpb);
extern char **environ;
void  _exit (int __status ) ;
int  access (const char *__path, int __amode );
unsigned  alarm (unsigned __secs );
int  chdir (const char *__path );
int  chmod (const char *__path, mode_t __mode );
int  chown (const char *__path, uid_t __owner, gid_t __group );
int  chroot (const char *__path );
int  close (int __fildes );
size_t  confstr (int __name, char *__buf, size_t __len);
char *  ctermid (char *__s );
char *  cuserid (char *__s );
int  daemon (int nochdir, int noclose);
int  dup (int __fildes );
int  dup2 (int __fildes, int __fildes2 );
int  dup3 (int __fildes, int __fildes2, int flags);
int  eaccess (const char *__path, int __mode);
void  endusershell (void);
int  euidaccess (const char *__path, int __mode);
int  execl (const char *__path, const char *, ... );
int  execle (const char *__path, const char *, ... );
int  execlp (const char *__file, const char *, ... );
int  execv (const char *__path, char * const __argv[] );
int  execve (const char *__path, char * const __argv[], char * const __envp[] );
int  execvp (const char *__file, char * const __argv[] );
int  execvpe (const char *__file, char * const __argv[], char * const __envp[] );
int  faccessat (int __dirfd, const char *__path, int __mode, int __flags);
int  fchdir (int __fildes);
int  fchmod (int __fildes, mode_t __mode );
int  fchown (int __fildes, uid_t __owner, gid_t __group );
int  fchownat (int __dirfd, const char *__path, uid_t __owner, gid_t __group, int __flags);
int  fexecve (int __fd, char * const __argv[], char * const __envp[] );
pid_t  fork (void );
long  fpathconf (int __fd, int __name );
int  fsync (int __fd);
int  fdatasync (int __fd);
char *  getcwd (char *__buf, size_t __size );
int  getdomainname (char *__name, size_t __len);
gid_t  getegid (void );
uid_t  geteuid (void );
gid_t  getgid (void );
int  getgroups (int __gidsetsize, gid_t __grouplist[] );
long  gethostid (void);
char *  getlogin (void );
int  getlogin_r (char *name, size_t namesize);
char *  getpass (const char *__prompt);
int  getpagesize (void);
int  getpeereid (int, uid_t *, gid_t *);
pid_t  getpgid (pid_t);
pid_t  getpgrp (void );
pid_t  getpid (void );
pid_t  getppid (void );
pid_t  getsid (pid_t);
uid_t  getuid (void );
char *  getusershell (void);
char *  getwd (char *__buf );
int  iruserok (unsigned long raddr, int superuser, const char *ruser, const char *luser);
int  isatty (int __fildes );
int  lchown (const char *__path, uid_t __owner, gid_t __group );
int  link (const char *__path1, const char *__path2 );
int  linkat (int __dirfd1, const char *__path1, int __dirfd2, const char *__path2, int __flags );
int  nice (int __nice_value );
off_t  lseek (int __fildes, off_t __offset, int __whence );
int  lockf (int __fd, int __cmd, off_t __len);
long  pathconf (const char *__path, int __name );
int  pause (void );
int  pthread_atfork (void (*)(void), void (*)(void), void (*)(void));
int  pipe (int __fildes[2] );
int  pipe2 (int __fildes[2], int flags);
ssize_t  pread (int __fd, void *__buf, size_t __nbytes, off_t __offset);
ssize_t  pwrite (int __fd, const void *__buf, size_t __nbytes, off_t __offset);
_ssize_t  read (int __fd, void *__buf, size_t __nbyte );
int  rresvport (int *__alport);
int  revoke (char *__path);
int  rmdir (const char *__path );
int  ruserok (const char *rhost, int superuser, const char *ruser, const char *luser);
void *  sbrk (ptrdiff_t __incr);
int  setegid (gid_t __gid );
int  seteuid (uid_t __uid );
int  setgid (gid_t __gid );
int  setgroups (int ngroups, const gid_t *grouplist );
int  setpgid (pid_t __pid, pid_t __pgid );
int  setpgrp (void );
int  setregid (gid_t __rgid, gid_t __egid);
int  setreuid (uid_t __ruid, uid_t __euid);
pid_t  setsid (void );
int  setuid (uid_t __uid );
void  setusershell (void);
unsigned  sleep (unsigned int __seconds );
void  swab (const void *, void *, ssize_t);
long  sysconf (int __name );
pid_t  tcgetpgrp (int __fildes );
int  tcsetpgrp (int __fildes, pid_t __pgrp_id );
char *  ttyname (int __fildes );
int  ttyname_r (int, char *, size_t);
int  unlink (const char *__path );
int  usleep (useconds_t __useconds);
int  vhangup (void );
_ssize_t  write (int __fd, const void *__buf, size_t __nbyte );
extern int  opterr;
extern int  optind;
extern int  optopt;
extern int  optreset;
extern char  *optarg;
int getopt (int, char * const *, const char *);
pid_t  vfork (void );
int  ftruncate (int __fd, off_t __length);
int  truncate (const char *, off_t __length);
int  getdtablesize (void);
int  setdtablesize (int);
useconds_t  ualarm (useconds_t __useconds, useconds_t __interval);
 int  gethostname (char *__name, size_t __len);
char *  mktemp (char *);
void  sync (void);
ssize_t  readlink (const char *__path, char *__buf, size_t __buflen);
ssize_t  readlinkat (int __dirfd1, const char *__path, char *__buf, size_t __buflen);
int  symlink (const char *__name1, const char *__name2);
int  symlinkat (const char *, int, const char *);
int  unlinkat (int, const char *, int);
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
typedef u8 tpl_os_state;
typedef u8 tpl_proc_type;
typedef void (* tpl_proc_function)(void);
typedef struct {
  const tpl_priority
    ceiling_priority;
  tpl_priority
    owner_prev_priority;
  tpl_bool
    taken;
} tpl_internal_resource;
struct TPL_PROC_STATIC {
  tpl_context
    context;
  tpl_stack
    stack;
  const tpl_proc_function
    entry;
  tpl_internal_resource * const
    internal_resource;
  const tpl_task_id
    id;
  const tpl_priority
    base_priority;
  const tpl_activate_counter
    max_activate_count;
  const tpl_proc_type
    type;
};
typedef struct TPL_PROC_STATIC tpl_proc_static;
struct TPL_PROC {
  struct TPL_RESOURCE *
    resources;
  tpl_activate_counter
    activate_count;
  tpl_priority
    priority;
  tpl_proc_state
    state;
};
typedef struct TPL_PROC tpl_proc;
typedef struct
{
  const tpl_proc_static * s_old;
  const tpl_proc_static * s_running;
  tpl_proc * old;
  tpl_proc * running;
  int running_id;
  u8 need_switch;
} tpl_kern_state;
typedef struct {
    u8 read;
    u8 size;
} tpl_fifo_state;
typedef struct {
    tpl_proc_id * fifo;
    u8 size;
} tpl_priority_level;
extern tpl_kern_state tpl_kern;
extern tpl_internal_resource INTERNAL_RES_SCHEDULER;
extern tpl_proc idle_task;
extern const tpl_proc_static idle_task_static;
extern const tpl_proc_static * const
  tpl_stat_proc_table[2 +0 +1];
extern tpl_proc * const
  tpl_dyn_proc_table[2 +0 +1];
tpl_os_state tpl_current_os_state(void);
tpl_application_mode tpl_get_active_application_mode_service(
    void);
void tpl_start_os_service(
    const tpl_application_mode mode);
void tpl_shutdown_os_service(
    const tpl_status error);
void tpl_schedule_from_running(void);
void tpl_schedule_from_dying(void);
void tpl_schedule_from_waiting(void);
void tpl_init_proc(
  const tpl_proc_id proc_id);
void tpl_put_preempted_proc(
  const tpl_proc_id proc_id);
void tpl_put_new_proc(
  const tpl_proc_id proc_id);
void tpl_init_os(
  const tpl_application_mode app_mode);
void tpl_get_internal_resource(
  const tpl_proc_id task_id);
void tpl_release_internal_resource(
  const tpl_proc_id task_id);
tpl_status tpl_activate_task(
    const tpl_task_id task_id);
tpl_status tpl_set_event(
    const tpl_task_id task_id,
    const tpl_event_mask incoming_event);
typedef tpl_status StatusType;
typedef tpl_proc_state TaskStateType;
typedef tpl_proc_state * TaskStateRefType;
typedef tpl_task_id TaskType;
typedef tpl_task_id * TaskRefType;
typedef tpl_tick TickType;
typedef tpl_tick * TickRefType;
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
void tpl_call_error_hook(
    const tpl_status error);
extern void ErrorHook(
    StatusType error);
extern void tpl_switch_context(
  const tpl_context * const old_context,
  const tpl_context * const new_context
);
extern void tpl_switch_context_from_it(
  const tpl_context * const old_context,
  const tpl_context * const new_context
);
extern void tpl_init_context(
  const tpl_proc_id proc_id);
extern void tpl_disable_interrupts(void);
extern void tpl_enable_interrupts(void);
extern void tpl_get_task_lock(void);
extern void tpl_release_task_lock(void);
extern void tpl_init_machine(void);
extern void tpl_sleep(void);
extern void tpl_shutdown(void);
struct TPL_COUNTER;
struct TPL_TIME_OBJ;
typedef u8 tpl_time_obj_state;
typedef tpl_status (* tpl_expire_func)(
    struct TPL_TIME_OBJ *
);
struct TPL_TIME_OBJ_STATIC {
  struct TPL_COUNTER *
    counter;
  const tpl_expire_func
    expire;
};
typedef struct TPL_TIME_OBJ_STATIC tpl_time_obj_static;
struct TPL_TIME_OBJ {
    tpl_time_obj_static * stat_part;
    struct TPL_TIME_OBJ * next_to;
    struct TPL_TIME_OBJ * prev_to;
    tpl_tick cycle;
    tpl_tick date;
    tpl_time_obj_state state;
};
typedef struct TPL_TIME_OBJ tpl_time_obj;
struct TPL_COUNTER {
  const tpl_tick
    ticks_per_base;
  const tpl_tick
    max_allowed_value;
  const tpl_tick
    min_cycle;
  tpl_tick
    current_tick;
  tpl_tick
    current_date;
  tpl_time_obj *
    first_to;
  tpl_time_obj *
    next_to;
};
typedef struct TPL_COUNTER tpl_counter;
extern tpl_counter SystemCounter_counter_desc;
void tpl_insert_time_obj(
    tpl_time_obj * time_obj);
void tpl_remove_time_obj(
    tpl_time_obj * time_obj);
tpl_status tpl_counter_tick(
    tpl_counter * counter);
void tpl_trace_task_execute(
  TaskType new_executed_task_id);
void tpl_trace_task_preempt(
  tpl_kern_state kernel_stat);
void tpl_trace_task_terminate(
  TaskType dying_task_id, TaskType
chained_task_id);
void tpl_trace_task_activate(
  TaskType task_id);
void tpl_trace_task_wait(
  TaskType waiting_task_id);
void tpl_trace_task_released(
  TaskType released_task_id, tpl_event_mask
event_id);
void tpl_trace_task_change_priority(
  TaskType priority_changing_task_id);
void tpl_trace_res_get(
  tpl_resource_id res_id, TaskType task_id);
void tpl_trace_res_released(
  tpl_resource_id res_id);
void tpl_trace_isr_run(
  tpl_isr_id running_isr_id);
void tpl_trace_isr_preempt(
  tpl_kern_state kernel_stat);
void tpl_trace_isr_terminate(
  tpl_isr_id dying_isr_id, tpl_isr_id chained_isr_id);
void tpl_trace_isr_activate(
  tpl_isr_id isr_id);
void tpl_trace_isr_change_priority(
  tpl_isr_id priority_changing_isr_id);
void tpl_trace_alarm_scheduled(
  tpl_time_obj * scheduled_alarm);
void tpl_trace_alarm_expire(
  tpl_time_obj * expired_alarm);
void tpl_trace_alarm_cancel(
  tpl_alarm_id cancelled_alarm_id);
void tpl_trace_tpl_init();
void tpl_trace_tpl_terminate();
typedef tpl_bool (* tpl_isr_helper)(void);
typedef void (* tpl_it_handler)(const void * );
struct TPL_IT_VECTOR_ENTRY {
    const tpl_it_handler func;
    void * args;
};
typedef struct TPL_IT_VECTOR_ENTRY tpl_it_vector_entry;
struct TPL_ISR_STATIC {
    const tpl_isr_helper
                              helper;
    struct TPL_ISR_STATIC *
                              next;
    const tpl_isr_id
                              isr_id;
};
typedef struct TPL_ISR_STATIC tpl_isr_static;
tpl_status tpl_terminate_isr2_service(void);
void tpl_central_interrupt_handler(const u16 id);
void tpl_central_interrupt_handler_2(const void * id);
void tpl_null_it(const void * );
void tpl_enable_all_interrupts_service(void);
void tpl_disable_all_interrupts_service(void);
void tpl_resume_all_interrupts_service(void);
void tpl_suspend_all_interrupts_service(void);
void tppl_resume_os_interrupts_service(void);
void tpl_suspend_os_interrupts_service(void);
//void  __assert (const char *, int, const char *) ;
//void  __assert_func (const char *, int, const char *, const char *) ;
volatile u32 tpl_locking_depth = 0;
tpl_bool tpl_user_task_lock = ((tpl_bool)0);
u32 tpl_cpt_user_task_lock_All = 0;
u32 tpl_cpt_user_task_lock_OS = 0;
//u32 tpl_cpt_os_task_lock = 0;
u8 tpl_cpt_os_task_lock = 0;
tpl_bool tpl_get_interrupt_lock_status(void)
{
    tpl_bool result;
    if( (((tpl_bool)1) == tpl_user_task_lock) || (tpl_cpt_user_task_lock_OS > 0) || (tpl_cpt_user_task_lock_All > 0) )
    {
        result = ((tpl_bool)1);
    }
    else
    {
        result = ((tpl_bool)0);
    }
    return result;
}
void tpl_reset_interrupt_lock_status(void)
{
 tpl_user_task_lock = ((tpl_bool)0);
 tpl_cpt_user_task_lock_All = 0;
 tpl_cpt_user_task_lock_OS = 0;
 tpl_locking_depth = tpl_cpt_os_task_lock;
}
void tpl_suspend_all_interrupts_service(void)
{
  tpl_disable_interrupts();
  tpl_locking_depth++;
  tpl_cpt_user_task_lock_All++;
}
void tpl_resume_all_interrupts_service(void)
{
 //((tpl_locking_depth >= 0) ? (void)0 : assert (tpl_locking_depth >= 0));
	//assert (tpl_locking_depth >= 0);
 if( tpl_cpt_user_task_lock_All != 0 )
 {
  tpl_locking_depth--;
  tpl_cpt_user_task_lock_All--;
  if( tpl_locking_depth == 0)
  {
   tpl_enable_interrupts();
  }
 }
}
void tpl_disable_all_interrupts_service(void)
{
  if( (tpl_cpt_user_task_lock_All == 0) && (tpl_cpt_user_task_lock_OS == 0) )
  {
   tpl_disable_interrupts();
   tpl_user_task_lock = ((tpl_bool)1);
  }
}
void tpl_enable_all_interrupts_service(void)
{
 if( tpl_user_task_lock != ((tpl_bool)0) )
 {
  tpl_user_task_lock = ((tpl_bool)0);
  tpl_enable_interrupts();
 }
}
void tpl_suspend_os_interrupts_service(void)
{
  tpl_disable_interrupts();
  tpl_locking_depth++;
  tpl_cpt_user_task_lock_OS++;
}
void tpl_resume_os_interrupts_service(void)
{
 //((tpl_locking_depth >= 0) ? (void)0 : assert (tpl_locking_depth >= 0));
	//assert (tpl_locking_depth >= 0);
 if( tpl_cpt_user_task_lock_OS != 0 )
 {
  tpl_locking_depth--;
  tpl_cpt_user_task_lock_OS--;
  if( tpl_locking_depth == 0)
  {
   tpl_enable_interrupts();
  }
 }
}
tpl_status tpl_terminate_isr2_service(void)
{
  tpl_status result = 0;
  tpl_get_task_lock();
  if(((tpl_bool)0)!=tpl_get_interrupt_lock_status()) { result = 19; }
  tpl_service.service_id = (26);
  if (result == (tpl_status)0) { result = (tpl_status)2; }
  if ((result == (tpl_status)0) && ((tpl_kern.running->resources) != ((void *)0))) { result = (tpl_status)6; }
  if ((0x1F & (result)) != 0) { tpl_call_error_hook(result); }
  tpl_release_task_lock();
  return result;
}
void tpl_null_it(const void * foo)
{
}