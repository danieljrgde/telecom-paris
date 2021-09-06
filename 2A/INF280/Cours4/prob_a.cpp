#include <iostream>
#include <queue>
#include <array>
#include <vector>
#include <utility>
#define MAXN 100001
using namespace std;

int parent[MAXN];

int find(int x){
    if(parent[x] == x){ return x; }
    else{ return find(parent[x]); }
}

int main(void){
    int x, y, refusals;

    while(true){

        refusals = 0;
        int ret = scanf("%d", &x);

        // initialize parent vector
        for(int i=0;i<MAXN;i++){
            parent[i] = i;
        }
        
        if(ret != 1){ break; }

        scanf("%d", &y);
        
        parent[y] = x;

        while(scanf("%d", &x) && x != -1){
            scanf("%d", &y);

            parent[x] = find(x); parent[y] = find(y);

            // combination generates explosion
            if(parent[x] == parent[y]){ refusals++; }

            // if combination does not generate explosion, add it to the truck
            else{
                parent[parent[y]] = x;
            }
        }

        cout << refusals << endl;
    }

    return 0;
}