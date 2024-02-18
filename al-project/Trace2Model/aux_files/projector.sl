(set-logic LIA)
(declare-datatypes ((rt_state.event_t 0))
	(((rt_state.eventNONE) (rt_state.eventPOWER_OFF) (rt_state.eventPOWER_ON) )))
(declare-datatypes ((rt_state.mode_t 0))
	(((rt_state.modeOFF) (rt_state.modeON) (rt_state.modeREADY) )))
(synth-fun next ((rt_state.counter Int) (rt_state.event rt_state.event_t)(rt_state.mode rt_state.mode_t)(rt_state.power_switch Bool) (rt_input.power_switch Bool) ) Int
	((Start Int)  (Var Int) (EnumVar0 rt_state.event_t) (EnumVar1 rt_state.mode_t) (StartBool Bool))
	((Start Int (
				 0
				 1
				 (ite StartBool Start Start)))

	(Var Int (
				 1
				 2
				 0
			 rt_state.counter
				(abs Var)						
			 	(+ Var Var)						
			 	(- Var Var)						
			 	(* Var Var)))

(EnumVar0 rt_state.event_t (
	rt_state.event
rt_state.eventNONE
rt_state.eventPOWER_OFF
rt_state.eventPOWER_ON
))

(EnumVar1 rt_state.mode_t (
	rt_state.mode
rt_state.modeOFF
rt_state.modeON
rt_state.modeREADY
))

	 (StartBool Bool (
				 	 rt_state.power_switch
				 	 rt_input.power_switch
	 ( = EnumVar0 EnumVar0)
	 ( = EnumVar1 EnumVar1)
					 (> Var Var)						
					 (>= Var Var)						
					 (< Var Var)						
					 (<= Var Var)						
					 (= Var Var)						
					 (and StartBool StartBool)			
					 (or  StartBool StartBool)				
					 (not StartBool)))))

(constraint (= (next 2 rt_state.eventPOWER_OFF rt_state.modeREADY true true ) 1))
(constraint (= (next 0 rt_state.eventPOWER_OFF rt_state.modeREADY true true ) 0))

(check-synth)
