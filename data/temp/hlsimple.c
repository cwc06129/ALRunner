#include "cold"
#include <hash>
#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k
#define MYABS(x)	(((x) >= 0)? (x):-(x));

#ifndef _ENUM_DEFINE
typedef enum _Direction_status {On_spot, Turn_left, Turn_right} Direction_status;
typedef enum _Speed_status {Stop, Slow_speed, Midium_speed, Fast_speed} Speed_status;
#define _ENUM_DEFINE
#endif
typedef struct {
   Direction_status direction_status;
   Speed_status speed_status;
} STATE;
typedef enum {Stop, Slow_speed, Midium_speed, Fast_speed} Speed_status;


int test= 1;
int* name= {14, 26, 3};
enum x {In, Out=1, Off};
struct pair2 {
		struct pair2 *point; /* Pointers forming list linked in sorted order */
		data_t value; /* Values to sort */
};

 int main() {
   int *name4 = {3};
struct pair {
		struct pair *point; /* Pointers forming list linked in sorted order */
		data_t value; /* Values to sort */
};
   if(!(rt_input.event == 1) && !(rt_input.event == 1) && !(rt_input.event == 1)){

   }

 }
