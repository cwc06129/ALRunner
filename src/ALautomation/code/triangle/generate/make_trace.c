#include <stddef.h>
#include <stdio.h>
#include <stdlib.h>
#include "model.h"
#include <time.h>

INPUT get_rand_values() {
	INPUT in;
	in.trianglea = rand() % 9 + 1;
	in.triangleb = rand() % 9 + 1;
	in.trianglec = rand() % 9 + 1;
	return in;
}

int main() {
	srand(time(NULL));
	int inp[50][50];
	int header = 1;
	if(header) {
		printf("types\n");
		printf("0 rt_input.trianglea:N rt_input.triangleb:N rt_input.trianglec:N rt_output.result:N\n");
		printf("1 rt_input.trianglea:N rt_input.triangleb:N rt_input.trianglec:N rt_output.result:N\n");
		printf("2 rt_input.trianglea:N rt_input.triangleb:N rt_input.trianglec:N rt_output.result:N\n");
		printf("3 rt_input.trianglea:N rt_input.triangleb:N rt_input.trianglec:N rt_output.result:N\n");
		printf("4 rt_input.trianglea:N rt_input.triangleb:N rt_input.trianglec:N rt_output.result:N\n");
		printf("5 rt_input.trianglea:N rt_input.triangleb:N rt_input.trianglec:N rt_output.result:N\n");
		printf("6 rt_input.trianglea:N rt_input.triangleb:N rt_input.trianglec:N rt_output.result:N\n");
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
