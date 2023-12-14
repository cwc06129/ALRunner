#define TASK(n) void TASK_##n

#define TASK(n) TASK_##n

#include <stdio.h>

int main(void) {
    int a = 0;
    int index = 0;

    for(index = 0; index < 100; index++) {
        a++;
    }
    
    for(; index < 90; index++) {
        a++;
    }

    for(index = 1; ; index++) {
        a++;
    }

    for(index = 2; index < 80; ) {
        a++;
    }

    printf("%d", a);

    return 0;
}