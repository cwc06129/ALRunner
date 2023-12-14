/*
* There is no vehicle in front that is in the center
* and the distance is greater than 0 and less than 50.
* expected result: violated
*/
#include <assert.h>

// variables in property.
extern int acq_size;
extern int x;

// automata states.
#define S0 0
#define S1 1
int state = S0;

// symboilc variables.
#define c1 (acq_size < 50)
#define c2 (acq_size > 0)
#define c3 (x == 83)

// property checker automata.
void update_prop_automata() {
  if (state == S0) {
    if (c1 && c2 && c3) {
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
  assert(!(c1 && c2 && c3));
}