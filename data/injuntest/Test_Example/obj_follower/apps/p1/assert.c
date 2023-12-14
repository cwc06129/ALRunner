/*
* Automotive must not drive at speed exceeding 150 and
* steer less than -15 or more than 15.
* expected result: satisfied
*/

#include <assert.h>

// variables in property.
extern int new_speed;
extern int direction_adjustment;

// automata states.
#define S0 0
#define S1 1
int state = S0;

// symboilc variables.
#define c1 (new_speed > 150)
#define c2 (direction_adjustment < 15)
#define c3 (direction_adjustment > -15)

// property checker automata.
void update_prop_automata() {
  if (state == S0) {
    if (c1 && !(c2 && c3)) {
        state = S1;
    }
  } else {
    // when the state is S1...
  }
}

// check property by assert violation with model checking.
void check_property() {
  // update_prop_automata();
  // assert(state != S1);
  assert(!(c1 && !(c2 && c3)));
}