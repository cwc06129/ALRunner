#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

#define TASK(n) TASK_##n

#include <stdio.h>

int main(){
    int a = 0;

	while (1){
		a++;
        if (a == 17) exit(1);
	}
    a = 155;
	return head;
}