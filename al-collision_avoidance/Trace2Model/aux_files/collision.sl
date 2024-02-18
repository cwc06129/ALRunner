(set-logic LIA)
(synth-fun next ((determineSpeed_j_1 Int) (zone_0 Int) (zone_1 Int) (zone_2 Int) (zone_3 Int) (motor_speed_0 Int) (motor_speed_1 Int) (obsDistance_0 Int) (obsDistance_1 Int) (obsDistance_2 Int) (obsDistance_3 Int) ) Int
	((Start Int)  (Var Int) (StartBool Bool))
	((Start Int (
				 0
				 1
				 (ite StartBool Start Start)))

	(Var Int (
				 1
				 2
				 4
				 70
				 139
				 0
				 55
				 48
				 165
				 200
				 140
				 170
				 22
				 94
				 254
				 56
				 148
				 83
				 179
				 255
				 74
				 158
				 67
				 201
				 133
				 44
			 determineSpeed_j_1
			 zone_0
			 zone_1
			 zone_2
			 zone_3
			 motor_speed_0
			 motor_speed_1
			 obsDistance_0
			 obsDistance_1
			 obsDistance_2
			 obsDistance_3
				(abs Var)						
			 	(+ Var Var)						
			 	(- Var Var)						
			 	(* Var Var)))

	 (StartBool Bool (
					 (> Var Var)						
					 (>= Var Var)						
					 (< Var Var)						
					 (<= Var Var)						
					 (= Var Var)						
					 (and StartBool StartBool)			
					 (or  StartBool StartBool)				
					 (not StartBool)))))

(constraint (= (next 4 2 2 2 2 0 0 166 254 255 255 ) 0))
(constraint (= (next 4 2 2 2 2 0 0 140 246 255 255 ) 0))
(constraint (= (next 4 2 2 2 2 35 35 140 246 74 209 ) 0))
(constraint (= (next 4 2 2 2 2 30 30 200 180 123 133 ) 0))
(constraint (= (next 4 2 2 2 2 0 0 153 58 255 255 ) 0))
(constraint (= (next 4 2 2 2 2 139 139 197 56 179 201 ) 1))
(constraint (= (next 4 2 2 2 2 70 70 165 94 179 201 ) 1))

(check-synth)
