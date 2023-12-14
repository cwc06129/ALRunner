#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

#include <stdio.h>
#include <stdlib.h>

int n1;
int n2;
int result;

void main()
{
    n1 = rand() % 10;
    n2 = rand() % 10;

    while(n1!=n2)
    {
        if(n1 > n2)
            n1 -= n2;
        else
            n2 -= n1;
    }
    result = n1;
}