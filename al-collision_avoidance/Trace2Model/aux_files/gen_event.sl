(set-logic LIA)
(declare-datatypes ((rt_state.motor_speed_status_t 0))
	(((rt_state.motor_speed_statusSlow_speed) (rt_state.motor_speed_statusStop) )))
(synth-fun next ((rt_state.motor_speed_status rt_state.motor_speed_status_t)(rt_state.motor_speed_0 Int) (rt_state.motor_speed_1 Int) (rt_input.obsDistance_0 Int) (rt_input.obsDistance_1 Int) (rt_input.obsDistance_2 Int) (rt_input.obsDistance_3 Int) ) Int
	((Start Int)  (Var Int) (EnumVar0 rt_state.motor_speed_status_t) (StartBool Bool))
	((Start Int (
				 0
				 1
				 (ite StartBool Start Start)))

	(Var Int (
				 1
				 2
				 30
				 (- 10)
				 4
				 25
				 0
				 12
				 8
				 11
				 13
				 7
				 18
			 rt_state.motor_speed_0
			 rt_state.motor_speed_1
			 rt_input.obsDistance_0
			 rt_input.obsDistance_1
			 rt_input.obsDistance_2
			 rt_input.obsDistance_3
				(abs Var)						
			 	(+ Var Var)						
			 	(- Var Var)						
			 	(* Var Var)))

(EnumVar0 rt_state.motor_speed_status_t (
	rt_state.motor_speed_status
rt_state.motor_speed_statusSlow_speed
rt_state.motor_speed_statusStop
))

	 (StartBool Bool (
	 ( = EnumVar0 EnumVar0)
					 (> Var Var)						
					 (>= Var Var)						
					 (< Var Var)						
					 (<= Var Var)						
					 (= Var Var)						
					 (and StartBool StartBool)			
					 (or  StartBool StartBool)				
					 (not StartBool)))))

(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 14 11 16 14 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 21 18 24 13 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 6 16 24 1 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 16 2 0 14 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 20 20 10 14 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 4 9 16 6 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 18 6 25 16 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 15 1 9 11 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 14 1 19 9 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 19 25 25 8 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 13 12 16 22 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 9 23 10 3 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 24 11 4 18 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 8 3 2 1 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 19 4 14 20 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 2 6 24 21 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 17 3 7 18 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 8 12 6 22 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 25 14 3 7 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 5 13 23 9 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 16 3 7 8 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 22 18 17 6 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 22 15 18 19 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 16 16 18 5 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 11 6 0 8 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 5 14 18 17 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 17 0 4 8 ) 0))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 21 19 10 7 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 17 0 19 1 ) 0))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 21 2 20 17 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 2 20 23 19 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 16 1 16 0 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 19 18 22 14 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 16 1 25 23 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 3 16 6 1 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 14 21 7 3 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 25 25 12 20 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 0 18 3 1 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 16 10 18 23 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 6 5 17 0 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 4 22 7 8 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 11 14 11 7 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 23 15 23 21 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 11 3 4 10 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 4 12 18 20 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 22 14 4 13 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 0 8 13 25 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 20 25 3 1 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 5 5 7 8 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 14 14 6 15 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 10 11 3 5 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 14 12 21 0 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 23 11 8 24 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 11 24 4 12 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 15 24 22 23 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 9 15 9 10 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 20 14 0 10 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 7 13 18 21 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 7 22 4 17 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 9 15 17 0 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 5 13 18 24 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 9 9 2 19 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 20 9 17 8 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 3 8 25 2 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 8 18 17 19 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 0 8 7 17 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 4 24 8 18 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 12 23 20 18 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 9 8 11 17 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 12 14 6 20 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 22 25 9 5 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 21 10 5 25 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 10 6 8 9 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 21 7 17 25 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 21 19 18 14 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 0 7 12 24 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 11 4 3 14 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 12 4 20 7 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 9 16 6 1 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 1 22 15 11 ) 1))
(constraint (= (next rt_state.motor_speed_statusSlow_speed 30 (- 10) 4 11 18 4 ) 1))

(check-synth)
