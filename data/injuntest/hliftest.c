#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

#include <stdio.h>

int main() {


	int a = 1;
	int b = 2;
	if(a == b ){
		a = b;
	}else if (a*b==1){
		a = b+3;
	}else {
		a+b = 1;
	}

    return a+b;
}