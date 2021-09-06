#include <iostream>
#include <queue>
#include <array>
#include <vector>
#include <utility>
#define MAXN 40001
#define INPUT_LEN 8
#define NUM_LETTERS 26
#define GRAPH_LEN 2*NUM_LETTERS

using namespace std;

array<int, INPUT_LEN/2> parse_input(char* input){
    array<int, INPUT_LEN/2> nodes;

    int i = 0;
    int num_nodes = 0;

    while(i<INPUT_LEN){
        
        if(input[i] == '0' && input[i+1] == '0'){
            nodes[num_nodes] = -1;
        }

        else{
            int letter = input[i];
            char sign = input[i+1];

            if((char)sign ==  '+'){ nodes[num_nodes] = letter - 65; }
            else if((char)sign == '-'){ nodes[num_nodes] = letter - 65 + NUM_LETTERS; }
        }
        
        i = i+2;
        num_nodes++;
    }  

    return nodes;
}

void add_edges(char input[INPUT_LEN], bool G[GRAPH_LEN][GRAPH_LEN]){
    array<int, INPUT_LEN/2> nodes;
    int u, v, v_opposite;
    int i, j;

    nodes = parse_input(input);

    for(i=0; i<INPUT_LEN/2; i++){
        u = nodes[i];

        if(u == -1){
            continue;
        }

        for(j=0; j<INPUT_LEN/2; j++){
            v = nodes[j];

            if(i == j){
                continue;
            }

            if(v == -1){
                continue;
            }

            else{

                if(v<=25){
                    v_opposite = v + NUM_LETTERS;
                }

                else{
                    v_opposite = v - NUM_LETTERS;
                }

            }

            G[u][v_opposite] = true;
        }
    }
}

bool has_cycle(bool G[GRAPH_LEN][GRAPH_LEN]){
    int k, i, j;

    // run floyd warshall to detect possible paths
    for(k=0; k<GRAPH_LEN; k++){
        for(i=0; i<GRAPH_LEN; i++){
            for(j=0; j<GRAPH_LEN; j++){
                if(G[i][k] && G[k][j]){
                    G[i][j] = true;
                }
            }
        }
    }

    // detect if exists a path to itself
    for(k=0; k<GRAPH_LEN; k++){
        if(G[k][k] == true){ 
            return true; }
    }

    return false;
}

int main(void){
    int N;
    char input[INPUT_LEN];
    int i, j;

    while(true){

        bool G[GRAPH_LEN][GRAPH_LEN];

        for(i=0;i<GRAPH_LEN;i++){
            for(j=0;j<GRAPH_LEN;j++){
                G[i][j] = false;
            }
        }

        int ret = scanf("%d", &N);

        if(ret != 1){ break; }

        for(i=0; i<N; i++){
            scanf("%s", input);

            add_edges(input, G);
        }

        if(has_cycle(G)){ cout << "unbounded" << endl; }
        else{ cout << "bounded" << endl; }
    }

    return 0;
}