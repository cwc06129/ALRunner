#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

#define TASK(n) TASK_##n

#include "ee.h"


/***************************************************************************
 *
 * Kernel ( CPU 0 )
 *
 **************************************************************************/
    /* Definition of task's body */
    DeclareTask(Task1);
    DeclareTask(Task2);

    const EE_FADDR EE_hal_thread_body[EE_MAX_TASK] = {
        (EE_FADDR)EE_oo_thread_stub, 		// thread Task1
     	(EE_FADDR)EE_oo_thread_stub         //thread Task2
    };

	EE_UINT32 EE_terminate_data[EE_MAX_TASK];


    const EE_FADDR EE_terminate_real_th_body[EE_MAX_TASK] = {
        (EE_FADDR)FuncTask1, 		/* thread Task1 */
        (EE_FADDR)FuncTask2 		/* thread Task2 */

    };

    /* ready priority */
    const EE_TYPEPRIO EE_th_ready_prio[EE_MAX_TASK] = {
        0x1U, 		/* thread Task1 */
        0x1U 		/* thread Task2 */
    };

    /* dispatch priority */
    const EE_TYPEPRIO EE_th_dispatch_prio[EE_MAX_TASK] = {
        0x1U, 		/* thread Task1 */
        0x1U 		/* thread Task2 */
    };

    /* thread status */
    //#if defined(__MULTI__) || defined(__WITH_STATUS__)
        EE_TYPESTATUS EE_th_status[EE_MAX_TASK] = {
            SUSPENDED,
            SUSPENDED
        };
    //#endif

    // remaining nact: init= maximum pending activations of a Task
    // MUST BE 1 for BCC1 and ECC1
    EE_TYPENACT   EE_th_rnact[EE_MAX_TASK] =
        { 1, 1};

    /* next thread */
    EE_TID EE_th_next[EE_MAX_TASK] = {
        EE_NIL,
        EE_NIL
    };

    EE_TYPEPRIO EE_th_nact[EE_MAX_TASK] =
    { 1U, 1U };

    /* The first stacked task */
    EE_TID EE_stkfirst = EE_NIL;

    /* The first task into the ready queue */
    EE_TID EE_rq_first  = EE_NIL;

    /* system ceiling */
    EE_TYPEPRIO EE_sys_ceiling= 0x0000U;

    #ifndef __OO_NO_CHAINTASK__
        // The next task to be activated after a ChainTask. initvalue=all EE_NIL
        EE_TID EE_th_terminate_nextask[EE_MAX_TASK] = {
            EE_NIL,
            EE_NIL
        };
    #endif

//////////////////////////////////////////////////////////////////////////////
//
//   AppMode
//
/////////////////////////////////////////////////////////////////////////////
    EE_TYPEAPPMODE EE_ApplicationMode;



/***************************************************************************
 *
 * Auto Start (TASK)
 *
 **************************************************************************/
    static const EE_TID EE_oo_autostart_task_mode_OSDEFAULTAPPMODE[1] = 
        { Task2 };

    const struct EE_oo_autostart_task_type EE_oo_autostart_task_data[EE_MAX_APPMODE] = {
        { 1U, EE_oo_autostart_task_mode_OSDEFAULTAPPMODE}
    };