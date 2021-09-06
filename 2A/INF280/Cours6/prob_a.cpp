#include <iostream>
#include <utility>
#include <cmath>
#define NMAX 1001
#define EPS 1e-7
using namespace std;

// struct to save a point's x and y coordinates
struct point{
    double x, y;
};

// calculates distances between two points
double dist(point a, point b){
    double dist = sqrt(pow(a.x - b.x, 2) + pow(a.y - b.y, 2));
    return dist;
}

// calculates angle <AOB
double center_angle(point a, point b, point c){
    double d1, d2, d3, angle_acb;

    d1 = dist(a, b);
    d2 = dist(b, c);
    d3 = dist(c, a);

    angle_acb = acos((d2*d2 + d3*d3 - d1*d1)/(2*d2*d3));
    return (double)(180)/M_PI*(2*angle_acb);
}

int main(void){
    point p1, p2, p3;
    double angle_aob, angle_boc, angle_coa;
    double remainder_aob, remainder_boc, remainder_coa;

    // scan inputs
    while(scanf("%lf %lf %lf %lf %lf %lf", &p1.x, &p1.y, &p2.x, &p2.y, &p3.x, &p3.y) == 6){

        // calculate center angles
        angle_aob = center_angle(p1, p2, p3);
        angle_boc = center_angle(p2, p3, p1);
        angle_coa = center_angle(p3, p1, p2);

        // calculates the least common multiple for the share of 360 that each center angle represents
        for(int n=3; n<NMAX; n++){

            // calculate float remainder
            remainder_aob = fabs(angle_aob/(360.0/n) - round(angle_aob/(360.0/n)));
            remainder_boc = fabs(angle_boc/(360.0/n) - round(angle_boc/(360.0/n)));
            remainder_coa = fabs(angle_coa/(360.0/n) - round(angle_coa/(360.0/n)));

            // if the remainder is zero for all angles, then n is the smallest regular convex polygon possible
            if( (remainder_aob <= EPS || fabs(remainder_aob - angle_aob) <= EPS) &&
                (remainder_boc <= EPS || fabs(remainder_boc - angle_boc) <= EPS) &&
                (remainder_coa <= EPS || fabs(remainder_coa - angle_coa) <= EPS)){

                cout << n << endl;
                break;
            }
        }
    }

    return 0;
}