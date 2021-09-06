#include <stdio.h>
#include <math.h>
#define N_MAX 100001
#define INF 2147483647

int min(int x, int y){
    if(x <= y) return x;
    else return y;
}

int main(void){

    long int num_datasets, C, N, round_trip_dist;
    long int x[N_MAX], y[N_MAX], w[N_MAX];
    long int total_w[N_MAX], total_d[N_MAX], min_moves[N_MAX]; 
    long int k, i, j;
   
    scanf("%ld", &num_datasets);

    for(k=1; k <= num_datasets; k++){
        scanf("%ld%ld", &C, &N);

        x[0] = 0; y[0] = 0;
        total_w[0] = 0; total_d[0] = 0;
        for(i=1; i<=N; i++){
            scanf("%ld%ld%ld", &x[i], &y[i], &w[i]);

            total_w[i] = total_w[i-1] + w[i];
            total_d[i] = total_d[i-1] + abs(x[i]-x[i-1]) + abs(y[i]-y[i-1]);
        }


        min_moves[0] = 0;
        for(i=1; i<=N; i++){
            min_moves[i] = INF;
            for(j=i; j>=1; --j){

                if(total_w[i]-total_w[j-1] > C){
                    break;
                }

                round_trip_dist = x[i] + y[i] + x[j] + y[j] + total_d[i] - total_d[j];

                min_moves[i] = min(min_moves[i], min_moves[j-1] + round_trip_dist);
            }
        }

        printf("%ld\n", min_moves[N]);

        if(k < num_datasets) printf("\n");
    }

    return 0;
}