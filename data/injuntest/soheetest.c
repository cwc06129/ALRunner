#define TASK(n) void TASK_##n

#define TASK(n) TASK_##n

#include <stdio.h>

int main() { 
    int score = 0;
    int grade = 0;

    if(score >= 90) { // hello
        grade = 1;
        grade++;
        grade--;
        printf("%d\n", grade);
    }
    else if(score >= 80)
    {
        grade = 2;
        printf("%d\n", grade);
    }
    else if(score >= 70) {

        grade = 3;
        printf("%d\n", grade);
    }
    else if(score >= 60);
    else {
        grade = 4;
        printf("%d\n", grade);
    }
    return 0;
}
