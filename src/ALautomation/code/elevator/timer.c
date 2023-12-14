#include "timer.h"

static clock_t timerStarted; // tidspunkt timeren startes
static int timerActive = 0; //om timeren er aktiv
static double duration; // varighet for timeren
long previousTime = 0;

void startTimer(double length) {
	timerStarted = clock();
	duration = length;
	timerActive = 1;
}


int getTimerStatus() { // returnerer 1 om timeren er gått ut
	long double deltaTime = (long double)(clock() - timerStarted) / (CLOCKS_PER_SEC); 
	if (deltaTime >= duration && timerActive) {
		return 1;
	}
	return 0;
}


void timerDeactivate() {
	timerActive = 0;
}


int isTimerActive() {
	return timerActive;
}

clock_t clock() {
    // long currentTime = __VERFIER_nondet_long();
    // __CPROVER_assume(currentTime > previousTime);
    // previousTime = currentTime;
    long currentTime = previousTime + 1;
    previousTime = currentTime;
    return currentTime;
}
