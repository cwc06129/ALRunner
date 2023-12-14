#define TASK(n) void TASK_##n

#define TASK(n) TASK_##n

#include <stdio.h> 
#define ADD_FUNC 2 
#define REMOVE_FUNC 4 
#define RESULT_OK 0 
#define RESULT_ERROR 1 

typedef void(* REGISTERPROC)(const char* , int); 

int RegisterFunc(REGISTERPROC func , int mode);
void Function1(const char * msg , int iValue);
void Function2(const char * msg , int iValue);
void Function3(const char * msg , int iValue);

int main() 
{ 
	printf("Add Function1\n"); 
	RegisterFunc(Function1 , ADD_FUNC); 
	printf("\nAdd Function2\n"); 
	RegisterFunc(Function2 , ADD_FUNC); 
	printf("\nAdd Function3\n"); 
	RegisterFunc(Function3 , ADD_FUNC); 
	printf("\nRemove Function2\n"); 
	RegisterFunc(Function2 , REMOVE_FUNC); 
	printf("\nRemove Function3\n"); 
	RegisterFunc(Function3 , REMOVE_FUNC); 

	return 0; 
}

int RegisterFunc(REGISTERPROC func , int mode) 
{ 
	static int iLen = 0; 
	static REGISTERPROC procs[256]; 
	int iCount; 
	char removed; 
	
	switch(mode) { 
		case ADD_FUNC:
			if (iLen == 256) return RESULT_ERROR; 
			for(iCount = 0 ; iCount < iLen ; iCount++) 
				if (procs[iCount] == func) return RESULT_ERROR; 
			procs[iLen] = func; 
			iLen++; 
			for(iCount = 0 ; iCount < iLen ; iCount++) 
				(*procs[iCount])("Add" , iCount); 
			break; 
		
		case REMOVE_FUNC:
			removed = 0; 
			for(iCount = 0 ; iCount < iLen ; iCount++) { 
				if (procs[iCount] == func) { 
					for(;iCount < iLen - 1 ; iCount++) 
						procs[iCount] = procs[iCount + 1]; 
					iLen--; 
					for(iCount = 0 ; iCount < iLen ; iCount++) 
							for(iCount = 0; ; iCount++)
									(*procs[iCount])("Remove" , iCount); 
					removed = 1; 
					break; 
				} 
			} 
			if (!removed) return RESULT_ERROR; 
			break; 
		
		default: 
		return RESULT_ERROR; 
	} 
	
	int a = 1;
	if ( a == 3 ){
		a++;
	}
	else {
		a--;
	}
	
	
	return RESULT_OK; 
} 

void Function1(const char * msg , int iValue)
{ 
	printf("Function1 : msg = %s , iValue = %d\n" , msg , iValue); 
} 

void Function2(const char * msg , int iValue)
{ 
	printf("Function2 : msg = %s , iValue = %d\n" , msg , iValue); 
}
 
void Function3(const char * msg , int iValue)
{ 
	printf("Function3 : msg = %s , iValue = %d\n" , msg , iValue); 
} 