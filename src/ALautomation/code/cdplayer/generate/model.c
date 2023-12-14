#define TASK (n) void TASK_##n
#define FUNC (n,m) k n FUNC_##k
#define IN_DiscPresent ((uint8_T)1U)
#define IN_Ejecting ((uint8_T)2U)
#define IN_Empty ((uint8_T)3U)
#define IN_FF ((uint8_T)1U)
#define IN_Inserting ((uint8_T)4U)
#define IN_NO_ACTIVE_CHILD ((uint8_T)0U)
#define IN_PLAY ((uint8_T)2U)
#define IN_REW ((uint8_T)3U)
#define IN_STOP ((uint8_T)4U)
#define IN_AMMode ((uint8_T)1U)
#define IN_CDMode ((uint8_T)2U)
#define IN_Eject ((uint8_T)1U)
#define IN_FMMode ((uint8_T)3U)
#define IN_FastForward ((uint8_T)1U)
#define IN_ModeManager ((uint8_T)2U)
#define IN_Normal ((uint8_T)2U)
#define IN_ON ((uint8_T)1U)
#define IN_Play ((uint8_T)1U)
#define IN_Rew ((uint8_T)3U)
#define IN_Standby ((uint8_T)2U)
#define IN_Stop ((uint8_T)2U)
#define MAX_uint32_T ((uint32_T)(0xFFFFFFFFU))
#define false (0U)
#define true (1U)

#include <stdio.h>
#include "rtwtypes.h"
#include "model.h"

INPUT rt_input;
STATE rt_state;
OUTPUT rt_output;

RadioRequestMode RadioReq_start;
uint8_T is_active_c1_model = 0;
uint8_T is_c1_model = 0;
uint8_T is_ON;
uint8_T was_ON;
uint8_T is_CDMode;
uint8_T is_Play;
uint8_T is_active_c4_model = 0;
uint8_T is_c4_model = 0;
uint8_T is_DiscPresent;
CdRequestMode CdReq = EMPTY;
boolean_T DiscPresent;
boolean_T DiscInsert;
RadioRequestMode CurrentRadioMode = OFF;
CdRequestMode MechCmd = EMPTY;
CdRequestMode CdStatus = EMPTY;
boolean_T outDiscPresent;

char * RadioReq_str[] = {"OFF","CD","FM","AM"};

void exit_internal_ON() {
	if(is_ON == IN_CDMode){
		is_Play = IN_NO_ACTIVE_CHILD;
		is_CDMode = IN_NO_ACTIVE_CHILD;
		MechCmd = STOP;
		is_ON = IN_NO_ACTIVE_CHILD;
	}
	else{
		is_ON = IN_NO_ACTIVE_CHILD;
	}
}

void ModeManager(const RadioRequestMode * RadioReq_prev) {
	if(rt_input.DiscEject != 0.0){
		if(rt_state.is_ModeManager == IN_ON){
			exit_internal_ON();
			rt_state.is_ModeManager = IN_NO_ACTIVE_CHILD;
		}
		else{
			rt_state.is_ModeManager = IN_NO_ACTIVE_CHILD;
		}
		is_c1_model = IN_Eject;
		MechCmd = EJECT;
	}
	else if(rt_state.is_ModeManager == IN_ON){
		if(rt_input.RadioReq == OFF){
			exit_internal_ON();
			rt_state.is_ModeManager = IN_Standby;
			rt_state.was_ModeManager = IN_Standby;
			CurrentRadioMode = OFF;
			MechCmd = STOP;
		}
		else if(*RadioReq_prev != RadioReq_start){
			if(rt_input.RadioReq == CD){
				exit_internal_ON();
				is_ON = IN_CDMode;
				was_ON = IN_CDMode;
				CurrentRadioMode = CD;
				MechCmd = STOP;
				if(DiscPresent){
					is_CDMode = IN_Play;
					is_Play = IN_Normal;
					MechCmd = PLAY;
				}
				else{
					is_CDMode = IN_Stop;
					MechCmd = STOP;
				}
			}
			else if(rt_input.RadioReq == AM){
				exit_internal_ON();
				is_ON = IN_AMMode;
				was_ON = IN_AMMode;
				CurrentRadioMode = AM;
				MechCmd = STOP;
			}
			else{
				exit_internal_ON();
				is_ON = IN_FMMode;
				was_ON = IN_FMMode;
				CurrentRadioMode = FM;
				MechCmd = STOP;
			}
		}
		switch(is_ON){
			case IN_AMMode :
				else{
					CurrentRadioMode = AM;
					break;
				}
			case IN_CDMode :
				CurrentRadioMode = CD;
				if(is_CDMode == IN_Play){
					if(CdReq == STOP){
						is_Play = IN_NO_ACTIVE_CHILD;
						is_CDMode = IN_Stop;
						MechCmd = STOP;
					}
					else if((CdReq == FF) && (is_Play != IN_FastForward)){
						is_Play = IN_FastForward;
						MechCmd = FF;
					}
					else if((CdReq == REW) && (is_Play != IN_Rew)){
						is_Play = IN_Rew;
						MechCmd = REW;
					}
					else if((CdReq == PLAY) && (is_Play != IN_Normal)){
						is_Play = IN_Normal;
						MechCmd = PLAY;
					}
				}
				else if(DiscPresent && (CdReq == PLAY)){
					is_CDMode = IN_Play;
					is_Play = IN_Normal;
					MechCmd = PLAY;
				}
				break;
			default :
				CurrentRadioMode = FM;
				break;
		}
	}
	else{
		CurrentRadioMode = OFF;
		if(rt_input.RadioReq != OFF){
			if(rt_input.RadioReq == CD){
				rt_state.is_ModeManager = IN_ON;
				rt_state.was_ModeManager = IN_ON;
				is_ON = IN_CDMode;
				was_ON = IN_CDMode;
				CurrentRadioMode = CD;
				MechCmd = STOP;
				if(DiscPresent){
					is_CDMode = IN_Play;
					is_Play = IN_Normal;
					MechCmd = PLAY;
				}
				else{
					is_CDMode = IN_Stop;
					MechCmd = STOP;
				}
			}
			else if(rt_input.RadioReq == AM){
				rt_state.is_ModeManager = IN_ON;
				rt_state.was_ModeManager = IN_ON;
				is_ON = IN_AMMode;
				was_ON = IN_AMMode;
				CurrentRadioMode = AM;
				MechCmd = STOP;
			}
			else{
				rt_state.is_ModeManager = IN_ON;
				rt_state.was_ModeManager = IN_ON;
				is_ON = IN_FMMode;
				was_ON = IN_FMMode;
				CurrentRadioMode = FM;
				MechCmd = STOP;
			}
		}
	}
}

void enter_internal_ModeManager() {
	switch(rt_state.was_ModeManager){
		case IN_ON :
			rt_state.is_ModeManager = IN_ON;
			rt_state.was_ModeManager = IN_ON;
			switch(was_ON){
				case IN_AMMode :
					is_ON = IN_AMMode;
					was_ON = IN_AMMode;
					CurrentRadioMode = AM;
					MechCmd = STOP;
					break;
				case IN_CDMode :
					is_ON = IN_CDMode;
					was_ON = IN_CDMode;
					CurrentRadioMode = CD;
					MechCmd = STOP;
					if(DiscPresent){
						is_CDMode = IN_Play;
						is_Play = IN_Normal;
						MechCmd = PLAY;
					}
					else{
						is_CDMode = IN_Stop;
						MechCmd = STOP;
					}
					break;
				case IN_FMMode :
					is_ON = IN_FMMode;
					was_ON = IN_FMMode;
					CurrentRadioMode = FM;
					MechCmd = STOP;
					break;
				default :
					if(rt_input.RadioReq == CD){
						is_ON = IN_CDMode;
						was_ON = IN_CDMode;
						CurrentRadioMode = CD;
						MechCmd = STOP;
						if(DiscPresent){
							is_CDMode = IN_Play;
							is_Play = IN_Normal;
							MechCmd = PLAY;
						}
						else{
							is_CDMode = IN_Stop;
							MechCmd = STOP;
						}
					}
					else if(rt_input.RadioReq == AM){
						is_ON = IN_AMMode;
						was_ON = IN_AMMode;
						CurrentRadioMode = AM;
						MechCmd = STOP;
					}
					else{
						is_ON = IN_FMMode;
						was_ON = IN_FMMode;
						CurrentRadioMode = FM;
						MechCmd = STOP;
					}
					break;
			}
			break;
		case IN_Standby :
			rt_state.is_ModeManager = IN_Standby;
			rt_state.was_ModeManager = IN_Standby;
			CurrentRadioMode = OFF;
			MechCmd = STOP;
			break;
		default :
			rt_state.is_ModeManager = IN_Standby;
			rt_state.was_ModeManager = IN_Standby;
			CurrentRadioMode = OFF;
			MechCmd = STOP;
			break;
	}
}

void model_step() {
	printf("%d %d %d %s %d\n", rt_state.is_ModeManager, rt_state.was_ModeManager, rt_state.temporalCounter_i1, RadioReq_str[rt_input.RadioReq], rt_input.DiscEject);
	RadioRequestMode RadioReq_prev;
	RadioReq_prev = RadioReq_start;
	RadioReq_start = rt_input.RadioReq;
	if(is_active_c1_model == 0U){
		is_active_c1_model = 1U;
		is_c1_model = IN_ModeManager;
		enter_internal_ModeManager();
	}
	else if(is_c1_model == IN_Eject){
		is_c1_model = IN_ModeManager;
		enter_internal_ModeManager();
	}
	else{
		ModeManager(&RadioReq_prev);
	}
	if(rt_state.temporalCounter_i1 < MAX_uint32_T){
		rt_state.temporalCounter_i1++;
	}
	if(is_active_c4_model == 0U){
		is_active_c4_model = 1U;
		is_c4_model = IN_Empty;
		CdStatus = EMPTY;
	}
	switch(is_c4_model){
		case IN_DiscPresent :
			else{
				if(MechCmd == EJECT){
					is_DiscPresent = IN_NO_ACTIVE_CHILD;
					outDiscPresent = false;
					is_c4_model = IN_Ejecting;
					rt_state.temporalCounter_i1 = 0U;
					CdStatus = EJECT;
				}
				else if(MechCmd == STOP){
					is_DiscPresent = IN_STOP;
					CdStatus = STOP;
				}
				else if(MechCmd == PLAY){
					is_DiscPresent = IN_PLAY;
					CdStatus = PLAY;
				}
				else if(MechCmd == REW){
					is_DiscPresent = IN_REW;
					CdStatus = REW;
				}
				else if(MechCmd == FF){
					is_DiscPresent = IN_FF;
					CdStatus = FF;
				}
				switch(is_DiscPresent){
					case IN_FF :
						else{
							CdStatus = FF;
							break;
						}
					case IN_PLAY :
						CdStatus = PLAY;
						break;
					case IN_REW :
						CdStatus = REW;
						break;
					default :
						CdStatus = STOP;
						break;
				}
				break;
			}
		case IN_Ejecting :
			CdStatus = EJECT;
			if(rt_state.temporalCounter_i1 >= 100U){
				is_c4_model = IN_Empty;
				CdStatus = EMPTY;
			}
			break;
		case IN_Empty :
			CdStatus = EMPTY;
			if(DiscInsert){
				is_c4_model = IN_Inserting;
				rt_state.temporalCounter_i1 = 0U;
				CdStatus = DISCINSERT;
			}
			break;
		default :
			CdStatus = DISCINSERT;
			if(rt_state.temporalCounter_i1 >= 100U){
				is_c4_model = IN_DiscPresent;
				outDiscPresent = true;
				is_DiscPresent = IN_STOP;
				CdStatus = STOP;
			}
			break;
	}
}

void model_initialize() {
	rt_state.temporalCounter_i1 = 0;
	RadioReq_start = 0;
	is_active_c1_model = 0;
	is_c1_model = 0;
	rt_state.is_ModeManager = 0;
	rt_state.was_ModeManager = 0;
	is_ON = 0;
	was_ON = 0;
	is_CDMode = 0;
	is_Play = 0;
	is_active_c4_model = 0;
	is_c4_model = 0;
	is_DiscPresent = 0;
	rt_input.RadioReq = 0;
	CdReq = EMPTY;
	rt_input.DiscEject = 0;
	DiscPresent = 0;
	DiscInsert = 0;
	CurrentRadioMode = OFF;
	MechCmd = EMPTY;
	CdStatus = EMPTY;
	outDiscPresent = 0;
}


