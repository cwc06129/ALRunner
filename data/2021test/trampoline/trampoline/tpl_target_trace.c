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
struct timeval {
  time_t tv_sec;
  suseconds_t tv_usec;
};
struct timezone {
  int tz_minuteswest;
  int tz_dsttime;
};
typedef unsigned long __ULong;
struct _reent;
struct _Bigint
{
  struct _Bigint *_next;
  int _k, _maxwds, _sign, _wds;
  __ULong _x[1];
};
struct __tm
{
  int __tm_sec;
  int __tm_min;
  int __tm_hour;
  int __tm_mday;
  int __tm_mon;
  int __tm_year;
  int __tm_wday;
  int __tm_yday;
  int __tm_isdst;
};
struct _on_exit_args {
 void * _fnargs[32];
 void * _dso_handle[32];
 __ULong _fntypes;
 __ULong _is_cxa;
};
struct _atexit {
 struct _atexit *_next;
 int _ind;
 void (*_fns[32])(void);
        struct _on_exit_args _on_exit_args;
};
struct __sbuf {
 unsigned char *_base;
 int _size;
};
struct __sFILE {
  unsigned char *_p;
  int _r;
  int _w;
  short _flags;
  short _file;
  struct __sbuf _bf;
  int _lbfsize;
  void * _cookie;
  _ssize_t ( * _read) (struct _reent *, void *, char *, int);
  _ssize_t ( * _write) (struct _reent *, void *, const char *, int);
  _fpos_t ( * _seek) (struct _reent *, void *, _fpos_t, int);
  int ( * _close) (struct _reent *, void *);
  struct __sbuf _ub;
  unsigned char *_up;
  int _ur;
  unsigned char _ubuf[3];
  unsigned char _nbuf[1];
  struct __sbuf _lb;
  int _blksize;
  int _offset;
  struct _reent *_data;
  _flock_t _lock;
  _mbstate_t _mbstate;
  int _flags2;
};
struct __sFILE64 {
  unsigned char *_p;
  int _r;
  int _w;
  short _flags;
  short _file;
  struct __sbuf _bf;
  int _lbfsize;
  struct _reent *_data;
  void * _cookie;
  _ssize_t ( * _read) (struct _reent *, void *, char *, int);
  _ssize_t ( * _write) (struct _reent *, void *, const char *, int);
  _fpos_t ( * _seek) (struct _reent *, void *, _fpos_t, int);
  int ( * _close) (struct _reent *, void *);
  struct __sbuf _ub;
  unsigned char *_up;
  int _ur;
  unsigned char _ubuf[3];
  unsigned char _nbuf[1];
  struct __sbuf _lb;
  int _blksize;
  int _flags2;
  _off64_t _offset;
  _fpos64_t ( * _seek64) (struct _reent *, void *, _fpos64_t, int);
  _flock_t _lock;
  _mbstate_t _mbstate;
};
typedef struct __sFILE64 __FILE;
struct _glue
{
  struct _glue *_next;
  int _niobs;
  __FILE *_iobs;
};
struct _rand48 {
  unsigned short _seed[3];
  unsigned short _mult[3];
  unsigned short _add;
};
struct _reent
{
  int _errno;
  __FILE *_stdin, *_stdout, *_stderr;
  int _inc;
  char _emergency[25];
  int _current_category;
  const char *_current_locale;
  int __sdidinit;
  void ( * __cleanup) (struct _reent *);
  struct _Bigint *_result;
  int _result_k;
  struct _Bigint *_p5s;
  struct _Bigint **_freelist;
  int _cvtlen;
  char *_cvtbuf;
  union
    {
      struct
        {
          unsigned int _unused_rand;
          char * _strtok_last;
          char _asctime_buf[26];
          struct __tm _localtime_buf;
          int _gamma_signgam;
           unsigned long long _rand_next;
          struct _rand48 _r48;
          _mbstate_t _mblen_state;
          _mbstate_t _mbtowc_state;
          _mbstate_t _wctomb_state;
          char _l64a_buf[8];
          char _signal_buf[24];
          int _getdate_err;
          _mbstate_t _mbrlen_state;
          _mbstate_t _mbrtowc_state;
          _mbstate_t _mbsrtowcs_state;
          _mbstate_t _wcrtomb_state;
          _mbstate_t _wcsrtombs_state;
   int _h_errno;
        } _reent;
      struct
        {
          unsigned char * _nextf[30];
          unsigned int _nmalloc[30];
        } _unused;
    } _new;
  struct _atexit *_atexit;
  struct _atexit _atexit0;
  void (**(_sig_func))(int);
  struct _glue __sglue;
  __FILE __sf[3];
};
extern struct _reent *_impure_ptr ;
extern struct _reent *const _global_impure_ptr ;
void _reclaim_reent (struct _reent *);
  //struct _reent *  __getreent (void);
struct tm
{
  int tm_sec;
  int tm_min;
  int tm_hour;
  int tm_mday;
  int tm_mon;
  int tm_year;
  int tm_wday;
  int tm_yday;
  int tm_isdst;
};
clock_t  clock (void);
double  difftime (time_t _time2, time_t _time1);
time_t  mktime (struct tm *_timeptr);
time_t  time (time_t *_timer);
char * asctime (const struct tm *_tblock);
char * ctime (const time_t *_time);
struct tm * gmtime (const time_t *_timer);
struct tm * localtime (const time_t *_timer);
size_t  strftime (char *_s, size_t _maxsize, const char *_fmt, const struct tm *_t);
char * asctime_r (const struct tm *, char *);
char * ctime_r (const time_t *, char *);
struct tm * gmtime_r (const time_t *, struct tm *);
struct tm * localtime_r (const time_t *, struct tm *);
char * strptime (const char *, const char *, struct tm *);
void  tzset (void);
void  _tzset_r (struct _reent *);
typedef struct __tzrule_struct
{
  char ch;
  int m;
  int n;
  int d;
  int s;
  time_t change;
  long offset;
} __tzrule_type;
typedef struct __tzinfo_struct
{
  int __tznorth;
  int __tzyear;
  __tzrule_type __tzrule[2];
} __tzinfo_type;
__tzinfo_type * __gettzinfo (void);
extern  long _timezone;
extern  int _daylight;
extern  char *_tzname[2];
int  clock_setres (clockid_t, struct timespec *);
time_t  timelocal (struct tm *);
time_t  timegm (struct tm *);
extern int daylight ;
extern long timezone ;
int  clock_settime (clockid_t clock_id, const struct timespec *tp);
int  clock_gettime (clockid_t clock_id, struct timespec *tp);
int  clock_getres (clockid_t clock_id, struct timespec *res);
int  timer_create (clockid_t clock_id, struct sigevent *evp, timer_t *timerid);
int  timer_delete (timer_t timerid);
int  timer_settime (timer_t timerid, int flags, const struct itimerspec *value, struct itimerspec *ovalue);
int  timer_gettime (timer_t timerid, struct itimerspec *value);
int  timer_getoverrun (timer_t timerid);
int  nanosleep (const struct timespec *rqtp, struct timespec *rmtp);
int select (int __n, _types_fd_set *__readfds, _types_fd_set *__writefds, _types_fd_set *__exceptfds, struct timeval *__timeout);
int pselect (int __n, _types_fd_set *__readfds, _types_fd_set *__writefds, _types_fd_set *__exceptfds, const struct timespec *__timeout, const sigset_t *__set);
int futimes (int, const struct timeval *);
int lutimes (const char *, const struct timeval *);
struct itimerval {
  struct timeval it_interval;
  struct timeval it_value;
};
int  gettimeofday (struct timeval *__p, void *__tz);
int  settimeofday (const struct timeval *, const struct timezone *);
int  utimes (const char *__path, const struct timeval *__tvp);
int  getitimer (int __which, struct itimerval *__value);
int  setitimer (int __which, const struct itimerval *__value, struct itimerval *__ovalue);
typedef __builtin_va_list __gnuc_va_list;
//typedef __FILE FILE;
typedef _fpos64_t fpos_t;
ssize_t  getline (char **, size_t *, FILE *);
ssize_t  getdelim (char **, size_t *, int, FILE *);
FILE *  tmpfile (void);
char *  tmpnam (char *);
int  fclose (FILE *);
int  fflush (FILE *);
FILE *  freopen (const char *, const char *, FILE *);
void  setbuf (FILE *, char *);
int  setvbuf (FILE *, char *, int, size_t);
int  fprintf (FILE *, const char *, ...) ;
int  fscanf (FILE *, const char *, ...) ;
int  printf (const char *, ...) ;
int  scanf (const char *, ...) ;
int  sscanf (const char *, const char *, ...) ;
int  vfprintf (FILE *, const char *, __gnuc_va_list) ;
int  vprintf (const char *, __gnuc_va_list) ;
int  vsprintf (char *, const char *, __gnuc_va_list) ;
int  fgetc (FILE *);
char *  fgets (char *, int, FILE *);
int  fputc (int, FILE *);
int  fputs (const char *, FILE *);
int  getc (FILE *);
int  getchar (void);
char *  gets (char *);
int  putc (int, FILE *);
int  putchar (int);
int  puts (const char *);
int  ungetc (int, FILE *);
size_t  fread (void *, size_t _size, size_t _n, FILE *);
size_t  fwrite (const void * , size_t _size, size_t _n, FILE *);
int  fgetpos (FILE *, fpos_t *);
int  fseek (FILE *, long, int);
int  fsetpos (FILE *, const fpos_t *);
long  ftell ( FILE *);
void  rewind (FILE *);
void  clearerr (FILE *);
int  feof (FILE *);
int  ferror (FILE *);
//void  perror (const char *);
FILE *  fopen (const char *_name, const char *_type);
int  sprintf (char *, const char *, ...) ;
int  remove (const char *);
int  rename (const char *, const char *);
int  fseeko (FILE *, off_t, int);
off_t  ftello ( FILE *);
int  asiprintf (char **, const char *, ...) ;
char *  asniprintf (char *, size_t *, const char *, ...) ;
char *  asnprintf (char *, size_t *, const char *, ...) ;
int  asprintf (char **, const char *, ...) ;
int  diprintf (int, const char *, ...) ;
int  fcloseall (void);
int  fiprintf (FILE *, const char *, ...) ;
int  fiscanf (FILE *, const char *, ...) ;
int  iprintf (const char *, ...) ;
int  iscanf (const char *, ...) ;
int  siprintf (char *, const char *, ...) ;
int  siscanf (const char *, const char *, ...) ;
int  snprintf (char *, size_t, const char *, ...) ;
int  sniprintf (char *, size_t, const char *, ...) ;
char *  tempnam (const char *, const char *);
int  vasiprintf (char **, const char *, __gnuc_va_list) ;
char *  vasniprintf (char *, size_t *, const char *, __gnuc_va_list) ;
char *  vasnprintf (char *, size_t *, const char *, __gnuc_va_list) ;
int  vasprintf (char **, const char *, __gnuc_va_list) ;
int  vdiprintf (int, const char *, __gnuc_va_list) ;
int  vfiprintf (FILE *, const char *, __gnuc_va_list) ;
int  vfiscanf (FILE *, const char *, __gnuc_va_list) ;
int  vfscanf (FILE *, const char *, __gnuc_va_list) ;
int  viprintf (const char *, __gnuc_va_list) ;
int  viscanf (const char *, __gnuc_va_list) ;
int  vscanf (const char *, __gnuc_va_list) ;
int  vsiprintf (char *, const char *, __gnuc_va_list) ;
int  vsiscanf (const char *, const char *, __gnuc_va_list) ;
int  vsniprintf (char *, size_t, const char *, __gnuc_va_list) ;
int  vsnprintf (char *, size_t, const char *, __gnuc_va_list) ;
int  vsscanf (const char *, const char *, __gnuc_va_list) ;
FILE *  fdopen (int, const char *);
int  fileno (FILE *);
int  getw (FILE *);
int  pclose (FILE *);
FILE *  popen (const char *, const char *);
int  putw (int, FILE *);
void  setbuffer (FILE *, char *, int);
int  setlinebuf (FILE *);
int  getc_unlocked (FILE *);
int  getchar_unlocked (void);
void  flockfile (FILE *);
int  ftrylockfile (FILE *);
void  funlockfile (FILE *);
int  putc_unlocked (int, FILE *);
int  putchar_unlocked (int);
int  dprintf (int, const char *, ...) ;
FILE *  fmemopen (void *, size_t, const char *);
FILE *  open_memstream (char **, size_t *);
int  renameat (int, const char *, int, const char *);
int  vdprintf (int, const char *, __gnuc_va_list) ;
int  _asiprintf_r (struct _reent *, char **, const char *, ...) ;
char *  _asniprintf_r (struct _reent *, char *, size_t *, const char *, ...) ;
char *  _asnprintf_r (struct _reent *, char *, size_t *, const char *, ...) ;
int  _asprintf_r (struct _reent *, char **, const char *, ...) ;
int  _diprintf_r (struct _reent *, int, const char *, ...) ;
int  _dprintf_r (struct _reent *, int, const char *, ...) ;
int  _fclose_r (struct _reent *, FILE *);
int  _fcloseall_r (struct _reent *);
FILE *  _fdopen_r (struct _reent *, int, const char *);
int  _fflush_r (struct _reent *, FILE *);
int  _fgetc_r (struct _reent *, FILE *);
char *  _fgets_r (struct _reent *, char *, int, FILE *);
int  _fgetpos_r (struct _reent *, FILE *, fpos_t *);
int  _fsetpos_r (struct _reent *, FILE *, const fpos_t *);
int  _fiprintf_r (struct _reent *, FILE *, const char *, ...) ;
int  _fiscanf_r (struct _reent *, FILE *, const char *, ...) ;
FILE *  _fmemopen_r (struct _reent *, void *, size_t, const char *);
FILE *  _fopen_r (struct _reent *, const char *, const char *);
FILE *  _freopen_r (struct _reent *, const char *, const char *, FILE *);
int  _fprintf_r (struct _reent *, FILE *, const char *, ...) ;
int  _fpurge_r (struct _reent *, FILE *);
int  _fputc_r (struct _reent *, int, FILE *);
int  _fputs_r (struct _reent *, const char *, FILE *);
size_t  _fread_r (struct _reent *, void *, size_t _size, size_t _n, FILE *);
int  _fscanf_r (struct _reent *, FILE *, const char *, ...) ;
int  _fseek_r (struct _reent *, FILE *, long, int);
int  _fseeko_r (struct _reent *, FILE *, _off_t, int);
long  _ftell_r (struct _reent *, FILE *);
_off_t  _ftello_r (struct _reent *, FILE *);
void  _rewind_r (struct _reent *, FILE *);
size_t  _fwrite_r (struct _reent *, const void * , size_t _size, size_t _n, FILE *);
int  _getc_r (struct _reent *, FILE *);
int  _getc_unlocked_r (struct _reent *, FILE *);
int  _getchar_r (struct _reent *);
int  _getchar_unlocked_r (struct _reent *);
char *  _gets_r (struct _reent *, char *);
int  _iprintf_r (struct _reent *, const char *, ...) ;
int  _iscanf_r (struct _reent *, const char *, ...) ;
FILE *  _open_memstream_r (struct _reent *, char **, size_t *);
void  _perror_r (struct _reent *, const char *);
int  _printf_r (struct _reent *, const char *, ...) ;
int  _putc_r (struct _reent *, int, FILE *);
int  _putc_unlocked_r (struct _reent *, int, FILE *);
int  _putchar_unlocked_r (struct _reent *, int);
int  _putchar_r (struct _reent *, int);
int  _puts_r (struct _reent *, const char *);
int  _remove_r (struct _reent *, const char *);
int  _rename_r (struct _reent *, const char *_old, const char *_new);
int  _scanf_r (struct _reent *, const char *, ...) ;
int  _siprintf_r (struct _reent *, char *, const char *, ...) ;
int  _siscanf_r (struct _reent *, const char *, const char *, ...) ;
int  _sniprintf_r (struct _reent *, char *, size_t, const char *, ...) ;
int  _snprintf_r (struct _reent *, char *, size_t, const char *, ...) ;
int  _sprintf_r (struct _reent *, char *, const char *, ...) ;
int  _sscanf_r (struct _reent *, const char *, const char *, ...) ;
char *  _tempnam_r (struct _reent *, const char *, const char *);
FILE *  _tmpfile_r (struct _reent *);
char *  _tmpnam_r (struct _reent *, char *);
int  _ungetc_r (struct _reent *, int, FILE *);
int  _vasiprintf_r (struct _reent *, char **, const char *, __gnuc_va_list) ;
char *  _vasniprintf_r (struct _reent*, char *, size_t *, const char *, __gnuc_va_list) ;
char *  _vasnprintf_r (struct _reent*, char *, size_t *, const char *, __gnuc_va_list) ;
int  _vasprintf_r (struct _reent *, char **, const char *, __gnuc_va_list) ;
int  _vdiprintf_r (struct _reent *, int, const char *, __gnuc_va_list) ;
int  _vdprintf_r (struct _reent *, int, const char *, __gnuc_va_list) ;
int  _vfiprintf_r (struct _reent *, FILE *, const char *, __gnuc_va_list) ;
int  _vfiscanf_r (struct _reent *, FILE *, const char *, __gnuc_va_list) ;
int  _vfprintf_r (struct _reent *, FILE *, const char *, __gnuc_va_list) ;
int  _vfscanf_r (struct _reent *, FILE *, const char *, __gnuc_va_list) ;
int  _viprintf_r (struct _reent *, const char *, __gnuc_va_list) ;
int  _viscanf_r (struct _reent *, const char *, __gnuc_va_list) ;
int  _vprintf_r (struct _reent *, const char *, __gnuc_va_list) ;
int  _vscanf_r (struct _reent *, const char *, __gnuc_va_list) ;
int  _vsiprintf_r (struct _reent *, char *, const char *, __gnuc_va_list) ;
int  _vsiscanf_r (struct _reent *, const char *, const char *, __gnuc_va_list) ;
int  _vsniprintf_r (struct _reent *, char *, size_t, const char *, __gnuc_va_list) ;
int  _vsnprintf_r (struct _reent *, char *, size_t, const char *, __gnuc_va_list) ;
int  _vsprintf_r (struct _reent *, char *, const char *, __gnuc_va_list) ;
int  _vsscanf_r (struct _reent *, const char *, const char *, __gnuc_va_list) ;
int  fpurge (FILE *);
ssize_t  __getdelim (char **, size_t *, int, FILE *);
ssize_t  __getline (char **, size_t *, FILE *);
int  __srget_r (struct _reent *, FILE *);
int  __swbuf_r (struct _reent *, int, FILE *);
FILE * funopen (const void * __cookie, int (*__readfn)(void * __c, char *__buf, int __n), int (*__writefn)(void * __c, const char *__buf, int __n), _fpos64_t (*__seekfn)(void * __c, _fpos64_t __off, int __whence), int (*__closefn)(void * __c));
FILE * _funopen_r (struct _reent *, const void * __cookie, int (*__readfn)(void * __c, char *__buf, int __n), int (*__writefn)(void * __c, const char *__buf, int __n), _fpos64_t (*__seekfn)(void * __c, _fpos64_t __off, int __whence), int (*__closefn)(void * __c));
typedef ssize_t cookie_read_function_t(void *__cookie, char *__buf, size_t __n);
typedef ssize_t cookie_write_function_t(void *__cookie, const char *__buf,
     size_t __n);
typedef int cookie_seek_function_t(void *__cookie, _off64_t *__off,
       int __whence);
typedef int cookie_close_function_t(void *__cookie);
typedef struct
{
  cookie_read_function_t *read;
  cookie_write_function_t *write;
  cookie_seek_function_t *seek;
  cookie_close_function_t *close;
} cookie_io_functions_t;
FILE * fopencookie (void *__cookie, const char *__mode, cookie_io_functions_t __functions);
FILE * _fopencookie_r (struct _reent *, void *__cookie, const char *__mode, cookie_io_functions_t __functions);
/*extern  int __sgetc_r(struct _reent *__ptr, FILE *__p);
extern  int __sgetc_r(struct _reent *__ptr, FILE *__p)
  {
    int __c = (--(__p)->_r < 0 ? __srget_r(__ptr, __p) : (int)(*(__p)->_p++));
    if ((__p->_flags & 0x4000) && (__c == '\r'))
      {
      int __c2 = (--(__p)->_r < 0 ? __srget_r(__ptr, __p) : (int)(*(__p)->_p++));
      if (__c2 == '\n')
        __c = __c2;
      else
        ungetc(__c2, __p);
      }
    return __c;
  }*/
struct STR_TRACE
{
  char begin_date[20];
  int trace_type;
  int value[10];
  char end_date[20];
};
typedef struct STR_TRACE tpl_str_trace;
extern tpl_str_trace trace;
extern char DATE[20];
extern FILE* TRACE_FILE_PT;
void tpl_trace_get_date();
void tpl_trace_event_begin(
    int trace_id);
void tpl_trace_value(
  int value);
void tpl_trace_event_end(void);
void tpl_trace_format_txt(void);
void tpl_trace_format_xml(void);
void tpl_trace_format_bin(void);
void *  memchr (const void *, int, size_t);
int  memcmp (const void *, const void *, size_t);
void *  memcpy (void *, const void *, size_t);
void *  memmove (void *, const void *, size_t);
void *  memset (void *, int, size_t);
char * strcat (char *, const char *);
char * strchr (const char *, int);
int  strcmp (const char *, const char *);
int  strcoll (const char *, const char *);
char * strcpy (char *, const char *);
size_t  strcspn (const char *, const char *);
char * strerror (int);
size_t  strlen (const char *);
char * strncat (char *, const char *, size_t);
int  strncmp (const char *, const char *, size_t);
char * strncpy (char *, const char *, size_t);
char * strpbrk (const char *, const char *);
char * strrchr (const char *, int);
size_t  strspn (const char *, const char *);
char * strstr (const char *, const char *);
char * strtok (char *, const char *);
size_t  strxfrm (char *, const char *, size_t);
char * strtok_r (char *, const char *, char **);
int  bcmp (const void *, const void *, size_t);
void  bcopy (const void *, void *, size_t);
void  bzero (void *, size_t);
int  ffs (int);
char * index (const char *, int);
void *  memccpy (void *, const void *, int, size_t);
void *  mempcpy (void *, const void *, size_t);
void *  memmem (const void *, size_t, const void *, size_t);
char * rindex (const char *, int);
char * stpcpy (char *, const char *);
char * stpncpy (char *, const char *, size_t);
int  strcasecmp (const char *, const char *);
char * strcasestr (const char *, const char *);
char * strchrnul (const char *, int);
char * strdup (const char *);
char * _strdup_r (struct _reent *, const char *);
char * strndup (const char *, size_t);
char * _strndup_r (struct _reent *, const char *, size_t);
int  strerror_r (int, char *, size_t) ;
size_t  strlcat (char *, const char *, size_t);
size_t  strlcpy (char *, const char *, size_t);
int  strncasecmp (const char *, const char *, size_t);
size_t  strnlen (const char *, size_t);
char * strsep (char **, const char *);
char * strlwr (char *);
char * strupr (char *);
char * strsignal (int __signo);
int  strtosigno (const char *__name);
char *mkdtemp (char *);
const char *getprogname (void);
void setprogname (const char *);
char *realpath (const char *, char *);
char *canonicalize_file_name (const char *);
int unsetenv (const char *);
char *initstate (unsigned seed, char *state, size_t size);
long random (void);
char *setstate (const char *state);
void srandom (unsigned);
char *ptsname (int);
int grantpt (int);
int unlockpt (int);
int posix_openpt (int);
int posix_memalign (void **, size_t, size_t);
extern void * memalign (size_t, size_t);
extern void * valloc (size_t);
typedef struct
{
  int quot;
  int rem;
} div_t;
typedef struct
{
  long quot;
  long rem;
} ldiv_t;
typedef struct
{
  long long int quot;
  long long int rem;
} lldiv_t;
int  __locale_mb_cur_max (void);
void  abort (void) ;
int  abs (int);
int  atexit (void (*__func)(void));
double  atof (const char *__nptr);
float  atoff (const char *__nptr);
int  atoi (const char *__nptr);
int  _atoi_r (struct _reent *, const char *__nptr);
long  atol (const char *__nptr);
long  _atol_r (struct _reent *, const char *__nptr);
void *  bsearch (const void * __key, const void * __base, size_t __nmemb, size_t __size, int ( * _compar) (const void *, const void *));
void *  calloc (size_t __nmemb, size_t __size) ;
div_t  div (int __numer, int __denom);
void  exit (int __status) ;
void  free (void *) ;
char *  getenv (const char *__string);
char *  _getenv_r (struct _reent *, const char *__string);
char *  _findenv (const char *, int *);
char *  _findenv_r (struct _reent *, const char *, int *);
extern char *suboptarg;
int  getsubopt (char **, char * const *, char **);
long  labs (long);
ldiv_t  ldiv (long __numer, long __denom);
void *  malloc (size_t __size) ;
int  mblen (const char *, size_t);
int  _mblen_r (struct _reent *, const char *, size_t, _mbstate_t *);
int  mbtowc (wchar_t *, const char *, size_t);
int  _mbtowc_r (struct _reent *, wchar_t *, const char *, size_t, _mbstate_t *);
int  wctomb (char *, wchar_t);
int  _wctomb_r (struct _reent *, char *, wchar_t, _mbstate_t *);
size_t  mbstowcs (wchar_t *, const char *, size_t);
size_t  _mbstowcs_r (struct _reent *, wchar_t *, const char *, size_t, _mbstate_t *);
size_t  wcstombs (char *, const wchar_t *, size_t);
size_t  _wcstombs_r (struct _reent *, char *, const wchar_t *, size_t, _mbstate_t *);
char *  mkdtemp (char *);
int  mkostemp (char *, int);
int  mkostemps (char *, int, int);
int  mkstemp (char *);
int  mkstemps (char *, int);
char *  mktemp (char *) ;
char *  _mkdtemp_r (struct _reent *, char *);
int  _mkostemp_r (struct _reent *, char *, int);
int  _mkostemps_r (struct _reent *, char *, int, int);
int  _mkstemp_r (struct _reent *, char *);
int  _mkstemps_r (struct _reent *, char *, int);
char *  _mktemp_r (struct _reent *, char *) ;
void  qsort (void * __base, size_t __nmemb, size_t __size, int(*_compar)(const void *, const void *));
int  rand (void);
void *  realloc (void * __r, size_t __size) ;
void *  reallocf (void * __r, size_t __size);
void  srand (unsigned __seed);
double  strtod (const char *__n, char **__end_PTR);
double  _strtod_r (struct _reent *,const char *__n, char **__end_PTR);
float  strtof (const char *__n, char **__end_PTR);
long  strtol (const char *__n, char **__end_PTR, int __base);
long  _strtol_r (struct _reent *,const char *__n, char **__end_PTR, int __base);
unsigned long  strtoul (const char *__n, char **__end_PTR, int __base);
unsigned long  _strtoul_r (struct _reent *,const char *__n, char **__end_PTR, int __base);
int  system (const char *__string);
long  a64l (const char *__input);
char *  l64a (long __input);
char *  _l64a_r (struct _reent *,long __input);
int  on_exit (void (*__func)(int, void *),void * __arg);
void  _Exit (int __status) ;
int  putenv (char *__string);
int  _putenv_r (struct _reent *, char *__string);
void *  _reallocf_r (struct _reent *, void *, size_t);
int  setenv (const char *__string, const char *__value, int __overwrite);
int  _setenv_r (struct _reent *, const char *__string, const char *__value, int __overwrite);
char *  gcvt (double,int,char *);
char *  gcvtf (float,int,char *);
char *  fcvt (double,int,int *,int *);
char *  fcvtf (float,int,int *,int *);
char *  ecvt (double,int,int *,int *);
char *  ecvtbuf (double, int, int*, int*, char *);
char *  fcvtbuf (double, int, int*, int*, char *);
char *  ecvtf (float,int,int *,int *);
char *  dtoa (double, int, int, int *, int*, char**);
int  rand_r (unsigned *__seed);
double  drand48 (void);
double  _drand48_r (struct _reent *);
double  erand48 (unsigned short [3]);
double  _erand48_r (struct _reent *, unsigned short [3]);
long  jrand48 (unsigned short [3]);
long  _jrand48_r (struct _reent *, unsigned short [3]);
void  lcong48 (unsigned short [7]);
void  _lcong48_r (struct _reent *, unsigned short [7]);
long  lrand48 (void);
long  _lrand48_r (struct _reent *);
long  mrand48 (void);
long  _mrand48_r (struct _reent *);
long  nrand48 (unsigned short [3]);
long  _nrand48_r (struct _reent *, unsigned short [3]);
unsigned short *
        seed48 (unsigned short [3]);
unsigned short *
        _seed48_r (struct _reent *, unsigned short [3]);
void  srand48 (long);
void  _srand48_r (struct _reent *, long);
long long  atoll (const char *__nptr);
long long  _atoll_r (struct _reent *, const char *__nptr);
long long  llabs (long long);
lldiv_t  lldiv (long long __numer, long long __denom);
long long  strtoll (const char *__n, char **__end_PTR, int __base);
long long  _strtoll_r (struct _reent *, const char *__n, char **__end_PTR, int __base);
unsigned long long  strtoull (const char *__n, char **__end_PTR, int __base);
unsigned long long  _strtoull_r (struct _reent *, const char *__n, char **__end_PTR, int __base);
char *  _dtoa_r (struct _reent *, double, int, int, int *, int*, char**);
int  _system_r (struct _reent *, const char *);
void  __eprintf (const char *, const char *, unsigned int, const char *);
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
tpl_str_trace trace;
char DATE[20];
FILE* TRACE_FILE_PT;
void tpl_trace_get_date()
{
  struct timeval result;
  int sec;
  int usec;
  gettimeofday(&result,((void *)0));
  sec = result.tv_sec;
  usec = result.tv_usec;
  sprintf(DATE,"%d%06d",sec,usec);
}
void tpl_trace_event_begin(
    int trace_id)
{
 strncpy(trace.begin_date, DATE, sizeof(DATE));
 trace.trace_type = trace_id;
 trace.value[0] = -1;
}
void tpl_trace_value(
  int value)
{
  int i = 0;
  while ( trace.value[i] != -1 )
  {
    i++;
  }
  trace.value[i] = value;
  trace.value[i+1] = -1;
}
void tpl_trace_event_end(void)
{
  tpl_trace_get_date();
  strncpy(trace.end_date, DATE, sizeof(DATE));
}
void tpl_trace_format_txt(void)
{
  int i = 0;
  if(TRACE_FILE_PT == ((void *)0))
  {
    //TRACE_FILE_PT = fopen(TRACE_FILE,"w");
  }
  fprintf(TRACE_FILE_PT,"%s,%d",trace.begin_date,trace.trace_type);
  while ( trace.value[i] != -1 )
  {
    fprintf(TRACE_FILE_PT,",%d",trace.value[i]);
    i++;
  }
  fprintf(TRACE_FILE_PT,"\n");
}
void tpl_trace_format_xml(void)
{
  int i = 0;
  if(TRACE_FILE_PT == ((void *)0))
  {
    //TRACE_FILE_PT = fopen(TRACE_FILE,"w");
    fprintf(TRACE_FILE_PT,
            "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n"
            "<!DOCTYPE trace\n"
            "[\n"
            "<!ELEMENT trace (record*)>\n"
            "<!ELEMENT record (values)>\n"
            "<!ELEMENT values (value*)>\n"
            "<!ELEMENT value (#PCDATA)>\n"
            "]>\n"
            "<trace>\n");
  }
  fprintf(TRACE_FILE_PT,
          "<record date=\"%s\" type=\"%d\">\n"
          "<values>\n",
          trace.begin_date,trace.trace_type);
  while ( trace.value[i] != -1 )
  {
    fprintf(TRACE_FILE_PT,"<value>%d</value>\n",trace.value[i]);
    i++;
  }
  fprintf(TRACE_FILE_PT,"</values>\n</record>\n");
  if(trace.trace_type == 19)
  {
    fprintf(TRACE_FILE_PT,"</trace>\n");
  }
}
void tpl_trace_format_bin(void)
{
  int i = 0;
  long date;
  if (TRACE_FILE_PT == ((void *)0))
  {
    //TRACE_FILE_PT = fopen(TRACE_FILE,"wb");
  }
  date = atol(trace.begin_date);
  fwrite(&date,1,sizeof(date),TRACE_FILE_PT);
  fwrite(&trace.trace_type,1,sizeof(trace.trace_type),TRACE_FILE_PT);
  while ( trace.value[i] != -1 )
  {
    fwrite(&trace.value[i],1,sizeof(trace.value[i]),TRACE_FILE_PT);
    i++;
  }
}