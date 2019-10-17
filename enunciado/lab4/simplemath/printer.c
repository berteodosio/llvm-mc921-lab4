#include <stdio.h>
#include <stdlib.h>

extern int sm_main;
extern int batata();

void printVariableValue() {
//    printf("%d\n", sm_main);
    printf("SM_main value is %d\n", sm_main);
}

int main() {
    batata();       // initializes sm_main
    printVariableValue();
    return 0;
}