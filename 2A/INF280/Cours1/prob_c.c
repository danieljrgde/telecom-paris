#include <stdio.h>

int main(void){

    int P, Q, i, j, current, emergency, emergency_idx, next;
    char command;

    int count = 1;
    while(1){

        scanf("%d", &P);
        int ret = scanf("%d", &Q);

        if(ret != 1){
            break;
        }

        if(P == 0 && Q == 0){
            break;
        }

        int queue[Q];

        for(i=0; i<Q; i++){
            if(i<P){
                queue[i] = i+1;
            }
            
            else{
                queue[i] = -1;
            }
        }

        printf("Case %d:\n", count);
        for(i=0; i<Q; i++){
            scanf(" %c", &command);

            if(command == 'N'){

                printf("%d\n", queue[0]);

                for(j=0; j<Q-1 && j<P-1; j++){
                    current = queue[j];
                    next = queue[j+1];

                    queue[j] = next;
                    queue[j+1] = current;
                }
            }

            else if(command == 'E'){
                scanf("%d", &emergency);

                emergency_idx = -1;
                for(j=0; j<Q; j++){
                    if(queue[j] == emergency){
                        emergency_idx = j;
                    }
                }

                if(emergency_idx == -1){
                    for(j=Q-1;j>0;j--){
                        queue[j] = queue[j-1];
                    }

                    queue[0] = emergency;
                }

                else{
                    for(j=emergency_idx; j>0; j--){
                        current = queue[j];
                        next = queue[j-1];

                        queue[j-1] = current;
                        queue[j] = next;
                    }
                }
            }
        }

        count++;
    }

    return 0;
}