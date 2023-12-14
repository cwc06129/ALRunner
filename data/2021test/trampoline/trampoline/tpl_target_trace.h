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
typedef long clock_t;
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
 time_t  time (time_t*);
 double  difftime (time_t, time_t);
 time_t  mktime (struct tm*);
 char*  asctime (const struct tm*);
 char*  ctime (const time_t*);
 struct tm*  gmtime (const time_t*);
 struct tm*  localtime (const time_t*);
 size_t  strftime (char*, size_t, const char*, const struct tm*);
extern void  _tzset (void);
extern void  tzset (void);
 char*  _strdate(char*);
 char*  _strtime(char*);
extern int*  __p__daylight (void);
extern long*  __p__timezone (void);
extern char**  __p__tzname (void);
extern  int _daylight;
extern  long _timezone;
extern  char *_tzname[2];
extern  int daylight;
extern  long timezone;
extern  char *tzname[2];
 wchar_t*  _wasctime(const struct tm*);
 wchar_t*  _wctime(const time_t*);
 wchar_t*  _wstrdate(wchar_t*);
 wchar_t*  _wstrtime(wchar_t*);
 size_t  wcsftime (wchar_t*, size_t, const wchar_t*, const struct tm*);
struct timeval {
  long tv_sec;
  long tv_usec;
};
struct timezone
{
  int tz_minuteswest;
  int tz_dsttime;
};
int  gettimeofday(struct timeval *__restrict__,
    void *__restrict__ );
typedef __builtin_va_list __gnuc_va_list;
typedef struct _iobuf
{
 char* _ptr;
 int _cnt;
 char* _base;
 int _flag;
 int _file;
 int _charbuf;
 int _bufsiz;
 char* _tmpfname;
} FILE;
extern  FILE _iob[];
 FILE*  fopen (const char*, const char*);
 FILE*  freopen (const char*, const char*, FILE*);
 int  fflush (FILE*);
 int  fclose (FILE*);
 int  remove (const char*);
 int  rename (const char*, const char*);
 FILE*  tmpfile (void);
 char*  tmpnam (char*);
 char*  _tempnam (const char*, const char*);
 int  _rmtmp(void);
 int  _unlink (const char*);
 char*  tempnam (const char*, const char*);
 int  rmtmp(void);
 int  unlink (const char*);
 int  setvbuf (FILE*, char*, int, size_t);
 void  setbuf (FILE*, char*);
extern int  __mingw_fprintf(FILE*, const char*, ...);
extern int  __mingw_printf(const char*, ...);
extern int  __mingw_sprintf(char*, const char*, ...);
extern int  __mingw_snprintf(char*, size_t, const char*, ...);
extern int  __mingw_vfprintf(FILE*, const char*, __gnuc_va_list);
extern int  __mingw_vprintf(const char*, __gnuc_va_list);
extern int  __mingw_vsprintf(char*, const char*, __gnuc_va_list);
extern int  __mingw_vsnprintf(char*, size_t, const char*, __gnuc_va_list);
 int  fprintf (FILE*, const char*, ...);
 int  printf (const char*, ...);
 int  sprintf (char*, const char*, ...);
 int  vfprintf (FILE*, const char*, __gnuc_va_list);
 int  vprintf (const char*, __gnuc_va_list);
 int  vsprintf (char*, const char*, __gnuc_va_list);
 int  __msvcrt_fprintf(FILE*, const char*, ...);
 int  __msvcrt_printf(const char*, ...);
 int  __msvcrt_sprintf(char*, const char*, ...);
 int  __msvcrt_vfprintf(FILE*, const char*, __gnuc_va_list);
 int  __msvcrt_vprintf(const char*, __gnuc_va_list);
 int  __msvcrt_vsprintf(char*, const char*, __gnuc_va_list);
 int  _snprintf (char*, size_t, const char*, ...);
 int  _vsnprintf (char*, size_t, const char*, __gnuc_va_list);
 int  _vscprintf (const char*, __gnuc_va_list);
int  snprintf (char *, size_t, const char *, ...);
int  vsnprintf (char *, size_t, const char *, __gnuc_va_list);
int  vscanf (const char * __restrict__, __gnuc_va_list);
int  vfscanf (FILE * __restrict__, const char * __restrict__,
       __gnuc_va_list);
int  vsscanf (const char * __restrict__,
       const char * __restrict__, __gnuc_va_list);
 int  fscanf (FILE*, const char*, ...);
 int  scanf (const char*, ...);
 int  sscanf (const char*, const char*, ...);
 int  fgetc (FILE*);
 char*  fgets (char*, int, FILE*);
 int  fputc (int, FILE*);
 int  fputs (const char*, FILE*);
 char*  gets (char*);
 int  puts (const char*);
 int  ungetc (int, FILE*);
 int  _filbuf (FILE*);
 int  _flsbuf (int, FILE*);
extern __inline__ int  getc (FILE* __F)
{
  return (--__F->_cnt >= 0)
    ? (int) (unsigned char) *__F->_ptr++
    : _filbuf (__F);
}
extern __inline__ int  putc (int __c, FILE* __F)
{
  return (--__F->_cnt >= 0)
    ? (int) (unsigned char) (*__F->_ptr++ = (char)__c)
    : _flsbuf (__c, __F);
}
extern __inline__ int  getchar (void)
{
  return (--(&_iob[0])->_cnt >= 0)
    ? (int) (unsigned char) *(&_iob[0])->_ptr++
    : _filbuf ((&_iob[0]));
}
extern __inline__ int  putchar(int __c)
{
  return (--(&_iob[1])->_cnt >= 0)
    ? (int) (unsigned char) (*(&_iob[1])->_ptr++ = (char)__c)
    : _flsbuf (__c, (&_iob[1]));}
 size_t  fread (void*, size_t, size_t, FILE*);
 size_t  fwrite (const void*, size_t, size_t, FILE*);
 int  fseek (FILE*, long, int);
 long  ftell (FILE*);
 void  rewind (FILE*);
typedef long long fpos_t;
 int  fgetpos (FILE*, fpos_t*);
 int  fsetpos (FILE*, const fpos_t*);
 int  feof (FILE*);
 int  ferror (FILE*);
 void  clearerr (FILE*);
 //void  perror (const char*);
 FILE*  _popen (const char*, const char*);
 int  _pclose (FILE*);
 FILE*  popen (const char*, const char*);
 int  pclose (FILE*);
 int  _flushall (void);
 int  _fgetchar (void);
 int  _fputchar (int);
 FILE*  _fdopen (int, const char*);
 int  _fileno (FILE*);
 int  _fcloseall (void);
 FILE*  _fsopen (const char*, const char*, int);
 int  _getmaxstdio (void);
 int  _setmaxstdio (int);
 int  fgetchar (void);
 int  fputchar (int);
 FILE*  fdopen (int, const char*);
 int  fileno (FILE*);
extern __inline__ FILE*  fopen64 (const char* filename, const char* mode)
{
  return fopen (filename, mode);
}
int  fseeko64 (FILE*, off64_t, int);
extern __inline__ off64_t  ftello64 (FILE * stream)
{
  fpos_t pos;
  if (fgetpos(stream, &pos))
    return -1LL;
  else
   return ((off64_t) pos);
}
 int  fwprintf (FILE*, const wchar_t*, ...);
 int  wprintf (const wchar_t*, ...);
 int  _snwprintf (wchar_t*, size_t, const wchar_t*, ...);
 int  vfwprintf (FILE*, const wchar_t*, __gnuc_va_list);
 int  vwprintf (const wchar_t*, __gnuc_va_list);
 int  _vsnwprintf (wchar_t*, size_t, const wchar_t*, __gnuc_va_list);
 int  _vscwprintf (const wchar_t*, __gnuc_va_list);
 int  fwscanf (FILE*, const wchar_t*, ...);
 int  wscanf (const wchar_t*, ...);
 int  swscanf (const wchar_t*, const wchar_t*, ...);
 wint_t  fgetwc (FILE*);
 wint_t  fputwc (wchar_t, FILE*);
 wint_t  ungetwc (wchar_t, FILE*);
 int  swprintf (wchar_t*, const wchar_t*, ...);
 int  vswprintf (wchar_t*, const wchar_t*, __gnuc_va_list);
 wchar_t*  fgetws (wchar_t*, int, FILE*);
 int  fputws (const wchar_t*, FILE*);
 wint_t  getwc (FILE*);
 wint_t  getwchar (void);
 wchar_t*  _getws (wchar_t*);
 wint_t  putwc (wint_t, FILE*);
 int  _putws (const wchar_t*);
 wint_t  putwchar (wint_t);
 FILE*  _wfdopen(int, const wchar_t *);
 FILE*  _wfopen (const wchar_t*, const wchar_t*);
 FILE*  _wfreopen (const wchar_t*, const wchar_t*, FILE*);
 FILE*  _wfsopen (const wchar_t*, const wchar_t*, int);
 wchar_t*  _wtmpnam (wchar_t*);
 wchar_t*  _wtempnam (const wchar_t*, const wchar_t*);
 int  _wrename (const wchar_t*, const wchar_t*);
 int  _wremove (const wchar_t*);
 void  _wperror (const wchar_t*);
 FILE*  _wpopen (const wchar_t*, const wchar_t*);
int  snwprintf (wchar_t* s, size_t n, const wchar_t* format, ...);
int  vsnwprintf (wchar_t* s, size_t n, const wchar_t* format, __gnuc_va_list arg);
int  vwscanf (const wchar_t * __restrict__, __gnuc_va_list);
int  vfwscanf (FILE * __restrict__,
         const wchar_t * __restrict__, __gnuc_va_list);
int  vswscanf (const wchar_t * __restrict__,
         const wchar_t * __restrict__, __gnuc_va_list);
 FILE*  wpopen (const wchar_t*, const wchar_t*);
 wint_t  _fgetwchar (void);
 wint_t  _fputwchar (wint_t);
 int  _getw (FILE*);
 int  _putw (int, FILE*);
 wint_t  fgetwchar (void);
 wint_t  fputwchar (wint_t);
 int  getw (FILE*);
 int  putw (int, FILE*);
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
