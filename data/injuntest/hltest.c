#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

#include <stdio.h>

int main() {

	A:
	continue;
	return;
	exit(0);
	goto A;
	int a = 1;
	int b = 2;
	int f = 1;
	if(a == b ){
		if (b - a == 0){
			a = b+1;
		}else if (f==1){
			f=9;
		}
	}else if (a*b==1){
		a = b+3;
	}else {
		a+b = 1;
	}

	for (i=0 ; i<10 ; i++){
		for (int j=0 ; j<10 ; j++){
			a+b = 1;
			break;
			for (int k=0 ; k<10 ; k++){
				continue;
			}
		}
		v ++;
	}


    return a+b;
	
}
