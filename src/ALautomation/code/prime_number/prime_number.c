#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

#include <stdio.h>

int n = 0;
int result = 0;

int main() {
  int i, flag = 0;
  scanf("%d", &n);

  // 0 and 1 are not prime numbers
  // change flag to 1 for non-prime number
  if (n == 0 || n == 1)
    flag = 1;

  for (i = 2; i <= n / 2; ++i) {

    // if n is divisible by i, then n is not prime
    // change flag to 1 for non-prime number
    if (n % i == 0) {
      flag = 1;
      break;
    }
  }

  // flag is 0 for prime numbers
  if (flag == 0)
    result = 1;
  else
    result = 0;
}