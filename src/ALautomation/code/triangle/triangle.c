#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

#define triangleaSK(n) void triangleaSK_##n
#define FUNC(n, m) k n FUNC_##k

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

int trianglea;
int triangleb;
int trianglec;
int match = 0;
int result = -1;

int triangle() {
    if(trianglea==triangleb) match=match+1;
    if(trianglea==trianglec) match=match+2;
    if(triangleb==trianglec) match=match+3;
    if(match==0) {
        if(trianglea+triangleb <= trianglec) result=2;
        else if(triangleb+trianglec <= trianglea) result=2;
        else if(trianglea+trianglec <= triangleb) result =2;
        else result=3;
    } else {
        if(match == 1) {
            if(trianglea+triangleb <= trianglec) result =2;
            else result=1;
        } else {
            if(match ==2) {
                if(trianglea+trianglec <=triangleb) result = 2;
                else result=1;
            } else {
                if(match==3) {
                    if(triangleb+trianglec <= trianglea) result=2;
                    else result=1;
                } else result = 0;
            } }}

    // 0: equilateral, 1:isoscele, 2:non-triangle, 3:scalene 
}