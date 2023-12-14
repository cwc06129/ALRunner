#include <stddef.h>
#include <stdio.h>
#include <stdlib.h>
#include "model.h"
#include <time.h>

INPUT get_rand_values() {
	INPUT in;
	in.message[0] = rand() % 256 + 0;
	in.message[1] = rand() % 256 + 0;
	in.message[2] = rand() % 256 + 0;
	in.message[3] = rand() % 256 + 0;
	in.message[4] = rand() % 256 + 0;
	in.message[5] = rand() % 256 + 0;
	in.message[6] = rand() % 256 + 0;
	in.message[7] = rand() % 256 + 0;
	in.message[8] = rand() % 256 + 0;
	in.message[9] = rand() % 256 + 0;
	in.message[10] = rand() % 256 + 0;
	in.message[11] = rand() % 256 + 0;
	in.message[12] = rand() % 256 + 0;
	in.message[13] = rand() % 256 + 0;
	in.message[14] = rand() % 256 + 0;
	in.message[15] = rand() % 256 + 0;
	in.message[16] = rand() % 256 + 0;
	in.message[17] = rand() % 256 + 0;
	in.message[18] = rand() % 256 + 0;
	in.message[19] = rand() % 256 + 0;
	in.message[20] = rand() % 256 + 0;
	in.message[21] = rand() % 256 + 0;
	in.message[22] = rand() % 256 + 0;
	in.message[23] = rand() % 256 + 0;
	in.message[24] = rand() % 256 + 0;
	in.message[25] = rand() % 256 + 0;
	in.message[26] = rand() % 256 + 0;
	in.message[27] = rand() % 256 + 0;
	in.message[28] = rand() % 256 + 0;
	in.message[29] = rand() % 256 + 0;
	return in;
}

int main() {
	srand(time(NULL));
	int inp[50][50];
	int header = 1;
	if(header) {
		printf("types\n");
		printf("NO_ACTIVE rt_state.packetStartByte:N rt_state.packetEndByte:N rt_state.contentsStartByte:N rt_state.contentsEndByte:N rt_input.message_0:N rt_input.message_1:N rt_input.message_2:N rt_input.message_3:N rt_input.message_4:N rt_input.message_5:N rt_input.message_6:N rt_input.message_7:N rt_input.message_8:N rt_input.message_9:N rt_input.message_10:N rt_input.message_11:N rt_input.message_12:N rt_input.message_13:N rt_input.message_14:N rt_input.message_15:N rt_input.message_16:N rt_input.message_17:N rt_input.message_18:N rt_input.message_19:N rt_input.message_20:N rt_input.message_21:N rt_input.message_22:N rt_input.message_23:N rt_input.message_24:N rt_input.message_25:N rt_input.message_26:N rt_input.message_27:N rt_input.message_28:N rt_input.message_29:N PACKAGE_START_BYTE_VALUE:N PACKAGE_END_BYTE_VALUE:N CONTENTS_START_BYTE_VALUE:N CONTENTS_END_BYTE_VALUE:N\n");
		printf("FAIL rt_state.packetStartByte:N rt_state.packetEndByte:N rt_state.contentsStartByte:N rt_state.contentsEndByte:N rt_input.message_0:N rt_input.message_1:N rt_input.message_2:N rt_input.message_3:N rt_input.message_4:N rt_input.message_5:N rt_input.message_6:N rt_input.message_7:N rt_input.message_8:N rt_input.message_9:N rt_input.message_10:N rt_input.message_11:N rt_input.message_12:N rt_input.message_13:N rt_input.message_14:N rt_input.message_15:N rt_input.message_16:N rt_input.message_17:N rt_input.message_18:N rt_input.message_19:N rt_input.message_20:N rt_input.message_21:N rt_input.message_22:N rt_input.message_23:N rt_input.message_24:N rt_input.message_25:N rt_input.message_26:N rt_input.message_27:N rt_input.message_28:N rt_input.message_29:N PACKAGE_START_BYTE_VALUE:N PACKAGE_END_BYTE_VALUE:N CONTENTS_START_BYTE_VALUE:N CONTENTS_END_BYTE_VALUE:N\n");
		printf("SUCCESS rt_state.packetStartByte:N rt_state.packetEndByte:N rt_state.contentsStartByte:N rt_state.contentsEndByte:N rt_input.message_0:N rt_input.message_1:N rt_input.message_2:N rt_input.message_3:N rt_input.message_4:N rt_input.message_5:N rt_input.message_6:N rt_input.message_7:N rt_input.message_8:N rt_input.message_9:N rt_input.message_10:N rt_input.message_11:N rt_input.message_12:N rt_input.message_13:N rt_input.message_14:N rt_input.message_15:N rt_input.message_16:N rt_input.message_17:N rt_input.message_18:N rt_input.message_19:N rt_input.message_20:N rt_input.message_21:N rt_input.message_22:N rt_input.message_23:N rt_input.message_24:N rt_input.message_25:N rt_input.message_26:N rt_input.message_27:N rt_input.message_28:N rt_input.message_29:N PACKAGE_START_BYTE_VALUE:N PACKAGE_END_BYTE_VALUE:N CONTENTS_START_BYTE_VALUE:N CONTENTS_END_BYTE_VALUE:N\n");
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
