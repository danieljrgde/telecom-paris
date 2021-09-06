#include <stdio.h>

#define PI 3.14159265358979323846
#define EPS 0.0000000001

double absolute_value(double x){
    if(x>=0) return x;
    else return -x; 
}

double min(double x, double y){
    if(x <= y){
        return x;
    }

    else return y;
}

double max(double x, double y){
    if(x >= y){
        return x;
    }

    else return y;
}

int main(void){
    
    int num_holes, num_slices, i, j;
    double volume, r_cube, slice_vol_target, slice_vol;
    double start_z, s, z, e;
    double R, hole_z, a, b, hole_vol;

    while(1){
        
        scanf("%d", &num_holes);
        int ret = scanf("%d", &num_slices);

        if(ret != 1){
            break;    
        }

        double holes[num_holes][4];
        
        volume = 100*100*100;

        for(i=0; i<num_holes; i++){
            scanf("%lf", &holes[i][0]);
            scanf("%lf", &holes[i][1]);
            scanf("%lf", &holes[i][2]);
            scanf("%lf", &holes[i][3]);

            holes[i][0] = holes[i][0]/(double)(1000);
            holes[i][1] = holes[i][1]/(double)(1000);
            holes[i][2] = holes[i][2]/(double)(1000);
            holes[i][3] = holes[i][3]/(double)(1000);

            r_cube = (double)(holes[i][0]*holes[i][0]*holes[i][0]);
            volume = volume - (double)4/(double)3*PI*r_cube;
        }

        slice_vol_target = volume/ (double)num_slices;

        start_z = 0;
        for(i=0; i<num_slices; i++){

            e = 100;
            z = e;
            s = start_z;

            slice_vol = volume;
            while(absolute_value(e - s) > EPS){

                z = (e+s)/(double)2;
                slice_vol = (double)(100*100)*(z - start_z);

                for(j=0; j<num_holes; j++){
                    R = holes[j][0];
                    hole_z = holes[j][3];

                    if((hole_z > (start_z - R)) && hole_z < (z + R)){
                        
                        a = max(start_z, hole_z - R) - hole_z;
                        b = min(z, hole_z + R) - hole_z;

                        hole_vol = PI*R*R*(b-a) - PI/(double)(3)*(b*b*b - a*a*a);
                        slice_vol = slice_vol - hole_vol;
                    }
                }

                if(slice_vol > slice_vol_target){
                    e = z; 
                }

                else{
                    s = z;
                }
            }

            printf("%.9lf\n", (z - start_z));
    
            start_z = z;
        }
    }

    return 0;
}