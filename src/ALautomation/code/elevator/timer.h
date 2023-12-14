#pragma once

void startTimer(double length);

int getTimerStatus(); // returnerer 1 om timeren er gått ut

void timerDeactivate(); // avbryt timer

int isTimerActive(); // returner 1 om timeren er aktivert


// 2022-09-21: added for running CBMC.
// -----------------------------------------
#define CLOCKS_PER_SEC 1000000
typedef long clock_t;
// extern clock_t __VERFIER_nondet_long();
// clock_t previousTime = 0; 
clock_t clock();
