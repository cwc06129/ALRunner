#include <stddef.h>
#include <stdio.h>
#include <stdlib.h>
#include "model.h"
#include <time.h>

INPUT get_rand_values() {
	INPUT in;
	in.teleevent = rand() % 7 + 0;
	in.dialDigit = rand() % 6 + 0;
	return in;
}

int main() {
	srand(time(NULL));
	int inp[50][50];
	int header = 1;
	if(header) {
		printf("types\n");
		printf("Idle rt_state.timer:N rt_input.teleevent:E rt_input.dialDigit:N\n");
		printf("DialTone rt_state.timer:N rt_input.teleevent:E rt_input.dialDigit:N\n");
		printf("TimeOut rt_state.timer:N rt_input.teleevent:E rt_input.dialDigit:N\n");
		printf("Dialing rt_state.timer:N rt_input.teleevent:E rt_input.dialDigit:N\n");
		printf("Connecting rt_state.timer:N rt_input.teleevent:E rt_input.dialDigit:N\n");
		printf("Invalid rt_state.timer:N rt_input.teleevent:E rt_input.dialDigit:N\n");
		printf("Busy rt_state.timer:N rt_input.teleevent:E rt_input.dialDigit:N\n");
		printf("Ringing rt_state.timer:N rt_input.teleevent:E rt_input.dialDigit:N\n");
		printf("Talking rt_state.timer:N rt_input.teleevent:E rt_input.dialDigit:N\n");
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
