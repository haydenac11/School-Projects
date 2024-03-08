#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>
#include <unistd.h>
#include <stdio.h>
#include <time.h>

#define NUM_ASTRONOMERS 10 
#define NUM_ASYMMETRIC 3 // Number of asymmetric astronomers
#define AVG_EAT_TIME 1
#define MAX_WAIT_TIME 2

int ordering[NUM_ASTRONOMERS];  // 0 means symmetric, 1 means asymmetric
pthread_mutex_t chopstick[NUM_ASTRONOMERS]; // Array of mutexes for each chopstick
pthread_t astro_threads[NUM_ASTRONOMERS]; // Array for astronomer threads



void think(int id) {
    //printf("ASTRO %d is thinking\n", id);
}

void eat(int id) {
    printf("ASTRO %d is eating\n", id);
    sleep(AVG_EAT_TIME);
    printf("ASTRO %d done eating and now thinking\n", id);
}

// template for the philosopher thread
// it is ran by each thread
void* astronomers(void* arg) {
    int id = *((int*)arg);
    int chop_left = id % NUM_ASTRONOMERS;
    int chop_right = (id + NUM_ASTRONOMERS - 1) % NUM_ASTRONOMERS;


    if (ordering[id] == 0){
        // Symmetric
        printf("ASTRO %d is symmetric\n", id);
        while(1){
            think(id);
            // Try to acquire the left chopstick first
            if(pthread_mutex_trylock(&chopstick[chop_left]) == 0){

                // Start timer 
                time_t start = time(NULL);
                double duration = 0.5;

                // try to acquire the right chopstick in less that 0.5s
                while((time(NULL) - start) < duration){
                    // Try to acquire right chopstick
                    if(pthread_mutex_trylock(&chopstick[chop_right]) == 0){

                        printf("ASTRO %d picked up chopstick %d (RIGHT)\n", id, chop_right);
                        printf("ASTRO %d picked up chopstick %d (LEFT)\n", id, chop_left);
                        eat(id);
                        printf("ASTRO %d released chopstick %d (LEFT)\n", id, chop_left);
                        pthread_mutex_unlock(&chopstick[chop_right]);
                        break;
                    }
                }
                printf("ASTRO %d released chopstick %d (RIGHT)\n", id, chop_right); 
                pthread_mutex_unlock(&chopstick[chop_left]);
                
            }
            
        }


    } else{
        // Asymmetric
        printf("ASTRO %d is asymmetric\n", id);

        while(1){
            think(id);

            if(pthread_mutex_trylock(&chopstick[chop_right]) == 0){     // Pickup the chopstick to the right

                // Start the timer
                time_t endwait;
                time_t start = time(NULL);
                endwait = start + 5;

                
                printf("ASTRO %d picked up chopstick %d (RIGHT)\n", id, chop_right);

                // Simulate asymmetric
                sleep(1);

                // While loop acts as a timer
                // Will allow for the astronomer to try to get the left chopstick 
                // until 2 seconds has passed since he grabbed the right one 

                while(start < endwait){

                    // Try to pickup the left chopstick
                    if(pthread_mutex_trylock(&chopstick[chop_left]) == 0){
                        printf("ASTRO %d picked up chopstick %d (LEFT)\n", id, chop_left);

                        // eat
                        eat(id);

                        // Release both chopsticks now that eating is done

                        // We release the left chopstick in the if statement and then break,
                        // the right chopstick is dealt with outside of the while loop

                        printf("ASTRO %d released chopstick %d (LEFT)\n", id, chop_left); 
                        pthread_mutex_unlock(&chopstick[chop_left]); 

                        // break from the while loop since we are done eating
                        break;
                    }
                }

                // Release right chopstick
                pthread_mutex_unlock(&chopstick[chop_right]);
                printf("ASTRO %d released chopstick %d (RIGHT)\n", id, chop_right);


            }
        }  
    }
    
}


// a function to generate random ordering of 0 and 1
// 0 means symmetric, 1 means asymmetric
void place_astronomers(int *ordering){
    // generate random ordering
    // 0 means symmetric, 1 means asymmetric
    for (int i = 0; i < NUM_ASYMMETRIC; i++) {
        ordering[i] = 1;
    }
    for (int i = NUM_ASYMMETRIC; i < NUM_ASTRONOMERS; i++) {
        ordering[i] = 0;
    }
    // shuffle the ordering
    for (int i = 0; i < NUM_ASTRONOMERS; i++) {
        int j = rand() % NUM_ASTRONOMERS;
        int temp = ordering[i];
        ordering[i] = ordering[j];
        ordering[j] = temp;
    }
}


int main(){
    // initialize forks
    for (int i = 0; i <  NUM_ASTRONOMERS; i++){
        pthread_mutex_init(&chopstick[i], NULL);
    }
    //pthread_mutex_init(&mutex, NULL);

    // initialize astronomers
    int astro_id[NUM_ASTRONOMERS];
    place_astronomers(ordering);

    // start astronomers
    for (int i = 0; i < NUM_ASTRONOMERS; i++){
        astro_id[i] = i;
        pthread_create(&astro_threads[i], NULL, astronomers, &astro_id[i]);
    }

    // call join on astronomers
    for (int i = 0; i < NUM_ASTRONOMERS; i++) {
        pthread_join(astro_threads[i], NULL);
    }

    return 0;
}