#ifndef _ENUM_DEFINE
//enum information
typedef enum _Fail_status {NO_ACTIVE, FAIL, SUCCESS} Fail_status;
#define _ENUM_DEFINE
#endif

typedef struct _EQMSProtocol{
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
} EQMSProtocol;

typedef struct {
	char message[30];
} INPUT;

typedef struct {
	Fail_status fail_status;
	int contentsStartByte;
	int packetStartByte;
	int packetEndByte;
	int contentsEndByte;
} STATE;

typedef struct {
} OUTPUT;

extern INPUT rt_input;
extern STATE rt_state;
extern OUTPUT rt_output;
extern void model_initialize(void);
extern void model_step(void);
