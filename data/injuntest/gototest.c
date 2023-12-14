#define TASK(n) void TASK_##n

#define TASK(n) TASK_##n

#include<stdio.h>
int main( ){
	A:
	printf("hello world!");
	goto A;
	return 0;
}