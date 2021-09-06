#include <iostream>
#include <string>
#include <cctype>
#include <utility>
#define NMAX 1000002
using namespace std;

int main(void){

    string s;
    long long int r, count, i;

    while(cin >> s){

        // init vars    
        long long int residue[3] = {0,0,0};
        long long int previous_residue[3] = {0,0,0};

        count = 0;
        for(i=0; i < s.length(); i++){

            // if digit is found, reset residue count
            if(!isdigit(s[i])){
                residue[0] = residue[1] = residue[2] = 0;
                previous_residue[0] = previous_residue[1] = previous_residue[2] = 0;
                continue;
            }

            // calculate current residue
            r = (s[i] - '0') % 3;
            
            // shift residues accordingly
            residue[0] = previous_residue[(3-r) % 3];
            residue[1] = previous_residue[(3-r+1) % 3];
            residue[2] = previous_residue[(3-r+2) % 3];
            
            // count for the new calculated residue
            residue[r]++;

            // count for new divisible by 3 strings
            count += residue[0];

            // update previous residue values
            previous_residue[0] = residue[0];
            previous_residue[1] = residue[1];
            previous_residue[2] = residue[2];
        }

        // print results
        cout << count << endl;
    }
    
    return 0;
}