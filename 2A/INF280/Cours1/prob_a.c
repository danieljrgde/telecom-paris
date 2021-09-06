#include <stdio.h>

int main(void){

    int i, tea, guess, count;

    while(1){
        count = 0;

        int ret = scanf("%d", &tea);

        for(i=0; i<5; i++){
            int ret = scanf("%d", &guess);

            if(guess == tea){
                count++;
            }
        }

        if(ret != 1){
            break;    
        }

        printf("%d\n", count);
    }
    
    return 0;
}
