#define TASK(n) void TASK_##n

#define TASK(n) TASK_##n

#include <stdio.h>

int multiple(int a, int b) {
    c = 10;
    d = 76;
    return add(d-c, a);
}

int add(int a, int b) {
    return a+b;
}

int sub(int a, int b) {
    return a-b;
}

int div(int a, int b) {
    return a/b;
}

int main() {
    int a = 10;
    int b = 5;
    add(a, b);
    multiple(a, 5);
    sub(a, b);
    div(a, b);
    
    return 0;
}