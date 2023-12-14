#define TASK(n) void TASK_##n
#define FUNC(n,m) k n FUNC_##k
#define MAX_TIME10

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "model.h"

INPUT rt_input;
STATE rt_state;
OUTPUT rt_output;

int item_price = 0;
int change = 0;
int price = 0;
int item = 0;

char * item_str[] = {"COKE","PEPSI","WATER","JUICE"};
char * machine_state_str[] = {"Initial","Insert","ButtonEnabled","Emit","Return"};

int processSelection(int selected_item) {
	switch(selected_item){
		case COKE :
			price = 1000;
			break;
		case PEPSI :
			price = 1000;
			break;
		case WATER :
			price = 500;
			break;
		case JUICE :
			price = 2000;
		default :
			break;
	}
	return price;
}

void model_step() {
	printf("%s %d %d %d %d\n", machine_state_str[rt_state.machine_state], rt_state.timer, rt_input.amount, rt_input.item_num, rt_input.buttonPress);
	switch(rt_state.machine_state){
		case Initial :
			rt_state.machine_state = Insert;
			break;
		case Insert :
			;
			if(rt_input.amount == 0){
				rt_state.timer--;
				if(rt_state.timer <= 0){
					rt_state.timer = MAX_TIME;
					rt_state.machine_state = Initial;
				}
			}
			else{
				rt_state.machine_state = ButtonEnabled;
				rt_state.timer = MAX_TIME;
			}
			break;
		case ButtonEnabled :
			if(rt_input.buttonPress){
				;
				if(rt_input.item_num == 0){
					item = COKE;
				}
				else if(rt_input.item_num == 1){
					item = PEPSI;
				}
				else if(rt_input.item_num == 2){
					item = WATER;
				}
				else if(rt_input.item_num == 3){
					item = JUICE;
				}
				rt_state.machine_state = Emit;
				rt_state.timer = MAX_TIME;
			}
			else{
				rt_state.timer--;
				if(rt_state.timer <= 0){
					rt_state.timer = MAX_TIME;
					rt_state.machine_state = Initial;
				}
			}
			break;
		case Emit :
			item_price = processSelection(item);
			rt_state.machine_state = Return;
			break;
		case Return :
			change = rt_input.amount - item_price;
			rt_state.machine_state = Initial;
			break;
		default :
			break;
	}
}


void model_initialize() {
	rt_input.item_num = 0;
	item_price = 0;
	rt_input.amount = 0;
	change = 0;
	price = 0;
	item = 0;
	rt_state.timer = MAX_TIME;
	rt_state.machine_state = Initial;
	rt_input.buttonPress = 0;
}


