(set-logic LIA)
(declare-datatypes ((rtU.TLM_MODE_IN_On_Start_t 0))
	(((rtU.TLM_MODE_IN_On_Start0) (rtU.TLM_MODE_IN_On_Start1) (rtU.TLM_MODE_IN_On_Start128) (rtU.TLM_MODE_IN_On_Start16) (rtU.TLM_MODE_IN_On_Start64) (rtU.TLM_MODE_IN_On_Start8))))
(declare-datatypes ((rtU.OP_CMD_IN_Disable_Audio_t 0))
	(((rtU.OP_CMD_IN_Disable_Audio0) (rtU.OP_CMD_IN_Disable_Audio1) (rtU.OP_CMD_IN_Disable_Audio128) (rtU.OP_CMD_IN_Disable_Audio130) (rtU.OP_CMD_IN_Disable_Audio18) (rtU.OP_CMD_IN_Disable_Audio2) (rtU.OP_CMD_IN_Disable_Audio66))))

(synth-fun next ((rtY.ALARM_OUT_Audio_Notification_Command Int) (rtU.CONST_IN_Audio_Level Int) (rtU.TLM_MODE_IN_On_Start rtU.TLM_MODE_IN_On_Start_t) (rtU.OP_CMD_IN_Disable_Audio rtU.OP_CMD_IN_Disable_Audio_t) (rtU.CONST_IN_Audio_Auto_Enable_Duration Int) (rtY.ALARM_OUT_Highest_Level_Alarm Int) (rtDW.audioTimer Int)) Int

	((Start Int) (StartBool Bool) (EnumVar0 rtU.TLM_MODE_IN_On_Start_t) (EnumVar1 rtU.OP_CMD_IN_Disable_Audio_t))

	((Start Int (
		rtY.ALARM_OUT_Audio_Notification_Command
		rtU.CONST_IN_Audio_Level
		rtU.CONST_IN_Audio_Auto_Enable_Duration
		rtY.ALARM_OUT_Highest_Level_Alarm
		rtDW.audioTimer
		0
		(abs Start) 							
		(+ Start Start)						
		(- Start Start)						
		(* Start Start)
		(ite StartBool Start Start)))

	 (StartBool Bool (
	 	(= EnumVar0 EnumVar0)
	 	(= EnumVar1 EnumVar1)
		(> Start Start)						
		(>= Start Start)						
		(< Start Start)						
		(<= Start Start)						
		(= Start Start)						
		(and StartBool StartBool)			
		(or  StartBool StartBool)				
		(not StartBool)))

	(EnumVar0 rtU.TLM_MODE_IN_On_Start_t (
		rtU.TLM_MODE_IN_On_Start
		rtU.TLM_MODE_IN_On_Start0
		rtU.TLM_MODE_IN_On_Start1
		rtU.TLM_MODE_IN_On_Start128
		rtU.TLM_MODE_IN_On_Start16
		rtU.TLM_MODE_IN_On_Start64
		rtU.TLM_MODE_IN_On_Start8))

	(EnumVar1 rtU.OP_CMD_IN_Disable_Audio_t (
		rtU.OP_CMD_IN_Disable_Audio
		rtU.OP_CMD_IN_Disable_Audio0
		rtU.OP_CMD_IN_Disable_Audio1
		rtU.OP_CMD_IN_Disable_Audio128
		rtU.OP_CMD_IN_Disable_Audio130
		rtU.OP_CMD_IN_Disable_Audio18
		rtU.OP_CMD_IN_Disable_Audio2
		rtU.OP_CMD_IN_Disable_Audio66))

))

(constraint (= (next 0 0 rtU.TLM_MODE_IN_On_Start0 rtU.OP_CMD_IN_Disable_Audio1 0 4 1 ) 0))
(constraint (= (next 0 0 rtU.TLM_MODE_IN_On_Start1 rtU.OP_CMD_IN_Disable_Audio0 0 4 1 ) 0))

(check-synth)
