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

int main()
{
    struct Person p1;

    strcpy(p1.name, "Tom");
    p1.age = 30;
    strcpy(p1.address, "Seoul, Korea");

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