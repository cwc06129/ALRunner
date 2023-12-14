#define TASK(n) void TASK_##n

#define TASK(n) TASK_##n

int main() 
{ 
   int x = 2; 
   switch (x) 
   { 
       case 1: printf("Choice is 1"); 
               break; 
       case 2: printf("Choice is 2"); 
                break; 
       case 3: printf("Choice is 3"); 
               break; 
       default: printf("Choice other than 1, 2 and 3"); 
                break;   
   } 
   return 0; 
}  


// void test(void){
// 	test2((struct module *)1);
// LDV_STOP:
// 	goto LDV_STOP;
// }