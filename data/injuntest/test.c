#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

#include <stdio.h>

void odd_even(int num) {
    for(int i = 1; i <= num, i++) {
        if(i % 2 == 0) {
            printf("odd number : %d\n", i);
        } else {
            printf("even number : %d\n", i);
        }   
    }
}