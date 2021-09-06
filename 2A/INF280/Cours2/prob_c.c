#include <stdio.h>
#define MAX 1000001

/* mergesort functions coded with the help of geeksforgeeks.org/merge-sort/ */
void merge(long int s, long int m, long int e, long int array_key[], long int array_sup[]){
    
    long int l1, l2, i;
    static long int temp_key[MAX/2];
    static long int temp_sup[MAX/2];

    for(l1 = s, l2 = m+1, i = s; l1<=m && l2<=e; i++) {
      
        if(array_key[l1] <= array_key[l2]){
            temp_key[i] = array_key[l1];
            temp_sup[i] = array_sup[l1];
            l1++;
        }

        else{
            temp_key[i] = array_key[l2];
            temp_sup[i] = array_sup[l2];
            l2++;
        }
    }

    while(l1 <= m){
        temp_key[i] = array_key[l1];
        temp_sup[i] = array_sup[l1];
        i++;
        l1++;
    }    

    while(l2 <= e){
        temp_key[i] = array_key[l2];
        temp_sup[i] = array_sup[l2];
        i++;
        l2++;
    }

    for(i = s; i <= e; i++){
        array_key[i] = temp_key[i];
        array_sup[i] = temp_sup[i];
    }
}

void merge_sort(long int s, long int e, long int array_key[], long int array_sup[]){

    if(s < e){

        /* avoids overflow */
        long int m = s + (e-s)/2;

        merge_sort(s, m, array_key, array_sup);
        merge_sort(m+1, e, array_key, array_sup);
        merge(s, m, e, array_key, array_sup);
    }
}

int main(void){
    long int n, i, free_size, use_size;
    long int swap, extra;

    while(1){
        
        int ret = scanf("%ld", &n);

        if(ret != 1){
            break;    
        }

        static long int capacity_old[MAX], capacity_new[MAX];
        static long int free_old[MAX], free_new[MAX];
        static long int use_old[MAX], use_new[MAX];
        static long int ordered_old[MAX], ordered_new[MAX];

        free_size = 0;
        use_size = 0;
        for(i=0; i<n; i++){
            scanf("%ld", &capacity_old[i]);
            scanf("%ld", &capacity_new[i]);

            if(capacity_old[i] <= capacity_new[i]){
                free_old[free_size] = capacity_old[i];
                free_new[free_size] = capacity_new[i];
                free_size++;
            }

            else{
                use_old[use_size] = capacity_old[i];
                use_new[use_size] = capacity_new[i];
                use_size++;
            }
        }

        merge_sort(0, free_size-1, free_old, free_new);
        merge_sort(0, use_size-1, use_new, use_old);

        for(i=0; i<free_size; i++){
            ordered_old[i] = free_old[i];
            ordered_new[i] = free_new[i];
        }

        for(i=0; i<use_size; i++){
            ordered_old[i + free_size] = use_old[use_size-i-1];
            ordered_new[i + free_size] = use_new[use_size-i-1];
        }

        swap = 0;
        extra = 0;
        for(i=0; i<n; i++){

            if(ordered_old[i] > extra){
                swap += ordered_old[i] - extra;
                extra = 0;
            }

            else{
                extra -= ordered_old[i];
            }

            extra += ordered_new[i];
        }

        printf("%ld\n", swap);
    }

    return 0;
}