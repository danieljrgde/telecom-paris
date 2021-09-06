#include <stdio.h>

int main(void){
    
    int W, w, L, l, A, N, i;

    while(1){
        A = 0;

        int ret = scanf("%d", &W);

        if(ret != 1){
            break;    
        }

        scanf("%d", &N);

        for(i=0; i<N; i++){
            scanf("%d", &w);
            scanf("%d", &l);
            A += l*w;
        }

        L = A/W;
        printf("%d\n", L);
    }

    return 0;
}
