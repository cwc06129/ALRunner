
typedef unsigned char U8;

void 	i2c_disable				(int port);
void 	i2c_enable				(int port);
void 	i2c_init				(void);
int 	i2c_busy				(int port);
int 	i2c_start_transaction	(int port, unsigned int address, int internal_address, int n_internal_address_bytes, unsigned char *data, unsigned int nbytes, int write);

void nxt_avr_set_input_power(unsigned char a, int b);
void nxt_avr_set_motor(unsigned int n, int power_percent, int brake);
void nxt_motor_set_speed(unsigned int n, int speed_percent, int brake);

void display_goto_xy(int x, int y);
void display_string(const char *str);
void display_int(int val, unsigned int places);
void display_update(void);
