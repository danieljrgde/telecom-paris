#include <stdio.h>
#include <math.h>
#define N_MAX 6
#define PERM_MAX 720
#define INF 10000000000
#define PI 3.14159265358979323846

int indexes[PERM_MAX][N_MAX];
int num_permutations = 0;

typedef struct{
    double x, y, z, r;
}balloon;

double min(double x, double y){
    if(x <= y){ return x; }
    else{ return y; }
}

double max(double x, double y){
    if(x >= y){ return x; }
    else{ return y; }
}

double volume(double r){
    return (double)4/(double)3*PI*r*r*r;
}

/* PERMUTATION FUNCTIONS BUILT WITH THE HELP OF www.geeksforgeeks.org/write-a-c-program-to-print-all-permutations-of-a-given-string */
void swap(int *x, int *y) { 
    int temp; 
    temp = *x; 
    *x = *y; 
    *y = temp; 
}

void permute(int *a, int l, int r) { 
    int i; 
    
    if(l == r){ 
        for(i=0; i<=r; i++) {
            indexes[num_permutations][i] = a[i];
        }

        num_permutations++;
    }

    else{ 
        for (i = l; i <= r; i++) { 
            swap((a+l), (a+i)); 
            permute(a, l+1, r); 
            swap((a+l), (a+i));
        }
    }
}
/*----------------------------------------------------------------------------------------------------------------------------------*/

int main(void){
    int n, index[N_MAX];
    double xi, yi, zi, xf, yf, zf;
    double box_max, x_max, y_max, z_max, balloons_max, balloons_max_j, max_radius;
    balloon balloons[N_MAX];
    int num_box, i, j, k;

    num_box = 1;
    while(1){
        
        scanf("%d", &n);

        if(n == 0){ break; }

        scanf("%lf%lf%lf", &xi, &yi, &zi);
        scanf("%lf%lf%lf", &xf, &yf, &zf);

        for(i=0; i<n; i++){
            scanf("%lf%lf%lf", &balloons[i].x, &balloons[i].y, &balloons[i].z);
            index[i] = i;
        }

        double box_vol = fabs(xi-xf)*fabs(yi-yf)*fabs(zi-zf);

        num_permutations = 0;
        permute(index, 0, n-1);

        double balloons_max_vol = 0;
        for(k=0; k<num_permutations; k++){

            double balloons_vol_k = 0;
            for(i=0; i<n; i++){

                x_max = min(fabs(xi - balloons[indexes[k][i]].x), fabs(xf - balloons[indexes[k][i]].x));
                y_max = min(fabs(yi - balloons[indexes[k][i]].y), fabs(yf - balloons[indexes[k][i]].y));
                z_max = min(fabs(zi - balloons[indexes[k][i]].z), fabs(zf - balloons[indexes[k][i]].z));

                box_max = min(x_max, min(y_max, z_max));

                balloons_max = INF;
                for(j=i-1; j>=0; j--){
                    
                    balloons_max_j = sqrt(pow(balloons[indexes[k][i]].x - balloons[indexes[k][j]].x, 2) +
                                          pow(balloons[indexes[k][i]].y - balloons[indexes[k][j]].y, 2) +
                                          pow(balloons[indexes[k][i]].z - balloons[indexes[k][j]].z, 2)) - balloons[indexes[k][j]].r;

                    if(balloons_max_j < 0){ balloons_max_j = 0; }

                    balloons_max = min(balloons_max, balloons_max_j);
                }

                max_radius = min(box_max, balloons_max);
                balloons[indexes[k][i]].r = max_radius;
                balloons_vol_k += volume(balloons[indexes[k][i]].r);
            }

            balloons_max_vol = max(balloons_max_vol, balloons_vol_k);
        }

        printf("Box %d: %d\n\n", num_box, lround(box_vol-balloons_max_vol));

        num_box++;
    }

    return 0;
}