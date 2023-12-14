#define TASK(n) void TASK_##n
#define FUNC(n, m) k n FUNC_##k

/**
 * 2023-06-12(Mon) SoheeJung
 * vending machine code
*/
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#define MAX_TIME 10

typedef enum _Item { COKE, PEPSI, WATER, JUICE } Item;
typedef enum _Machine_state { Initial, Insert, ButtonEnabled, Emit, Return  } Machine_state;

int item_num = 0;
int item_price = 0;
int amount = 0;
int change = 0;
int price = 0;
Item item = COKE;
int timer = MAX_TIME;
Machine_state machine_state = Initial;
int buttonPress = 0;

int processSelection(int selected_item) {    
    switch (selected_item) {
        case COKE:
            price = 1000;
            break;
        case PEPSI:
            price = 1000;
            break;
        case WATER:
            price = 500;
            break;
        case JUICE:
            price = 2000;
        default:
            break;
    }
    return price;
}

int main() {
    switch(machine_state) {
        case Initial:
            machine_state = Insert;
            break;

        case Insert:
            amount += (rand() % 5) * 500;
            if(amount == 0) {
                timer--;
                if(timer <= 0) {
                    timer = MAX_TIME;
                    machine_state = Initial;
                }
            }
            else {
                machine_state = ButtonEnabled;
                timer = MAX_TIME;
            }
            break;

        case ButtonEnabled:
            if(buttonPress) {
                item_num = rand() % 4;
    
                if(item_num == 0) item = COKE;
				else if(item_num == 1) item = PEPSI;
				else if(item_num == 2) item = WATER;
				else if(item_num == 3) item = JUICE;
				machine_state = Emit;
				timer = MAX_TIME;
            }
            else{
                timer--;
                if(timer <= 0){
                    timer = MAX_TIME;
                    machine_state = Initial;
                }
            }
            break;

        case Emit:
            item_price = processSelection(item);
            machine_state = Return;
            break;
        
        case Return:
            change = amount - item_price;
			machine_state = Initial;
            break;

        default:
            break;
    }
}