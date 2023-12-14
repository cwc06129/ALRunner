#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

// written by yshin
// 2023.10.07

#include <stdio.h>
#include <malloc.h>
#include <string.h>
#include <time.h>
#include <sys/time.h>
#include <sys/timeb.h>
//#include <math.h>

#define PACKAGE_START_BYTE_VALUE 0xF1
#define PACKAGE_END_BYTE_VALUE 0xFD

#define CONTENTS_START_BYTE_VALUE 0xA1
#define CONTENTS_END_BYTE_VALUE 0xED

#define EQMS_TYPE_ACC_VALUE 0x04
#define EQMS_TYPE_ENV_VALUE 0x05
#define EQMS_TYPE_AIR_VALUE 0x06

#define PARSESTAGE_HEADER 1
#define PARSESTAGE_MESSAGE 2
#define MSG_LEN 30 // 2023-11-02(Thu) SoheeJung : message length fixed value

void *data_bytes;

struct _EQMSProtocol {
    unsigned char PACKAGE_START_BYTE;
    unsigned char PACKAGE_END_BYTE;

    unsigned char CONTENTS_START_BYTE;
    unsigned char CONTENTS_END_BYTE;

    unsigned char EQMS_TYPE_ACC;
    unsigned char EQMS_TYPE_ENV;
    unsigned char EQMS_TYPE_AIR;

    unsigned char* _buf;
    size_t _buf_length;

    int _parse_stage;
    int _current_packet_usim;
    int _expected_length;

    int _inbound_bytes;
    int _processed_msg;
    int _failed;
    int _success;
};
typedef struct _EQMSProtocol EQMSProtocol;
EQMSProtocol eqms_protocol;

// 2023-11-02(Thu) SoheeJung : enumeration type addition
typedef enum _Fail_status{NO_ACTIVE, FAIL, SUCCESS} Fail_status;
Fail_status fail_status = NO_ACTIVE;

// 2023-10-25(Wed) SoheeJung : variable addition
int packetStartByte = 0;
int packetEndByte = 0;
int contentsStartByte = 0;
int contentsEndByte = 0;
int fail = 0;
int success = 0;
char message[MSG_LEN];

unsigned char* bytearray(unsigned char* p_buf, size_t* p_length) {
    free(p_buf);

    p_length = 0;

    return NULL;
}

unsigned char* slice_bytearray(unsigned char* p_buf, int p_start, int p_end) {
    unsigned char* temp = (unsigned char*) malloc((size_t)(p_end - p_start));
    memcpy(temp, p_buf + p_start, (size_t)(p_end - p_start));

    free(p_buf);

     return temp;
}

unsigned char* slice_and_copy_bytearray(unsigned char* p_buf, int p_start, int p_end) {
    unsigned char* temp = (unsigned char*) malloc((size_t)(p_end - p_start));
    memcpy(temp, p_buf + p_start, (size_t)(p_end - p_start));

    return temp;
}


void append_byte_array(unsigned char* p_source, size_t p_source_length) {
    // shorter than the above
    eqms_protocol._buf = realloc(eqms_protocol._buf, eqms_protocol._buf_length + p_source_length);
    if(eqms_protocol._buf == NULL) {
        perror("Failed to allocate memory");
        return;
    }

    memcpy(eqms_protocol._buf + eqms_protocol._buf_length, p_source, p_source_length);

    eqms_protocol._buf_length += p_source_length;
}

void stub_kafka() {}

void data_received(unsigned char* p_data_bytes, size_t p_data_length) {
    append_byte_array(p_data_bytes, p_data_length);

    eqms_protocol._inbound_bytes += p_data_length;

    unsigned char* view = eqms_protocol._buf;
    size_t view_length = eqms_protocol._buf_length;

    unsigned int consumed = 0;

    while (view_length > 0) {
        if(eqms_protocol._parse_stage == PARSESTAGE_HEADER) {
            if(view_length < 7) {
                break;
            }

            if(view[0] != eqms_protocol.PACKAGE_START_BYTE) {
                printf("log failure :: err=EQMS_SESSION_CORRUPTED, reason=Packet format violation: START_BYTE\n");
            }

            eqms_protocol._current_packet_usim = *((int*)slice_and_copy_bytearray(view, 1, 5));

            // little endian
            eqms_protocol._expected_length = (view[6] << 8) | view[5];

            eqms_protocol._expected_length += 1;

            view = slice_and_copy_bytearray(view, 7, view_length);
            view_length -= 7;

            consumed += 7;

            eqms_protocol._parse_stage = PARSESTAGE_MESSAGE;
        }

        if(eqms_protocol._parse_stage == PARSESTAGE_MESSAGE) {
            if(view_length < eqms_protocol._expected_length) {
                break;
            }

            consumed += eqms_protocol._expected_length;
            unsigned char* packet = slice_and_copy_bytearray(view, 0, eqms_protocol._expected_length);
            size_t packet_length = eqms_protocol._expected_length;
            view = slice_and_copy_bytearray(view, eqms_protocol._expected_length, view_length);
            view_length -= eqms_protocol._expected_length;

            if(packet[packet_length - 1] != eqms_protocol.PACKAGE_END_BYTE) {
                printf("log failure :: err=EQMS_SESSION_CORRUPTED, reason=Packet format violation: END_BYTE\n");
            }

            // try-catch statement in the original source code
            int key = eqms_protocol._current_packet_usim;

            packet = slice_bytearray(packet, 0, packet_length - 1);
            packet_length = packet_length - 1;
            // Error #1 is expected here.
            if(packet[0] != eqms_protocol.CONTENTS_START_BYTE || packet[packet_length - 1] != eqms_protocol.CONTENTS_END_BYTE) {
                eqms_protocol._failed += 1;
                fail += 1; // 2023-10-25(Wed) SoheeJung : variable addition
                printf("log failure :: err=EQMS_MSG_PARSE_FAILURE, reason=Message format violation\n");
                continue;
            }

            // little endian
            int expected_length = (packet[2] << 8) | packet[1];

            if(expected_length + 4 != packet_length) {
                eqms_protocol._failed += 1;
                fail += 1; // 2023-10-25(Wed) SoheeJung : variable addition
                printf("log failure :: err=EQMS_MSG_PARSE_FAILURE, reason=Length not matched\n");
                continue;
            }

            // littne endian
            packet_length = (packet[1] << 8) | packet[0];
            size_t data_length = packet_length - 9;

            unsigned char msg_type = packet[3];

            unsigned char* bcd_region = slice_and_copy_bytearray(packet, 4, 12);
            // struct timeb ts = bcd_time_to_timestamp(bcd_region, 8);

            unsigned char* msg = slice_and_copy_bytearray(packet, 12, packet_length);
            size_t msg_length = packet_length - 12;

            unsigned char* value = slice_and_copy_bytearray(msg, 0, msg_length);
            size_t value_length = msg_length;

            if(msg_type == eqms_protocol.EQMS_TYPE_ACC) {
                stub_kafka();
            }
            else if(msg_type == eqms_protocol.EQMS_TYPE_AIR) {
                stub_kafka();
            }
            else if(msg_type == eqms_protocol.EQMS_TYPE_ENV) {
                stub_kafka();
            }
            eqms_protocol._success += 1;
            success += 1;

            eqms_protocol._processed_msg += 1;
            eqms_protocol._parse_stage = PARSESTAGE_HEADER;
            // end of try-catch statement in the original source code
        }
    }

    free(view);
    view_length = 0;

    if(consumed != 0) {
        if(consumed == eqms_protocol._buf_length) {
            eqms_protocol._buf = bytearray(eqms_protocol._buf, &(eqms_protocol._buf_length));
        }
        else {
            eqms_protocol._buf = slice_bytearray(eqms_protocol._buf, consumed, eqms_protocol._buf_length);
        }
    }

    if(fail != 0) fail_status = FAIL;
    if(success != 0) fail_status = SUCCESS;
}

int main() {
    // initialize
    // mandatory
    eqms_protocol.PACKAGE_START_BYTE = PACKAGE_START_BYTE_VALUE;
    eqms_protocol.PACKAGE_END_BYTE = PACKAGE_END_BYTE_VALUE;
    eqms_protocol.CONTENTS_START_BYTE = CONTENTS_START_BYTE_VALUE;
    eqms_protocol.CONTENTS_END_BYTE = CONTENTS_END_BYTE_VALUE;
    eqms_protocol.EQMS_TYPE_ACC = EQMS_TYPE_ACC_VALUE;
    eqms_protocol.EQMS_TYPE_ENV = EQMS_TYPE_ENV_VALUE;
    eqms_protocol.EQMS_TYPE_AIR = EQMS_TYPE_AIR_VALUE;

    eqms_protocol._buf = NULL;
    eqms_protocol._parse_stage = PARSESTAGE_HEADER;
    eqms_protocol._buf_length = 0;

    eqms_protocol._inbound_bytes = 0;
    eqms_protocol._processed_msg = 0;
    eqms_protocol._failed = 0;
    eqms_protocol._success = 0;
    // end of initialize

    fail = 0;
    success = 0;
    
    for(int i = 0; i < MSG_LEN; i++) {
        message[i] = rand() % 256;
    }
    // sensor message length
    message[5] = MSG_LEN - 8;
    message[6] = 0;
    message[8] = MSG_LEN - 12;
    message[9] = 0;

    packetStartByte = message[0];
    packetEndByte = message[MSG_LEN - 1];
    contentsStartByte = message[7];
    contentsEndByte = message[MSG_LEN - 2];
    
    data_received(message, MSG_LEN);
}