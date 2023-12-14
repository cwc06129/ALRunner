#define TASK(n) void TASK_##n

#define TASK(n) TASK_##n

struct Test{
	int age;
	int num;
};

int A(int a){
	return a;
}

int B(int a, int b){
	return a + b;
}

#include<stdio.h>
int main(){
	int n1 = 10;
	int n2 = 20;
	int n3 = A(n1);
	int n4 = A(B(n1, n2) + B(n2, n3));
	
	
	
	int * p;
	p = &n1;
	printf("%d", *p);
	
	const int test = 10;
	float fff = 100.0;
	int f2 = (int) fff;
	int a = 0;
	int sz = sizeof(a);
	int k;
	if(a == 0){
		printf("%d", a);
	}
	printf("sadfsafdsadffifififi %d, fjfidsifjoifojs", a);
	int arr[10][10];
	
	TEST:
	
	for (int i = 0; i < 5; i++){
		for (int j = 0; j < 5; ++j){
			arr[i + j][5] = a < b ? (float) a + 3 : (b - 4) * 4;
		}
	}
	goto TEST;
	k = (a > sz) ? a : sz;
	
	int i = 0;
	int j = 0;
	arr[i + j][5] = a < b ? (float) a + 3 : (b - 4) * 4;
	
	printf("%d", arr[1][1]);
	
	struct Test t;
	t.age = 10;
	t -> num = 20;
	
	
	switch(int i){
		case 1:
			a = 10;
			break;
		case 2:
			a = 20;
			break;
	}
	return 0;
}