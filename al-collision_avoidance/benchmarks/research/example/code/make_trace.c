#include <stddef.h>
#include <stdio.h>
#include <stdlib.h>
#include "model.h"
#include <time.h>

INPUT get_rand_values() {
	INPUT in;
	in.obsDistance[0] = rand() % 26 + 0;
	in.obsDistance[1] = rand() % 26 + 0;
	in.obsDistance[2] = rand() % 26 + 0;
	in.obsDistance[3] = rand() % 26 + 0;
	return in;
}

int main() {
	srand(time(NULL));
	int inp[50][50];
	int header = 1;
	if(header) {
		printf("types\n");
		printf("Straight rt_state.motor_speed_status:E rt_state.motor_speed_0:N rt_state.motor_speed_1:N rt_input.obsDistance_0:N rt_input.obsDistance_1:N rt_input.obsDistance_2:N rt_input.obsDistance_3:N\n");
		printf("Turn_left rt_state.motor_speed_status:E rt_state.motor_speed_0:N rt_state.motor_speed_1:N rt_input.obsDistance_0:N rt_input.obsDistance_1:N rt_input.obsDistance_2:N rt_input.obsDistance_3:N\n");
		printf("Turn_right rt_state.motor_speed_status:E rt_state.motor_speed_0:N rt_state.motor_speed_1:N rt_input.obsDistance_0:N rt_input.obsDistance_1:N rt_input.obsDistance_2:N rt_input.obsDistance_3:N\n");
		printf("trace\n");
	}
	/* Initialize model */
	int i = 0;
	while(i < sizeof(inp)/sizeof(inp[0])) {
		int j = 0;
		STATE init_state;
		INPUT init_input;
		OUTPUT init_output;

		rt_state = init_state;
		rt_input = init_input;
		rt_output = init_output;
		model_initialize();

		while(j < sizeof(inp[0])/sizeof(inp[0][0])) {
			rt_input = get_rand_values();
			model_step();
			j++;
		}
		printf("trace\n");
		i++;
	}
	return 0;
}
