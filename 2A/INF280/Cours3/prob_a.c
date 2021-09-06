#include <stdio.h>
#include <string.h>
#define MAX_STRING_LEN 256
#define MAX_LEN 51
#define INF 100

int degrees[MAX_LEN][MAX_LEN];

typedef struct{
    char name[MAX_STRING_LEN];
}person;

void floyd_warshall(int num_people){
    int k, i, j;

    for(k=0; k<num_people; k++){
        for(i=0; i<num_people; i++){
            for(j=0; j<num_people; j++){
                if(degrees[i][j] > degrees[i][k] + degrees[k][j]){
                    degrees[i][j] = degrees[i][k] + degrees[k][j];
                }

            }
        }
    }
}

int main(void){
    char name1[MAX_STRING_LEN], name2[MAX_STRING_LEN];
    person people[MAX_STRING_LEN];
    int num_people, num_relationships, num_stored_people;
    int num_cases, i, j;

    num_cases = 1;
    while(1){

        for(i=0; i<MAX_LEN; i++){
            for(j=0; j<MAX_LEN; j++){
                degrees[i][j] = 0;
            }
        }
        
        scanf("%d%d", &num_people, &num_relationships);

        if(num_people==0 && num_relationships==0){ break; }


        /* clear matrix */
        for(i=0; i<num_people; i++){
            for(j=0; j<num_people; j++){
                if(i==j){
                    degrees[i][j] == 0;
                }

                else{
                    degrees[i][j] = INF;
                }
            }
        }

        num_stored_people = 0;
        for(i=0; i<num_relationships; i++){
            scanf("%s%s", name1, name2);
            
            int name1_idx = -1;
            int name2_idx = -1;

            for(j=0; j<num_stored_people; j++){
                
                if(strcmp(people[j].name, name1)==0){
                    name1_idx = j;
                }

                if(strcmp(people[j].name, name2)==0){
                    name2_idx = j;
                }
            }

            if(name1_idx == -1){
                strcpy(people[num_stored_people].name, name1);
                name1_idx = num_stored_people;
                num_stored_people++;
            }

            if(name2_idx == -1){
                strcpy(people[num_stored_people].name, name2);
                name2_idx = num_stored_people;
                num_stored_people++;
            }

            degrees[name1_idx][name2_idx] = 1;
            degrees[name2_idx][name1_idx] = 1;
        }

        floyd_warshall(num_people);

        int max_degree = 0;
        for(i=0; i<num_people; i++){
            for(j=0; j<num_people; j++){
                
                if(max_degree < degrees[i][j]){
                    max_degree = degrees[i][j];
                }
            }
        }

        printf("Network %d: ", num_cases);

        if(max_degree == INF){ printf("DISCONNECTED\n\n"); }

        else{ printf("%d\n\n", max_degree); }

        num_cases++;
    }

    return 0;
}