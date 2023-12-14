#define TASK(n) void TASK_##n
#define FUNC(n,m) k n FUNC_##k

#include <stdio.h>
#include <stdlib.h>
#include <math.h>//Thisisneededtousesqrt()function
#include "model.h"

INPUT rt_input;
STATE rt_state;
OUTPUT rt_output;

float r1;
float r2;
float real;
float imag;


void model_step() {
	printf("%d %d %d %d %d\n", rt_state.result, rt_state.determinant, rt_input.calca, rt_input.calcb, rt_input.calcc);
	;
	;
	;
	rt_state.determinant = rt_input.calcb * rt_input.calcb - 4 * rt_input.calca * rt_input.calcc;
	if(rt_state.determinant > 0){
		r1 = (-rt_input.calcb + sqrt(rt_state.determinant)) / 2 * rt_input.calca;
		r2 = (-rt_input.calcb - sqrt(rt_state.determinant)) / 2 * rt_input.calca;
		rt_state.result = 2;
	}
	else if(rt_state.determinant == 0){
		r1 = r2 = -rt_input.calcb / (2 * rt_input.calca);
		rt_state.result = 1;
	}
	else{
		real = -rt_input.calcb / (2 * rt_input.calca);
		imag = sqrt( - rt_state.determinant) / (2 * rt_input.calca);
		rt_state.result = 0;
	}
}



void model_initialize() {
	rt_input.calca = 0;
	rt_input.calcb = 0;
	rt_input.calcc = 0;
	rt_state.determinant = 0;
	r1 = 0;
	r2 = 0;
	real = 0;
	imag = 0;
	rt_state.result = 0;
}


