



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



__extension__ typedef long long _off64_t;







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
__extension__ typedef long long int __loff_t;
typedef unsigned char u_char;
typedef unsigned short u_short;
typedef unsigned int u_int;
typedef unsigned long u_long;
typedef unsigned short ushort;
typedef unsigned int uint;
typedef unsigned long clock_t;
typedef long time_t;
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
  __extension__ union
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
  __extension__ union
  {
    __uint32_t __pad[32];
    struct _sigcommune _si_commune;
    __extension__ union
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
    __extension__ struct
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
  __extension__ union
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
