#define TASK (n) void TASK_##n
#define FUNC (n,m) k n FUNC_##k
#define PACKAGE_START_BYTE_VALUE 0xF1
#define PACKAGE_END_BYTE_VALUE 0xFD
#define CONTENTS_START_BYTE_VALUE 0xA1
#define CONTENTS_END_BYTE_VALUE 0xED
#define EQMS_TYPE_ACC_VALUE 0x04
#define EQMS_TYPE_ENV_VALUE 0x05
#define EQMS_TYPE_AIR_VALUE 0x06
#define PARSESTAGE_HEADER 1
#define PARSESTAGE_MESSAGE 2
#define MSG_LEN 30

#include <stdio.h>
#include <malloc.h>
#include <string.h>
#include <time.h>
#include <sys/time.h>
#include <sys/timeb.h>
#include <math.h>
#include "model.h"

INPUT rt_input;
STATE rt_state;
OUTPUT rt_output;

void * data_bytes;
int fail = 0;
int success = 0;

char * fail_status_str[] = {"NO_ACTIVE","FAIL","SUCCESS"};

unsigned char * bytearray(unsigned char * p_buf, size_t * p_length) {
	free(p_buf);
	p_length = 0;
	return NULL;
}


unsigned char * slice_bytearray(unsigned char * p_buf, int p_start, int p_end) {
	unsigned char *temp = (*)malloc((size_t), (p_end - p_start));
	memcpy(temp, p_buf + p_start, (size_t), (p_end - p_start));
	free(p_buf);
	return temp;
}



unsigned char * slice_and_copy_bytearray(unsigned char * p_buf, int p_start, int p_end) {
	unsigned char *temp = (*)malloc((size_t), (p_end - p_start));
	memcpy(temp, p_buf + p_start, (size_t), (p_end - p_start));
	return temp;
}

void append_byte_array(unsigned char * p_source, size_t p_source_length) {
	rt_state.eqms_protocol._buf = realloc(rt_state.eqms_protocol._buf, rt_state.eqms_protocol._buf_length + p_source_length);
	if(rt_state.eqms_protocol._buf == NULL){
		perror("Failed to allocate memory");
		return ;
	}
	memcpy(rt_state.eqms_protocol._buf + rt_state.eqms_protocol._buf_length, p_source, p_source_length);
	rt_state.eqms_protocol._buf_length += p_source_length;
}



void stub_kafka() {
}

void data_received(unsigned char * p_data_bytes, size_t p_data_length) {
	append_byte_array(p_data_bytes, p_data_length);
	rt_state.eqms_protocol._inbound_bytes += p_data_length;
	unsigned char *view = rt_state.eqms_protocol;
	size_t view_length = rt_state.eqms_protocol;
	unsigned int consumed = 0;
	while(view_length > 0){
		if(rt_state.eqms_protocol._parse_stage == PARSESTAGE_HEADER){
			if(view_length < 7){
				break;
			}
			if(view[0] != rt_state.eqms_protocol.PACKAGE_START_BYTE){
				printf("log failure :: err=EQMS_SESSION_CORRUPTED, reason=Packet format violation: START_BYTE\n");
			}
			rt_state.eqms_protocol._current_packet_usim;
			rt_state.eqms_protocol._expected_length = (view[6] << 8) | view[5];
			rt_state.eqms_protocol._expected_length += 1;
			view = slice_and_copy_bytearray(view, 7, view_length);
			view_length -= 7;
			consumed += 7;
			rt_state.eqms_protocol._parse_stage = PARSESTAGE_MESSAGE;
		}
		if(rt_state.eqms_protocol._parse_stage == PARSESTAGE_MESSAGE){
			if(view_length < rt_state.eqms_protocol._expected_length){
				break;
			}
			consumed += rt_state.eqms_protocol._expected_length;
			unsigned char *packet = slice_and_copy_bytearray(view, 0, rt_state.eqms_protocol._expected_length);
			size_t packet_length = rt_state.eqms_protocol;
			view = slice_and_copy_bytearray(view, rt_state.eqms_protocol._expected_length, view_length);
			view_length -= rt_state.eqms_protocol._expected_length;
			if(packet[packet_length - 1] != rt_state.eqms_protocol.PACKAGE_END_BYTE){
				printf("log failure :: err=EQMS_SESSION_CORRUPTED, reason=Packet format violation: END_BYTE\n");
			}
			int key = rt_state.eqms_protocol;
			packet = slice_bytearray(packet, 0, packet_length - 1);
			packet_length = packet_length - 1;
			if(packet[0] != rt_state.eqms_protocol.CONTENTS_START_BYTE || packet[packet_length - 1] != rt_state.eqms_protocol.CONTENTS_END_BYTE){
				rt_state.eqms_protocol._failed += 1;
				fail += 1;
				printf("log failure :: err=EQMS_MSG_PARSE_FAILURE, reason=Message format violation\n");
				continue ;
			}
			int expected_length = (packet[2] << 8);
			if(expected_length + 4 != packet_length){
				rt_state.eqms_protocol._failed += 1;
				fail += 1;
				printf("log failure :: err=EQMS_MSG_PARSE_FAILURE, reason=Length not matched\n");
				continue ;
			}
			packet_length = (packet[1] << 8) | packet[0];
			size_t data_length = packet_length;
			unsigned char msg_type = packet[3];
			unsigned char *bcd_region = slice_and_copy_bytearray(packet, 4, 12);
			unsigned char *msg = slice_and_copy_bytearray(packet, 12, packet_length);
			size_t msg_length = packet_length;
			unsigned char *value = slice_and_copy_bytearray(msg, 0, msg_length);
			size_t value_length = msg_length;
			if(msg_type == rt_state.eqms_protocol.EQMS_TYPE_ACC){
				stub_kafka();
			}
			else if(msg_type == rt_state.eqms_protocol.EQMS_TYPE_AIR){
				stub_kafka();
			}
			else if(msg_type == rt_state.eqms_protocol.EQMS_TYPE_ENV){
				stub_kafka();
			}
			rt_state.eqms_protocol._success += 1;
			success += 1;
			rt_state.eqms_protocol._processed_msg += 1;
			rt_state.eqms_protocol._parse_stage = PARSESTAGE_HEADER;
		}
	}
	free(view);
	view_length = 0;
	if(consumed != 0){
		if(consumed == rt_state.eqms_protocol._buf_length){
			rt_state.eqms_protocol._buf = bytearray(rt_state.eqms_protocol._buf & (rt_state.eqms_protocol._buf_length));
		}
		else{
			rt_state.eqms_protocol._buf = slice_bytearray(rt_state.eqms_protocol._buf, consumed, rt_state.eqms_protocol._buf_length);
		}
	}
	if(fail != 0){
		rt_state.fail_status = FAIL;
	}
	if(success != 0){
		rt_state.fail_status = SUCCESS;
	}
}


void model_step() {
	printf("%s %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d\n", fail_status_str[rt_state.fail_status], rt_state.packetStartByte, rt_state.packetEndByte, rt_state.contentsStartByte, rt_state.contentsEndByte, rt_input.message[0], rt_input.message[1], rt_input.message[2], rt_input.message[3], rt_input.message[4], rt_input.message[5], rt_input.message[6], rt_input.message[7], rt_input.message[8], rt_input.message[9], rt_input.message[10], rt_input.message[11], rt_input.message[12], rt_input.message[13], rt_input.message[14], rt_input.message[15], rt_input.message[16], rt_input.message[17], rt_input.message[18], rt_input.message[19], rt_input.message[20], rt_input.message[21], rt_input.message[22], rt_input.message[23], rt_input.message[24], rt_input.message[25], rt_input.message[26], rt_input.message[27], rt_input.message[28], rt_input.message[29],  rt_state.eqms_protocol._failed, PACKAGE_START_BYTE_VALUE, PACKAGE_END_BYTE_VALUE, CONTENTS_START_BYTE_VALUE, CONTENTS_END_BYTE_VALUE);
	rt_state.eqms_protocol.PACKAGE_START_BYTE = PACKAGE_START_BYTE_VALUE;
	rt_state.eqms_protocol.PACKAGE_END_BYTE = PACKAGE_END_BYTE_VALUE;
	rt_state.eqms_protocol.CONTENTS_START_BYTE = CONTENTS_START_BYTE_VALUE;
	rt_state.eqms_protocol.CONTENTS_END_BYTE = CONTENTS_END_BYTE_VALUE;
	rt_state.eqms_protocol.EQMS_TYPE_ACC = EQMS_TYPE_ACC_VALUE;
	rt_state.eqms_protocol.EQMS_TYPE_ENV = EQMS_TYPE_ENV_VALUE;
	rt_state.eqms_protocol.EQMS_TYPE_AIR = EQMS_TYPE_AIR_VALUE;
	rt_state.eqms_protocol._buf = NULL;
	rt_state.eqms_protocol._parse_stage = PARSESTAGE_HEADER;
	rt_state.eqms_protocol._buf_length = 0;
	rt_state.eqms_protocol._inbound_bytes = 0;
	rt_state.eqms_protocol._processed_msg = 0;
	rt_state.eqms_protocol._failed = 0;
	rt_state.eqms_protocol._success = 0;
	fail = 0;
	success = 0;
	for(int i = 0 ; i < MSG_LEN ; i++){
		;
	}
	rt_input.message[5] = MSG_LEN - 8;
	rt_input.message[6] = 0;
	rt_input.message[8] = MSG_LEN - 12;
	rt_input.message[9] = 0;
	rt_state.packetStartByte = rt_input.message[0];
	rt_state.packetEndByte = rt_input.message[MSG_LEN - 1];
	rt_state.contentsStartByte = rt_input.message[7];
	rt_state.contentsEndByte = rt_input.message[MSG_LEN - 2];
	data_received(rt_input.message, MSG_LEN);
}


void model_initialize() {
	data_bytes = 0;
	rt_state.fail_status = NO_ACTIVE;
	rt_state.packetStartByte = 0;
	rt_state.packetEndByte = 0;
	rt_state.contentsStartByte = 0;
	rt_state.contentsEndByte = 0;
	fail = 0;
	success = 0;
	rt_input.message[0] = 0;
	rt_input.message[1] = 0;
	rt_input.message[2] = 0;
	rt_input.message[3] = 0;
	rt_input.message[4] = 0;
	rt_input.message[5] = 0;
	rt_input.message[6] = 0;
	rt_input.message[7] = 0;
	rt_input.message[8] = 0;
	rt_input.message[9] = 0;
	rt_input.message[10] = 0;
	rt_input.message[11] = 0;
	rt_input.message[12] = 0;
	rt_input.message[13] = 0;
	rt_input.message[14] = 0;
	rt_input.message[15] = 0;
	rt_input.message[16] = 0;
	rt_input.message[17] = 0;
	rt_input.message[18] = 0;
	rt_input.message[19] = 0;
	rt_input.message[20] = 0;
	rt_input.message[21] = 0;
	rt_input.message[22] = 0;
	rt_input.message[23] = 0;
	rt_input.message[24] = 0;
	rt_input.message[25] = 0;
	rt_input.message[26] = 0;
	rt_input.message[27] = 0;
	rt_input.message[28] = 0;
	rt_input.message[29] = 0;
}


