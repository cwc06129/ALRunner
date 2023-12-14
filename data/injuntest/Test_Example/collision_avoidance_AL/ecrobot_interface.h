#ifndef _ECROBOT_INTERFACE_H
#define _ECROBOT_INTERFACE_H

// enum declaration
typedef enum {NXT_PORT_A, NXT_PORT_B, NXT_PORT_C} MOTOR_PORT_T;
typedef enum {NXT_PORT_S1, NXT_PORT_S2, NXT_PORT_S3, NXT_PORT_S4} SENSOR_PORT_T;

// preprocess declaration
#define DISTANCE_L NXT_PORT_S4
#define DISTANCE_R NXT_PORT_S3
#define BUTTON_L NXT_PORT_S1
#define BUTTON_R NXT_PORT_S2
#define MOTOR_R NXT_PORT_A
#define MOTOR_L NXT_PORT_C
#define MOTOR_S NXT_PORT_B

/* 2022-05-18 move nxt_motors.h to ecrobot_interface.h */
void nxt_motor_set_count() {}
/**/

/* 2022-05-18 void function declaration */
void ecrobot_set_motor_speed();
void ecrobot_init_sonar_sensor();
void ecrobot_get_sonar_sensor();
void ecrobot_term_sonar_sensor();
void systick_wait_ms();
void ecrobot_device_initialize();
void ecrobot_device_terminate();
void user_1ms_isr_type2();
/**/

/* 2022-05-18 local variables and function parameters declaration */
unsigned int nxt_motor_set_count_n;
int nxt_motor_set_count_count;
unsigned char ecrobot_set_motor_speed_port_id;
signed int ecrobot_set_motor_speed_speed;
unsigned char ecrobot_init_sonar_sensor_port_id;
unsigned char ecrobot_get_sonar_sensor_port_id;
signed int ecrobot_get_sonar_sensor_return;
unsigned char ecrobot_term_sonar_sensor_port_id;
int systick_wait_ms_a;
/**/


void ecrobot_set_motor_speed() {}

void ecrobot_init_sonar_sensor() {}

void ecrobot_get_sonar_sensor() {}

void ecrobot_term_sonar_sensor() {}

void systick_wait_ms() {}

void ecrobot_device_initialize() {
    ecrobot_init_sonar_sensor_port_id = DISTANCE_L;
    ecrobot_init_sonar_sensor();
    ecrobot_init_sonar_sensor_port_id = DISTANCE_R;
    ecrobot_init_sonar_sensor();
    nxt_motor_set_count_n = MOTOR_S;
    nxt_motor_set_count_count = 0;
    nxt_motor_set_count();
}

void ecrobot_device_terminate() {
    ecrobot_term_sonar_sensor_port_id = DISTANCE_L;
    ecrobot_term_sonar_sensor();
    ecrobot_term_sonar_sensor_port_id = DISTANCE_R;
    ecrobot_term_sonar_sensor();
    ecrobot_set_motor_speed_port_id = MOTOR_L;
    ecrobot_set_motor_speed_speed = 0;
    ecrobot_set_motor_speed();
    ecrobot_set_motor_speed_port_id = MOTOR_R;
    ecrobot_set_motor_speed_speed = 0;
    ecrobot_set_motor_speed();
    ecrobot_set_motor_speed_port_id = MOTOR_S;
    ecrobot_set_motor_speed_speed = 0;
    ecrobot_set_motor_speed();
}

void user_1ms_isr_type2() {}

#endif
