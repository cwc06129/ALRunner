#define TASK(n) void TASK_##n

#define TASK(n) TASK_##n

#include<stdio.h>

void A(){
	return B(1);
}

double B(int a){
	return a*3.0;
}

int main(){
	printf("The answer is %d\n", A());
	return 0;
}