#include <assert.h>

extern void Func_AcquisitionTask_1();
extern void Func_AcquisitionTask_2();
extern void Func_AcquisitionTask_3();
extern void Func_AcquisitionTask_4();
extern void Func_AcquisitionTask_5();
extern void Func_AcquisitionTask_6();
extern void Func_AcquisitionTask_7();
extern void Func_AcquisitionTask_8();
extern void Func_AcquisitionTask_9();
extern void Func_AcquisitionTask_10();
extern void Func_AcquisitionTask_11();

extern void Func_ControlTask_15();
extern void Func_ControlTask_16();
extern void Func_ControlTask_17();
extern void Func_ControlTask_18();
extern void Func_ControlTask_19();
extern void Func_ControlTask_20();
extern void Func_ControlTask_21();
extern void Func_ControlTask_22();
extern void Func_ControlTask_23();
extern void Func_ControlTask_24();
extern void Func_ControlTask_25();
extern void Func_ControlTask_26();
extern void Func_ControlTask_27();
extern void Func_ControlTask_28();
extern void Func_ControlTask_29();

#define jump_AcquisitionTask() {\
	switch(current_pc[AcquisitionTask]) {\
		case 1: Func_AcquisitionTask_1(); break;\
		case 2: Func_AcquisitionTask_2(); break;\
		case 3: Func_AcquisitionTask_3(); break;\
		case 4: Func_AcquisitionTask_4(); break;\
		case 5: Func_AcquisitionTask_5(); break;\
		case 6: Func_AcquisitionTask_6(); break;\
		case 7: Func_AcquisitionTask_7(); break;\
		case 8: Func_AcquisitionTask_8(); break;\
		case 9: Func_AcquisitionTask_9(); break;\
		case 10: Func_AcquisitionTask_10(); break;\
		case 11: Func_AcquisitionTask_11(); break;\
		default: assert(999!=999); return;\
	}\
}
#define jump_ControlTask() {\
	switch(current_pc[ControlTask]) {\
		case 15: Func_ControlTask_15(); break;\
		case 16: Func_ControlTask_16(); break;\
		case 17: Func_ControlTask_17(); break;\
		case 18: Func_ControlTask_18(); break;\
		case 19: Func_ControlTask_19(); break;\
		case 20: Func_ControlTask_20(); break;\
		case 21: Func_ControlTask_21(); break;\
		case 22: Func_ControlTask_22(); break;\
		case 23: Func_ControlTask_23(); break;\
		case 24: Func_ControlTask_24(); break;\
		case 25: Func_ControlTask_25(); break;\
		case 26: Func_ControlTask_26(); break;\
		case 27: Func_ControlTask_27(); break;\
		case 28: Func_ControlTask_28(); break;\
		case 29: Func_ControlTask_29(); break;\
		default: assert(999!=999); return;\
	}\
}
// #define jump_DisplayTask() {\
// 	switch(current_pc[DisplayTask]) {\
// 		case 29: goto L_29; break;\
// 		case 30: goto L_30; break;\
// 		default: assert(999!=999); return;\
// 	}\
// }

#include "osek.h"