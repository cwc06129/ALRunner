#define TASK (n) void TASK_##n
#define FUNC (n,m) k n FUNC_##k

#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include "model.h"

INPUT rt_input;
STATE rt_state;
OUTPUT rt_output;



int binarySearch(int l, int r, int x) {
	if(r >= l){
		int mid = l;
		if(rt_input.arr[mid] == x){
			return mid;
		}
		if(rt_input.arr[mid] > x){
			return binarySearch(l, mid - 1, x);
		}
		return binarySearch(mid + 1, r, x);
	}
	return -1;
}

void model_step() {
	printf("%d %d %d %d %d %d %d\n", rt_output.result, rt_input.arr[0], rt_input.arr[1], rt_input.arr[2], rt_input.arr[3], rt_input.arr[4], rt_input.key);
	for(int i = 0 ; i < 5 ; i++){
		;
	}
	rt_output.result = binarySearch(0, 4, rt_input.key);
}


void model_initialize() {
	rt_input.arr[0] = 0;
	rt_input.arr[1] = 0;
	rt_input.arr[2] = 0;
	rt_input.arr[3] = 0;
	rt_input.arr[4] = 0;
	rt_input.key = 0;
	rt_output.result = -1;
}


