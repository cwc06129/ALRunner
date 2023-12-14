/*
* Automotive can not recognize a vehicle in front.
* expected result: violated
*/

#include <assert.h>

// variables in property.
extern int acq_size;

// automata states.
#define S0 0
#define S1 1
int state = S0;

// symboilc variables.
#define c1 (acq_size == 0)

// property checker automata.
void update_prop_automata() {
  if (state == S0) {
    if (!c1) {
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
  assert(c1);
}