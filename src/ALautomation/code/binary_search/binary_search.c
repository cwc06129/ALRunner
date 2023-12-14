#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

// C program to implement recursive Binary Search
#include <stdio.h>
#include <time.h>
#include <stdlib.h>

int arr[5];
int key = 0;
int result = -1;

// A recursive binary search function. It returns
// location of x in given array arr[l..r] is present,
// otherwise -1
int binarySearch(int l, int r, int x)
{
	if (r >= l) {
		int mid = l + (r - l) / 2;

		// If the element is present at the middle
		// itself
		if (arr[mid] == x)
			return mid;

		// If element is smaller than mid, then
		// it can only be present in left subarray
		if (arr[mid] > x)
			return binarySearch(l, mid - 1, x);

		// Else the element can only be present
		// in right subarray
		return binarySearch(mid + 1, r, x);
	}

	// We reach here when element is not
	// present in array
	return -1;
}

int main(void)
{   
    for(int i = 0; i < 5; i++) {
        arr[i] = rand() % 5;
    }
	result = binarySearch(0, 4, key);
}