#define TASK(n) void TASK_##n

#define TASK(n) TASK_##n

#include<stdio.h>

int main(){
	int i;
	int a = 0;
	for(i = 0; i < 6; i++){
		a++;
		printf("%d", a);
	}
	return 0;
}