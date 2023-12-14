#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

#include <stddef.h>
#include <stdio.h> 
#include <stdbool.h> 
#include "model.h"
#include "model.c"
int main()
{					
	extern char* state_str[];	
	STATE state;				
	STATE prev_state;			
	INPUT input;				
	INPUT prev_input;			

	// valid range assume 
	__CPROVER_assume(prev_state.fridge_state >= 0 && prev_state.fridge_state < 5); 
	__CPROVER_assume(prev_state.temp >= 0 && prev_state.temp <= 8); 
	
	// input assume	
	__CPROVER_assume(prev_input.button >= 0 && prev_input.button < 2); 
	__CPROVER_assume(prev_input.emode >= 0 && prev_input.emode < 3); 
	__CPROVER_assume(prev_input.door >= 0 && prev_input.door < 3); 
	__CPROVER_assume(input.button >= 0 && input.button < 2); 
	__CPROVER_assume(input.emode >= 0 && input.emode < 3); 
	__CPROVER_assume(input.door >= 0 && input.door < 3); 
	
	rt_state = prev_state;		
	rt_input = prev_input;		
	model_step();				
	state = rt_state;			

	// auto generated assume 
	__CPROVER_assume((state.fridge_state == Door_Open_Mode));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==0 && input.emode==0 && input.door==0 && state.temp==8));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==0 && input.emode==0 && input.door==1 && state.temp==2));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==0 && input.emode==0 && input.door==1 && state.temp==5));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==0 && input.emode==0 && input.door==1 && state.temp==8));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==0 && input.emode==0 && input.door==2 && state.temp==8));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==0 && input.emode==1 && input.door==1 && state.temp==0));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==0 && input.emode==1 && input.door==1 && state.temp==2));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==0 && input.emode==1 && input.door==1 && state.temp==4));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==0 && input.emode==1 && input.door==1 && state.temp==5));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==0 && input.emode==1 && input.door==1 && state.temp==6));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==0 && input.emode==1 && input.door==1 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==0 && input.emode==1 && input.door==1 && state.temp==8));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==0 && input.emode==1 && input.door==2 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==0 && input.emode==2 && input.door==0 && state.temp==8));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==0 && input.emode==2 && input.door==1 && state.temp==0));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==0 && input.emode==2 && input.door==1 && state.temp==8));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==0 && input.emode==2 && input.door==2 && state.temp==8));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==1 && input.emode==0 && input.door==1 && state.temp==1));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==1 && input.emode==0 && input.door==1 && state.temp==5));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==1 && input.emode==0 && input.door==1 && state.temp==6));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==1 && input.emode==0 && input.door==1 && state.temp==8));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==1 && input.emode==1 && input.door==0 && state.temp==8));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==1 && input.emode==1 && input.door==1 && state.temp==8));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==1 && input.emode==1 && input.door==2 && state.temp==8));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==1 && input.emode==2 && input.door==1 && state.temp==1));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==1 && input.emode==2 && input.door==1 && state.temp==4));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==1 && input.emode==2 && input.door==1 && state.temp==6));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==1 && input.emode==2 && input.door==1 && state.temp==8));
	__CPROVER_assume(!(prev_state.fridge_state == 0 && state.fridge_state == 1 && input.button==1 && input.emode==2 && input.door==2 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 1 && input.button==0 && input.emode==1 && input.door==1 && state.temp==3));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 1 && input.button==0 && input.emode==1 && input.door==1 && state.temp==4));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 1 && input.button==0 && input.emode==1 && input.door==1 && state.temp==6));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 1 && input.button==1 && input.emode==0 && input.door==1 && state.temp==0));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 1 && input.button==1 && input.emode==2 && input.door==1 && state.temp==0));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 1 && input.button==1 && input.emode==2 && input.door==1 && state.temp==4));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 1 && input.button==1 && input.emode==2 && input.door==1 && state.temp==6));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==0 && input.emode==0 && input.door==2 && state.temp==0));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==0 && input.emode==0 && input.door==2 && state.temp==1));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==0 && input.emode==0 && input.door==2 && state.temp==2));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==0 && input.emode==0 && input.door==2 && state.temp==3));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==0 && input.emode==0 && input.door==2 && state.temp==4));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==0 && input.emode==0 && input.door==2 && state.temp==5));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==0 && input.emode==0 && input.door==2 && state.temp==6));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==0 && input.emode==1 && input.door==2 && state.temp==0));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==0 && input.emode==1 && input.door==2 && state.temp==1));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==0 && input.emode==1 && input.door==2 && state.temp==2));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==0 && input.emode==1 && input.door==2 && state.temp==3));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==0 && input.emode==1 && input.door==2 && state.temp==4));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==0 && input.emode==1 && input.door==2 && state.temp==5));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==0 && input.emode==1 && input.door==2 && state.temp==6));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==0 && input.emode==2 && input.door==2 && state.temp==0));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==0 && input.emode==2 && input.door==2 && state.temp==1));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==0 && input.emode==2 && input.door==2 && state.temp==2));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==0 && input.emode==2 && input.door==2 && state.temp==3));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==0 && input.emode==2 && input.door==2 && state.temp==4));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==0 && input.emode==2 && input.door==2 && state.temp==5));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==0 && input.emode==2 && input.door==2 && state.temp==6));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==1 && input.emode==0 && input.door==2 && state.temp==0));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==1 && input.emode==0 && input.door==2 && state.temp==1));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==1 && input.emode==0 && input.door==2 && state.temp==2));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==1 && input.emode==0 && input.door==2 && state.temp==3));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==1 && input.emode==0 && input.door==2 && state.temp==4));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==1 && input.emode==0 && input.door==2 && state.temp==5));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==1 && input.emode==0 && input.door==2 && state.temp==6));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==1 && input.emode==1 && input.door==2 && state.temp==0));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==1 && input.emode==1 && input.door==2 && state.temp==1));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==1 && input.emode==1 && input.door==2 && state.temp==2));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==1 && input.emode==1 && input.door==2 && state.temp==3));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==1 && input.emode==1 && input.door==2 && state.temp==4));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==1 && input.emode==1 && input.door==2 && state.temp==5));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==1 && input.emode==1 && input.door==2 && state.temp==6));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==1 && input.emode==2 && input.door==2 && state.temp==0));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==1 && input.emode==2 && input.door==2 && state.temp==1));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==1 && input.emode==2 && input.door==2 && state.temp==2));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==1 && input.emode==2 && input.door==2 && state.temp==3));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==1 && input.emode==2 && input.door==2 && state.temp==4));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==1 && input.emode==2 && input.door==2 && state.temp==5));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 2 && input.button==1 && input.emode==2 && input.door==2 && state.temp==6));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 3 && input.button==0 && input.emode==0 && input.door==0 && state.temp==1));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 3 && input.button==1 && input.emode==1 && input.door==0 && state.temp==1));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 3 && input.button==1 && input.emode==2 && input.door==1 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 1 && state.fridge_state == 3 && input.button==1 && input.emode==2 && input.door==2 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 1 && input.button==0 && input.emode==1 && input.door==0 && state.temp==4));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 1 && input.button==0 && input.emode==1 && input.door==1 && state.temp==1));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 1 && input.button==0 && input.emode==1 && input.door==1 && state.temp==2));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 1 && input.button==0 && input.emode==1 && input.door==1 && state.temp==3));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 1 && input.button==0 && input.emode==1 && input.door==1 && state.temp==4));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 1 && input.button==0 && input.emode==1 && input.door==1 && state.temp==5));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 1 && input.button==0 && input.emode==1 && input.door==1 && state.temp==6));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 1 && input.button==0 && input.emode==2 && input.door==1 && state.temp==6));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==0 && input.door==2 && state.temp==1));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==0 && input.door==2 && state.temp==2));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==0 && input.door==2 && state.temp==3));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==0 && input.door==2 && state.temp==4));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==0 && input.door==2 && state.temp==5));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==0 && input.door==2 && state.temp==6));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==0 && input.door==2 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==0 && input.door==2 && state.temp==8));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==0 && input.door==2 && state.temp==9));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==1 && input.door==2 && state.temp==1));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==1 && input.door==2 && state.temp==2));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==1 && input.door==2 && state.temp==3));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==1 && input.door==2 && state.temp==4));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==1 && input.door==2 && state.temp==5));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==1 && input.door==2 && state.temp==6));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==1 && input.door==2 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==1 && input.door==2 && state.temp==8));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==1 && input.door==2 && state.temp==9));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==2 && input.door==2 && state.temp==1));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==2 && input.door==2 && state.temp==2));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==2 && input.door==2 && state.temp==3));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==2 && input.door==2 && state.temp==4));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==2 && input.door==2 && state.temp==5));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==2 && input.door==2 && state.temp==6));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==2 && input.door==2 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==2 && input.door==2 && state.temp==8));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==0 && input.emode==2 && input.door==2 && state.temp==9));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==0 && input.door==2 && state.temp==1));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==0 && input.door==2 && state.temp==2));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==0 && input.door==2 && state.temp==3));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==0 && input.door==2 && state.temp==4));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==0 && input.door==2 && state.temp==5));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==0 && input.door==2 && state.temp==6));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==0 && input.door==2 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==0 && input.door==2 && state.temp==8));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==0 && input.door==2 && state.temp==9));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==1 && input.door==2 && state.temp==1));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==1 && input.door==2 && state.temp==2));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==1 && input.door==2 && state.temp==3));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==1 && input.door==2 && state.temp==4));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==1 && input.door==2 && state.temp==5));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==1 && input.door==2 && state.temp==6));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==1 && input.door==2 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==1 && input.door==2 && state.temp==8));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==1 && input.door==2 && state.temp==9));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==2 && input.door==2 && state.temp==1));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==2 && input.door==2 && state.temp==2));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==2 && input.door==2 && state.temp==3));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==2 && input.door==2 && state.temp==4));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==2 && input.door==2 && state.temp==5));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==2 && input.door==2 && state.temp==6));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==2 && input.door==2 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==2 && input.door==2 && state.temp==8));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 2 && input.button==1 && input.emode==2 && input.door==2 && state.temp==9));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 3 && input.button==0 && input.emode==0 && input.door==0 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 3 && input.button==0 && input.emode==0 && input.door==0 && state.temp==8));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 3 && input.button==0 && input.emode==0 && input.door==0 && state.temp==9));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 3 && input.button==0 && input.emode==0 && input.door==1 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 3 && input.button==0 && input.emode==0 && input.door==1 && state.temp==8));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 3 && input.button==0 && input.emode==0 && input.door==1 && state.temp==9));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 3 && input.button==0 && input.emode==1 && input.door==0 && state.temp==8));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 3 && input.button==0 && input.emode==1 && input.door==0 && state.temp==9));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 3 && input.button==0 && input.emode==1 && input.door==1 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 3 && input.button==0 && input.emode==1 && input.door==1 && state.temp==8));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 3 && input.button==0 && input.emode==1 && input.door==1 && state.temp==9));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 3 && input.button==1 && input.emode==1 && input.door==0 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 2 && state.fridge_state == 3 && input.button==1 && input.emode==2 && input.door==1 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 3 && state.fridge_state == 1 && input.button==0 && input.emode==1 && input.door==0 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 3 && state.fridge_state == 1 && input.button==0 && input.emode==1 && input.door==2 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 3 && state.fridge_state == 1 && input.button==1 && input.emode==0 && input.door==0 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 3 && state.fridge_state == 1 && input.button==1 && input.emode==0 && input.door==1 && state.temp==1));
	__CPROVER_assume(!(prev_state.fridge_state == 3 && state.fridge_state == 1 && input.button==1 && input.emode==0 && input.door==1 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 3 && state.fridge_state == 1 && input.button==1 && input.emode==0 && input.door==2 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 3 && state.fridge_state == 1 && input.button==1 && input.emode==1 && input.door==0 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 3 && state.fridge_state == 1 && input.button==1 && input.emode==1 && input.door==1 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 3 && state.fridge_state == 1 && input.button==1 && input.emode==1 && input.door==2 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 3 && state.fridge_state == 1 && input.button==1 && input.emode==2 && input.door==0 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 3 && state.fridge_state == 1 && input.button==1 && input.emode==2 && input.door==1 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 3 && state.fridge_state == 1 && input.button==1 && input.emode==2 && input.door==2 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 3 && state.fridge_state == 2 && input.button==0 && input.emode==0 && input.door==2 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 3 && state.fridge_state == 2 && input.button==0 && input.emode==1 && input.door==2 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 3 && state.fridge_state == 2 && input.button==0 && input.emode==2 && input.door==2 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 3 && state.fridge_state == 2 && input.button==1 && input.emode==0 && input.door==2 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 3 && state.fridge_state == 2 && input.button==1 && input.emode==1 && input.door==2 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 3 && state.fridge_state == 2 && input.button==1 && input.emode==2 && input.door==2 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 3 && state.fridge_state == 3 && input.button==0 && input.emode==0 && input.door==1 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 3 && state.fridge_state == 3 && input.button==0 && input.emode==2 && input.door==1 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 3 && state.fridge_state == 3 && input.button==1 && input.emode==0 && input.door==2 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 3 && state.fridge_state == 3 && input.button==1 && input.emode==2 && input.door==0 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 3 && state.fridge_state == 3 && input.button==1 && input.emode==2 && input.door==1 && state.temp==7));
	__CPROVER_assume(!(prev_state.fridge_state == 3 && state.fridge_state == 3 && input.button==1 && input.emode==2 && input.door==2 && state.temp==7));

	while(1)	
	{						
		bool found = false;	
							
		rt_state = state;	
		rt_input = input;	
							
		model_step();		
							
		// input assume		
		INPUT new_input;	
		__CPROVER_assume(new_input.button >= 0 && new_input.button < 2); 
		__CPROVER_assume(new_input.emode >= 0 && new_input.emode < 3); 
		__CPROVER_assume(new_input.door >= 0 && new_input.door < 3); 
			printf("OUTPUT: %d %d %d %d %d\n", 
state.fridge_state, input.button, input.emode, input.door, state.temp);
	printf("OUTPUT: %d %d %d %d %d\n", 
		rt_state.fridge_state, new_input.button, new_input.emode, new_input.door, rt_state.temp);
	printf("PROP: assert(!(state.fridge_state == %d && rt_state.fridge_state == %d && rt_input.button==%d && rt_input.emode==%d && rt_input.door==%d && rt_state.temp==%d));", 
prev_state.fridge_state, state.fridge_state, input.button, input.emode, input.door, state.temp);
	printf("ASSUME: __CPROVER_assume(!(prev_state.fridge_state == %d && state.fridge_state == %d && input.button==%d && input.emode==%d && input.door==%d && state.temp==%d));", 
prev_state.fridge_state, state.fridge_state, input.button, input.emode, input.door, state.temp);
		if ((input.door == Close))
		{	assert(rt_state.fridge_state == Normal_Mode);
			found = true;}
		if (!found)
			assert((rt_state.fridge_state == Door_Open_Mode));
	}
}