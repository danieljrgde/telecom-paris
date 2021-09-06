#include <iostream>
#include <algorithm>
#include <vector>
#include <string>
#include <cstring>
#include <utility>
#include <set>
#define LMAX 100001
#define SMAX 11

using namespace std;

int n, num_sequences;
string strings[SMAX];

// coded with the help of geeksforgeeks.org/z-algorithm-linear-time-pattern-searching-algorithm/
int* get_Z(string str){

    static int Z[LMAX+1];
    int n = str.length();
    int L, R, k;
  
    L = R = 0;
    for (int i = 1; i < n; ++i){

        if (i > R){
            
            L = R = i;
            while (R<n && str[R-L] == str[R])
                R++;
            Z[i] = R-L;
            R--;
        }
        
        else{
            
            k = i-L;

            if (Z[k] < R-i+1) Z[i] = Z[k];
  
            else{
                // else start from R and check manually
                L = i;
                while (R<n && str[R-L] == str[R])
                    R++;
                Z[i] = R-L;
                R--;
            }
        }
    }

    return Z;
}

int main(void){

    char input_string[LMAX];
    vector<pair<int, set<int, greater<int>>>> overlaps;
    set<int, greater<int>> overlap;
    int* Z;

    while(true){
        // clear variables
        overlaps.clear();
        overlap.clear();

        // read initial input
        int ret = scanf("%d", &n);
        if(ret!=1) break;
        scanf("%d", &num_sequences);

        // read predictions
        for(int i=0; i<num_sequences; i++){
            scanf("%s", input_string);
            string input = input_string;
            strings[i] = input;
        }

        // calculate overlap for each prediction
        for(int i=0; i<num_sequences; i++){

            int l = strings[i].length();

            // cout << strings[i] << endl;

            Z = get_Z(strings[i]);
            
            overlap.clear();
            
            for(int j=0; j<l; j++){
                if(Z[j] == l-j && Z[j] >= 2*l-n){
                    overlap.insert(Z[j]);
                }
            }
            overlap.insert(0);

            // for(auto tmp : overlap){
            //     cout << tmp << " ";
            // }
            // cout << endl << endl;

            overlaps.push_back(make_pair(i, overlap));
        }

        // sort overlap
        sort(overlaps.begin(), overlaps.end(),

            [](const pair<int, set<int, greater<int>>>& a,  
               const pair<int, set<int, greater<int>>>& b)
                    { 
                        vector<int> a_overlap, b_overlap; 
                        int a_id = a.first;
                        int b_id = b.first;

                        if((a.second).size() != (b.second).size()){
                            return (a.second).size() < (b.second).size();
                        }

                        else{
                            for(auto tmp : (a.second)) a_overlap.push_back(tmp);
                            for(auto tmp : (b.second)) b_overlap.push_back(tmp);

                            for(int i=0; i<a_overlap.size(); i++){
                                if(a_overlap[i] != b_overlap[i]) return a_overlap[i] < b_overlap[i];
                            }
                        }

                        return a.first < b.first;
                    }
            );  

        for(auto pair : overlaps){
            cout << strings[pair.first] << endl;
            // for(auto tmp : pair.second){
            //     cout << tmp << " ";
            // }
            // cout << endl;

        }
    }

    return 0;
}