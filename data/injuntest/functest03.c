#define TASK(n) void TASK_##n

#define TASK(n) TASK_##n

#include <stdio.h>
#include <string.h>

struct Person {
    char name[20];
    int age;
    char address[100];
};

void printPerson(struct Person p);
void updatePerson(struct Person* p, int new_age);
struct Person makePerson(char name[], int age, char address[]);

int main()
{
    struct Person p1;
	char name[20] = "Gloria";
	char address[100] = "Seoul, Korea";
	
	p1 = makePerson(name, 30, address);

    printPerson(p1);

	updatePerson(&p1, 40);
	
    return 0;
}

void printPerson(struct Person p)
{
    printf("name: %s\n", p.name); 
    printf("age: %d\n", p.age);
    printf("address: %s\n", p.address);
}

void updatePerson(struct Person* p, int new_age)
{
	p->age = new_age;
}

struct Person makePerson(char name[], int age, char address[])
{
	struct Person p;
	
	strcpy(p1.name, name);
	p1.age = age;
	strcpy(p1.address, address);
	
	return p;
}