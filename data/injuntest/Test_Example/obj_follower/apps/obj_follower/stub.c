#include "ecrobot_interface.h"
#include <stdio.h>
#include <stdlib.h>

#define assume(a) if(!(a)) { exit(0); }

int i2c_busy (int port){
  int i2c_busy_ret;
  i2c_busy_ret = nondet();
  return i2c_busy_ret;
}
void i2c_disable (int port){
}
void i2c_enable (int port){
}
int i2c_start_transaction (int port,unsigned int address,int internal_address,int n_internal_address_bytes,unsigned char * data,unsigned int nbytes,int write){
  int i2c_start_transaction_ret;
  i2c_start_transaction_ret = nondet();
  /* manually put */
  int i;
  data[0] = nondet_uchar();
  assume(data[0] <= 8); // 0..8
  for (i=1;i<nbytes;++i) {
    data[i] = nondet_uchar();
    if (i % 5 == 1) assume(data[i] <= 7);
    else if (i % 5 == 2 || i % 5 == 4) assume(data[i] <= 87);
    else assume(data[i] <= 143);
  }
  for (i=0;i<nbytes;++i) {
    printf("\t\t- nxtcamdata[%d]: %d\n", i, data[i]); fflush(stdout);
  }
  /* manually put end */
  return i2c_start_transaction_ret;
}
void nxt_avr_set_input_power(unsigned char a,int b){
}
void display_goto_xy(int x,int y){
}
void display_int(int val,unsigned int places){
}
void display_string(const char * str){
}
void display_update(void){
}
void nxt_motor_set_speed(unsigned int n,int speed_percent,int brake){
}
