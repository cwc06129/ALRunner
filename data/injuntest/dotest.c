#define TASK(n) void TASK_##n

#define TASK(n) TASK_##n

#include<stdio.h>

int main(){
	int a = 0;
	do{
		a++;
		printf("%d\n", a);
	} while(a < 10);
	return a;
}