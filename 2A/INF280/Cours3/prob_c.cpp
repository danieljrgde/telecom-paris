#include <iostream>
#include <queue>
#include <vector>
#include <utility>
#include <algorithm>
#define MAXN 10001
#define INF 2147483647

using namespace std;
typedef pair<int,int> WeightNode;

void dijkstra(int N, int start, vector <pair<int, int>>(&Adj)[MAXN], int Dist[MAXN]){
    int i;
    priority_queue<WeightNode, vector<WeightNode>, greater<WeightNode> > Q;

    for(i=0; i<N; i++){
        Dist[i] = INF;
    }

    Dist[start] = 0;
    Q.push(make_pair(start, 0));

    while(!Q.empty()){
        int u = Q.top().first;
        Q.pop();

        for(auto tmp : Adj[u]){

            int v = tmp.first;
            int dist = tmp.second;

            if(Dist[v] > Dist[u] + dist){
                Dist[v] = Dist[u] + dist;
                Q.push(make_pair(v, Dist[v]));
            }
        }
    }
}

int main(){

    int N, M, S, D, u, v, d;
    int i,j;

    while(1){

        int Dist[MAXN], Dist_inv[MAXN], Dist_almost[MAXN];
        vector<pair<int, int>> Adj[MAXN], Adj_inv[MAXN], Adj_copy[MAXN];

        scanf("%d%d", &N, &M);

        if(N==0 && M==0){ break; }

        scanf("%d%d", &S, &D);

        for(i=0; i<M; i++){
            scanf("%d%d%d", &u, &v, &d);

            Adj[u].push_back(make_pair(v,d));
            Adj_inv[v].push_back(make_pair(u,d));
        }

        dijkstra(N, S, Adj, Dist);
        dijkstra(N, D, Adj_inv, Dist_inv);

        int min_dist = Dist[D];

        for(i=0; i<N; i++){            
            for(auto node : Adj[i]){
                v = node.first;
                d = node.second;
                //printf("edge(%d, %d)\n", i, v);
                if(Dist[i] + d + Dist_inv[v] > min_dist){
                    Adj_copy[i].push_back(make_pair(v, d));
                }
            }
        }

        dijkstra(N, S, Adj_copy, Dist_almost);
        int almost_min_dist = Dist_almost[D];

        if(almost_min_dist == INF){
            cout << -1 << endl;
        }
        else{ cout << almost_min_dist << endl; }
    }

    return 0;

}