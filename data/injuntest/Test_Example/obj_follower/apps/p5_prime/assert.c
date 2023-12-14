/*
* There is no driving of straight forward signal.
* expected result: violated
*/

#include <assert.h>

// variables in property.
extern int leftMotorValue;
extern int rightMotorValue;
extern int direction_adjustment;

// automata states.
#define S0 0
#define S1 1
int state = S0;

// symboilc variables.
#define c1 (leftMotorValue == rightMotorValue)
#define c2 (leftMotorValue > 0)
#define c3 (direction_adjustment == 0)

// property checker automata.
void update_prop_automata() {
  if (state == S0) {
    if (c1 && c2 && c3) state = S1;
  } else {
    // when the state is S1...   
  }
}

// check property by assert violation with model checking.
void check_property() {
  // update_prop_automata();
  // assert(state != S1);
  assert(!(c1 && c2 && c3));
}