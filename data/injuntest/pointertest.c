#define TASK(n) void TASK_##n

#include <stdio.h>
#include <stdlib.h>

typedef struct node {
    int data;
    struct node* link;
}node;

node* head = NULL;

void insert_back(int value) {
    node* newnode = (node*)malloc(sizeof(node));
    newnode->data = value;
    newnode->link = NULL;
    if(head  == NULL) {
        head = newnode;
    }
    else {
        node* temp = head;
        while(temp->link) temp = temp->link;
        temp->link = newnode;
    }
}

void insert_where(int value, int target) {
    // ?��?�� data�??? �???�??? node?��?�� newenode�??? ?��?�� 것인�????
    node* newnode = (node*)malloc(sizeof(node));
    node* temp = head;
    newnode->data = value;
    newnode->link = NULL;
    while(temp->data != target) temp = temp->link;
    newnode->link = temp->link;
    temp->link = newnode;
}

void delete_link(int value) {
    // ?��?�� data�??? �???�??? ?���??? delete?��겠다.
    node* temp = head;
    node* remove;
    if(head->data == value) {
        remove = head;
        head = temp->link;
        printf("I will delete %d!\n", remove->data);
        free(remove);
    }
    else {
        while(temp->link->data != value) temp = temp->link;
        remove = temp->link;
        temp->link = remove->link;
        printf("I will delete %d!\n", remove->data);
        free(remove);
    }
}

void print_link() {
    node* temp = head;
    while(temp) {
        printf("%d\n", temp->data);
        temp = temp->link;
    }
}

int main() {
    insert_back(1);
    insert_back(2);
    insert_back(3);
    insert_back(4);
    print_link();
    insert_where(10, 3);
    print_link();
    delete_link(10);
    print_link();
    delete_link(4);
    print_link();
    delete_link(1);
    print_link();
}