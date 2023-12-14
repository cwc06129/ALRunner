#define TASK(n) void TASK_##n

#define TASK(n) TASK_##n

#include <stdio.h>

int main() {
    char string = "hello";
    printf("%s", string);
    printf("hello\n");
    return string;
}