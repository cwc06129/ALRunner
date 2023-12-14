/*
* Automotive must not drive at speed exceeding 70 and then
* suddenly stop at speed less than 10.
* expected result: satisfied
*/

#include <assert.h>

// variables in property.
extern int new_speed;

// automata states.
#define S0 0
#define S1 1
#define S2 2
int state = S0;

// symboilc variables.
#define c1 (new_speed > 70)
#define c2 (new_speed < 10)

// property checker automata.
void update_prop_automata() {
  if (state == S0) {
    if (c1) state = S1;
  } else if (state == S1) {
    if (c2) state = S2;
    else if (c1) state = S1;
    else state = S0;
  } else {
    // when the state is S2...
  }
}

// check property by assert violation with model checking.
void check_property() {
  update_prop_automata();
  assert(state != S2);
}
