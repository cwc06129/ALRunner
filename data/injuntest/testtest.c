#define TASK(n) void TASK_##n

/* ---------------------------------------------------------------------------
 ** camera.c
 **
 ** Mindsensors NxtCam driver for NxtOsek.
 **
 ** Author: Adrian Dudau
 ** Acknowledgments: based on code from Takashi Chikamasa for the i2C communication.
 ** -------------------------------------------------------------------------*/

//#include "mymath.h"
//#include "kernel.h"
//#include "kernel_id.h"
#include "ecrobot_interface.h"
#include "camera.h"

#define MYABS(x)	(((x) >= 0)? (x):-(x));
#define MYSUM(x,y) x+y
#define ADDRESS 0x01

/*
 * Returns the width of the rectangle of index rectindex.
 */
int getWidth(U8 rectindex) {
	int xul = (int) nxtcamdata[5 * rectindex + 1 + 1];
	int xlr = (int) nxtcamdata[5 * rectindex + 1 + 3];

	return MYABS(xlr - xul);
}

// /*
//  * Returns the height of the rectangle of index rectindex.
//  */
// int getHeight(U8 rectindex) {
// 	int yul = (int) nxtcamdata[5 * rectindex + 1 + 2];
// 	int ylr = (int) nxtcamdata[5 * rectindex + 1 + 4];

// 	return MYABS(ylr - yul);
// }

// int getSum(U8 rectindex) {
// 	int yul = (int) nxtcamdata[5 * rectindex + 1 + 2];
// 	int ylr = (int) nxtcamdata[5 * rectindex + 1 + 4];

// 	return MYSUM(ylr, yul);
// }

// /*
//  * Returns the area of the rectangle of index rectindex.
//  */
// int getArea(U8 rectindex) {
// 	return getWidth(rectindex)*getHeight(rectindex);
// }

// /*
//  * Terminate I2C communication on the specified port
//  */
// void term_nxtcam(U8 port_id)
// {
// 	i2c_disable(port_id);
// }