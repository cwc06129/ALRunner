(set-logic LIA)
(declare-datatypes ((call_label_t 0))
	(((call_labelNone) )))
(declare-datatypes ((event_t 0))
	(((eventNONE) (eventPOWER_OFF) (eventPOWER_ON) )))
(declare-datatypes ((mode_t 0))
	(((modeOFF) (modeON) (modeREADY) )))
(synth-fun next ((call_label call_label_t)(counter Int) (event event_t)(mode mode_t)(power_switch Bool) ) Int
	((Start Int)  (Var Int) (EnumVar0 call_label_t) (EnumVar1 event_t) (EnumVar2 mode_t) (StartBool Bool))
	((Start Int (
				 0
				 1
				 (ite StartBool Start Start)))

	(Var Int (
				 1
				 2
				 0
			 counter
				(abs Var)						
			 	(+ Var Var)						
			 	(- Var Var)						
			 	(* Var Var)))

(EnumVar0 call_label_t (
	call_label
call_labelNone
))

(EnumVar1 event_t (
	event
eventNONE
eventPOWER_OFF
eventPOWER_ON
))

(EnumVar2 mode_t (
	mode
modeOFF
modeON
modeREADY
))

	 (StartBool Bool (
				 	 power_switch
	 ( = EnumVar0 EnumVar0)
	 ( = EnumVar1 EnumVar1)
	 ( = EnumVar2 EnumVar2)
					 (> Var Var)						
					 (>= Var Var)						
					 (< Var Var)						
					 (<= Var Var)						
					 (= Var Var)						
					 (and StartBool StartBool)			
					 (or  StartBool StartBool)				
					 (not StartBool)))))

(constraint (= (next call_labelNone 0 eventNONE modeON false ) 0))
(constraint (= (next call_labelNone 0 eventNONE modeOFF false ) 1))
(constraint (= (next call_labelNone 0 eventNONE modeON true ) 1))

(check-synth)
