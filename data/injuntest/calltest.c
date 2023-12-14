#define TASK(n) void TASK_##n

#define TASK(n) TASK_##n

#include <stdio.h>

int a = 1;

void A(){
	// hello
	/*
	*
	*/
	
	printf("A1");
	printf("A2");
	if (a == 1){
		printf("A3");
		B();
	}
	else{
		D();
	}
	printf("A4");
	printf("A5");
}

void B(){
	printf("B1");
	printf("B2");
	B();
	C();
	printf("B3");
	printf("B4");
}

void C(){
	printf("C1");
	printf("C2");
	B();
	printf("C3");
	printf("C4");
	printf("C5");
}

void D(){
	B();
	printf("D1");
	printf("D2");
	printf("D3");
}


int main(void)
{
	printf("Hello SSELAB\n");
	B();
	printf("Bye SSELAB\n");
	
    return 0;
}