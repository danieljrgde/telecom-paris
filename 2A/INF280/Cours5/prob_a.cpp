#include <iostream>
#include <bits/stdc++.h>
#include <queue>
#include <vector>
#include <bitset>
#define NMAX 21
using namespace std;

int m, n;
uint64_t neighbours[NMAX] = {0}; // stores the tree's neighbours
uint64_t seen_states[((uint64_t) 1) << NMAX]; // stores all the previously seen states
uint64_t parent[((uint64_t) 1) << NMAX]; // stores the parent of each state
uint64_t shot_pos[((uint64_t) 1) << NMAX]; // stores the position that was shot to get to that state
bool killed_monkey;


// modified bfs function (based on lecture slides) to kill the monkey
void BFS(uint64_t initial_state){
    int next_state; // stores the next state
    queue<int> Q;
    
    // reset arrays
    fill(seen_states, seen_states + (1 << NMAX), 0);
    fill(shot_pos, shot_pos + (1 << NMAX), 0);
    fill(parent, parent + (1 << NMAX), 0);

    // start BFS
    seen_states[initial_state] = 1; // mark initial state as seen
    Q.push(initial_state);

    while (!Q.empty()){

        // get next state
        int current_state = Q.front();
        Q.pop();

        // shoot every tree where the monkey can be
        for(int i=0; i<n; i++){

            // if monkey may be in position i
            if(current_state & (((uint64_t) 1) << i)){

                next_state = 0;
                // shoot pos i and calculate next state
                for(int j = 0; j<n; j++){
                    // ignore the position that was shot
                    if(i==j) continue;

                    // if position is available, include its possibilities in next state
                    if(current_state & (((uint64_t) 1) << j)) next_state = next_state | neighbours[j];
                }

                // if next state has not been seen, continue process of shooting
                if(seen_states[next_state] == 0){

                    seen_states[next_state] = 1; // mark next state as seen
                    parent[next_state] = current_state; // mark current state as a parent to next state
                    shot_pos[next_state] = i; // store shot position that led to this state
                    Q.push(next_state); // push next state to continue bfs
                }

                // if next state is 0, we've killed the monkey
                if(next_state == 0){
                    killed_monkey = true;
                    return;
                }
            }
        }
    }
}

int main(void){
    int initial_state, current_state, last_state;
    vector<int> shooting_path;
    int n1, n2;
    int i, j;

    while(true){

        // reset values
        fill(neighbours, neighbours + NMAX, 0);
        shooting_path.clear();
        killed_monkey = false;
        
        // scan tree size and input size
        scanf("%d", &n);
        scanf("%d", &m);
        if(m == 0 && n == 0) break;

        for(i=0; i<m; i++){
            // scan tree connections
            scanf("%d", &n1);
            scanf("%d", &n2);
            
            // update neighbours index
            neighbours[n1] = neighbours[n1] | (1<<n2);
            neighbours[n2] = neighbours[n2] | (1<<n1);
        }

        // if there exists a cycle in the graph, its impossible
        if(m > n-1){
            cout << "Impossible" << endl;
            continue;
        }

        initial_state = (((uint64_t) 1) << n) - 1; // define initial state: monkey can be in any position
        BFS(initial_state); // run bfs search to find which positions to shoot
        
        // case where no possibility kills the monkey
        if(!killed_monkey){
            cout << "Impossible" << endl;
            continue;
        }

        last_state = 0; // set final state as zero (monkey is dead)

        // store shot position for each state
        current_state = last_state;
        for(i=1; current_state != initial_state; i++){
            shooting_path.push_back(shot_pos[current_state]);
            current_state = parent[current_state];
        }

        // print results in order
        cout << i-1 << ":";
        while(!shooting_path.empty()){
            cout << " " << shooting_path.back();
            shooting_path.pop_back();
        }
        cout << endl;
    }

    return 0;
}
