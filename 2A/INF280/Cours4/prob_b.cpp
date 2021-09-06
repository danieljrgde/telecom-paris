#include <iostream>
#include <stack>
#include <array>
#include <vector>
#include <set>
#include <utility>
#define NCOLORS 51
using namespace std;

int deg[NCOLORS];
int G[NCOLORS][NCOLORS];

// coded with the help of geeksforgeeks.org/implementation-of-dfs-using-adjacency-matrix/
void dfs(int start){
    int i;

    for(i=1; i<NCOLORS; i++){
        if(G[start][i]){ // if we have a bead with these colors
            // remove bead
            G[start][i]--;
            G[i][start]--;

            // dfs from next color recursively (cycle through all beads until all have been used)
            dfs(i);

            // print last bead obtained after going full cycle
            cout << i << " " << start << endl;
        }
    }
}

int main(void){
    int T, N;
    int u, v, num_cases;
    int i, j;

    scanf("%d", &T);

    num_cases = 0;

    while(num_cases != T){

        // zero out variables
        for(i=1; i<NCOLORS; i++){
            for(j=1; j<NCOLORS; j++){
                G[i][j] = 0;
            }
        }
        fill_n(deg, NCOLORS, 0);

        // start algorithm
        num_cases++;
        cout << "Case #" << num_cases << endl;

        scanf("%d", &N);
        //cout << "num beads(N): " << N << endl;

        for(i=1; i<=N; i++){
            scanf("%d%d", &u, &v);

            // increase degree of each color
            deg[u]++;
            deg[v]++;

            // insert bead connection into graph matrix
            G[u][v]++;
            G[v][u]++;
        }

        // if we don't have eulerian circuit, print that beads are missing
        bool flag = true;
        for(i=1; i<NCOLORS; i++){
            if(deg[i] % 2 != 0){
                cout << "some beads may be lost\n" << endl;
                flag = false;
                break;
            }
        }

        // since we have eulerian circuit, just run dfs from first available color
        if(flag){
            
            // find first available color
            for(i=1; i<NCOLORS; i++){
                if(deg[i]){ break; }
            }

            // run dfs starting from node with max degree
            dfs(i);
            cout << endl;
        }
    }

    return 0;
}