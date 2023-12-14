/*
* Automotive must go backward
* if the distance of the vehicle in front is greater or equal to 84.
* expected result: satisfied
*/

#include <assert.h>

// variables in property.
extern int ctl_size;
extern int new_speed;

// automata states.
#define S0 0
#define S1 1
int state = S0;

// symboilc variables.
#define c1 (ctl_size>=84)
#define c2 (new_speed<0)

// property checker automata.
void update_prop_automata() {
  if (state == S0) {
    if (!(!c1 || c2)) {
      state = S1;
    }
  } else {
    // when state is S1...
  }
}

// check property by assert violation with model checking.
void check_property() {
  // update_prop_automata();
  // assert(state != S1);
  assert(!c1 || c2);
}