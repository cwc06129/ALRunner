/*
* Automotive must go forwared in 100 clocks
* if the distance of the vehicle in front is greater or equal to 84.
* expected result: satisfied
*/

#include <assert.h>

// variables in property.
extern int ctl_size;
extern int new_speed;

// symboilc variables.
#define c1 (ctl_size >= 84)
#define c2 (new_speed < 0)

// automata states.
#define S0 0
#define S1 1
#define S2 2
int state = S0;
int counter = 0;

// property checker automata.
void update_prop_automata() {
  if (state == S0) {
    counter = 0;
    if (c1) state = S1;
  } else if (state == S1) {
    if (c2) state = S0;
    else if (counter >= 100) state = S2;
    counter = counter + 1;
  } else {
    // when the state is S2...
  }
}

// check property by assert violation with model checking.
void check_property() {
  update_prop_automata();
  assert(state != S2);
}