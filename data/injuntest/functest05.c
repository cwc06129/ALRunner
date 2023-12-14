#define TASK(n) void TASK_##n

#define TASK(n) TASK_##n

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct employee{
	char name[30];
	int age;
};
typedef struct employee DATA;

struct node{
	struct employee data;
	struct node* next;
};
typedef struct node NODE;

NODE* insert(NODE* head);
int store(FILE* f, char* fname, NODE* head);
int printList(NODE*);
int fprint(char* fname);
NODE* createNode(char* name, int age);
NODE* append(NODE* head, NODE* newNode);

int main(){
	FILE* f = NULL;
	char fname[] = "employee.bin";
	NODE *head = NULL;
	int op = 0;

	fprint(fname, &head);

	while (1) {
		scanf("%d", &op);

		if (op == 1)
			head = insert(head);
		else if (op == 2)
			store(f, fname, head);
		else if (op == 3)
			break;
	}
	return 0;
}

int fprint(char* fname, NODE ** head){
	FILE* f;
	DATA D1;
	NODE* newNode = NULL;
	NODE* tail = NULL;
	int i, count = 0;
	*head = NULL;


	if ((f = fopen(fname, "rb+")) == NULL) {
		printf(";\n");
		return;
	}
	rewind(f);

	for (i = 0;; i++){
		count = fread(&D1, sizeof(DATA), 1, f);
		if ((i == 0) && (count == 0)){
			printf(";\n");
			break;
		}
		if (count != 0)
		{
			fprintf(stdout, "<%s, %d>;\n", D1.name, D1.age);
			
			newNode = (NODE*)malloc(sizeof(NODE));
			
			strcpy(newNode->data.name, D1.name);
			newNode->next = NULL;
			newNode->data.age = D1.age;
			
			if ((*head == NULL) && (tail == NULL)){
				*head = tail = newNode;
			}
			else {
				tail->next = newNode;
				tail = newNode;
			}
		}
		if (feof(f)) break;
	}
	fclose(f);
}
NODE* insert(NODE* head){
	DATA person;
	NODE *newNode;
	while (1){
		
		if (scanf("%s %d", person.name, &person.age) == EOF) break;
		
		newNode = createNode(person.name, person.age);
		if (newNode == NULL) exit(1);
		
		head = append(head, newNode);
	}
	printList(head);
	return head;
}
NODE* createNode(char* name, int age){
	NODE *newNode;
	
	newNode = (NODE*)malloc(sizeof(NODE));
	if (newNode == NULL) {
		return NULL;
	}
	
	strcpy(newNode->data.name, name);
	newNode->data.age = age;
	newNode->next = NULL;

	return newNode;
}
NODE* append(NODE* head, NODE* newNode){
	NODE *nextNode = head;
	NODE *precur = NULL;
	NODE *cur = NULL;

	if (head == NULL) {
		head = newNode;
		return head;
	}
	cur = head;
	while (1) {
		if (cur == NULL){
			newNode->next = cur;
			precur->next = newNode;
			break;
		}
		if (strcmp(cur->data.name, newNode->data.name) > 0){
			if (precur == NULL){
				newNode->next = cur;
				head = newNode;
				break;
			}
			else {
				newNode->next = cur;
				precur->next = newNode;
				break;
			}
		}
		precur = cur;
		cur = cur->next;
	}
	return head;
}
int store(FILE* f, char* fname, NODE* head){
	NODE* nextNode = head;
	DATA D1;
	int cnt = 0; 
	if(head == null) return 0;
	if ((f = fopen(fname, "wb")) == NULL) {

		exit(1);
	}
	while (nextNode){
		
		strcpy(D1.name, nextNode->data.name);
		D1.age = nextNode->data.age;
		cnt = fwrite(&D1, sizeof(DATA), 1, f);
		nextNode = nextNode->next;
	}
	fclose(f);
}
int printList(NODE *head)
{
	int cnt = 0;
	NODE *nextNode = head;

	while (nextNode != NULL) {
		printf("<%s %d>", nextNode->data.name, nextNode->data.age);
		nextNode = nextNode->next;
		if (nextNode)
			printf(";");
	}
	printf("\n");
	return cnt;
}