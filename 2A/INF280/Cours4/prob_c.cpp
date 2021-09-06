#include <iostream>
#include <vector>
#include <utility>
#include <queue>
#include <set>
#define NMAX 101
#define NIL NMAX
#define INF 2147483647
using namespace std;

vector<int> Adj[NMAX]; // Neighbors in Y of nodes in X
vector<int> rows, cols; // Partitions X and Y
unsigned int Dist[NMAX+1]; // Augmenting path lengths

// Matching X-Y and Y-X
int PairRow[NMAX];
int PairCol[NMAX];

// coded with the help of the course's lectures
bool BFS() {
    queue<int> Q;
    Dist[NIL] = INF;
    
    for(auto row : rows) { // start from nodes that are not yet matched
        Dist[row] = (PairRow[row] == NIL) ? 0 : INF;
        if (PairRow[row] == NIL) Q.push(row);
    }

    while (!Q.empty()) { // find all shortest paths to NIL
        int row = Q.front(); Q.pop();
        if (Dist[row] < Dist[NIL]) // can this become a shorter path?
            for (auto col : Adj[row])
                if (Dist[PairCol[col]] == INF) {
                    Dist[PairCol[col]] = Dist[row] + 1; // update path length
                    Q.push(PairCol[col]);
                }
    }
    
    return Dist[NIL] != INF; // any shortest path to NIL found?
}

bool DFS(int row) {
    if (row == NIL) return true; // reached NIL

    for (auto col : Adj[row])
        if (Dist[PairCol[col]] == Dist[row] + 1 &&
            DFS(PairCol[col])) { // follow trace of BFS
            
            PairRow[row] = col; // add edge from x to y to matching
            PairCol[col] = row;
            return true;
        }

    Dist[row] = INF;
    return false; // no augmenting path found
}

int HopcroftKarp() {
    fill_n(PairRow, NMAX, NIL); // initialize: empty matching
    fill_n(PairCol, NMAX, NIL);
    int Matching = 0; // count number of edges in matching

    while (BFS()) { // find all shortest augmenting paths
        for(auto row : rows){ // update matching cardinality
            if (PairRow[row] == NIL && // node not yet in matching?
                DFS(row)) // does an augmenting path start at x?
                
                Matching++;
        }
    }
    
    return Matching;
}

int main(){
    int i, j, k;
    int total_stacks, needed_stacks, h, h_count, max_bipartite;

    while(true){
        
        // initialize vars
        int nrows, ncols;
        int grid[NMAX][NMAX];
        vector<int> max_rows, max_cols, unique_h;
        set<int> h_set;


        // scan col and row sizes
        int ret = scanf("%d", &nrows);
        if(ret != 1) break;
        scanf("%d", &ncols);

        // scan grid
        total_stacks = 0;
        for(i=0; i<nrows; i++){
            for(j=0; j<ncols; j++){
                scanf("%d", &grid[i][j]);
                total_stacks += grid[i][j];
            }
        }

        // find max rows
        for(i=0; i<nrows; i++){
            max_rows.push_back(0);

            for(j=0; j<ncols; j++){
                
                if(grid[i][j] > max_rows.back()){
                    max_rows.pop_back();
                    max_rows.push_back(grid[i][j]);
                }
            }
        }

        // find max cols
        for(j=0; j<ncols; j++){
            max_cols.push_back(0);

            for(i=0; i<nrows; i++){
                
                if(grid[i][j] > max_cols.back()){
                    max_cols.pop_back();
                    max_cols.push_back(grid[i][j]);
                }
            }
        }

        // insert unique max heights to an independent set
        for(auto h : max_rows) h_set.insert(h);
        for (auto h : max_cols) h_set.insert(h);

        // push set elements to vector in increasing order
        for(auto h : h_set) unique_h.push_back(h);

        needed_stacks = 0;
        // loop for every unique height
        for(k=unique_h.size()-1; k>=0; k--){

            // clean vars
            rows.clear();
            cols.clear();
            for(auto& tmp : Adj) tmp.clear();

            // obtain this iteration's height
            h = unique_h[k];

            // count number of stacks with this height
            h_count = 0;
            for(i=0; i<nrows; i++){
                for(j=0; j<ncols; j++){
                    if(grid[i][j] == h) h_count++;
                }
            }

            // build the adjacency matrix for this height
            for(i=0; i<nrows; i++){
                if(max_rows[i] == h) rows.push_back(i);
            }

            for(j=0; j<ncols; j++){
                if(max_cols[j] == h) cols.push_back(j);
            }

            // create graph connection between row and col in every position where its possible to place h stacks
            for(auto row : rows){
                for(auto col : cols){
                    if(grid[row][col] != 0) Adj[row].push_back(col);
                }
            }

            // calculate max bipartite
            max_bipartite = HopcroftKarp();

            // calculate number of needed stacks using minimum stacks possible
            needed_stacks += h*(rows.size() + cols.size() - max_bipartite);
            
            // calculate needed number of stacks to set to 1
            if(h != 0) needed_stacks += h_count - (rows.size() + cols.size() - max_bipartite);
        }

        // calculate needed number of stacks to set to 1 that are not maximal in their rows/columns
        for(i=0; i<nrows; i++){
            for(j=0; j<ncols; j++){
                if(!h_set.count(grid[i][j]) && grid[i][j] != 0){
                    needed_stacks++;
                }
            }
        }

        // print total number of stacks
        cout << total_stacks - needed_stacks << endl;
    }

    return 0;
}