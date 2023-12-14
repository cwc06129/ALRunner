#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

#include <stdio.h>
#include <stdlib.h>
#include <math.h>  // This is needed to use sqrt() function

int calca, calcb, calcc, determinant;
float r1, r2, real, imag;
int result; // 실근이 몇개인가?

void main()
{
    calca = rand() % 10;
    calcb = rand() % 10;
    calcc = rand() % 10;

    /*
        mathematical formula to know the 
        nature of the roots
    */
    determinant = calcb*calcb - 4*calca*calcc; 

    if(determinant > 0)    // both roots are real
    {
        r1 = (-calcb + sqrt(determinant))/2*calca;  // Brackets are important
        r2 = (-calcb - sqrt(determinant))/2*calca;
        result = 2;
    }
    else if(determinant == 0)   // both roots are real and equal
    {
        r1 = r2 = -calcb/(2*calca); // brackets are important
        result = 1;
    }
    /*
        Determinant < 0 - both roots are imaginary of the 
        form real + i*imaginary
    */
    else
    {
        real = -calcb/(2*calca);
        imag = sqrt(-determinant)/(2*calca);
        result = 0;
    }
}