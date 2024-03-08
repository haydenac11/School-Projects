
/// this is the only file you should modify and submit for grading

#include "scheduler.h"
#include <iostream>

// this is the function you should implement
//
// simulate_rr() implements a Round-Robin scheduling simulator
// input:
//   quantum = time slice
//   max_seq_len = maximum length of the reported executing sequence
//   processes[] = list of process with populated IDs, arrivals, and bursts
// output:
//   seq[] - will contain the compressed execution sequence
//         - idle CPU will be denoted by -1
//         - other entries will be from processes[].id
//         - sequence will be compressed, i.e. no repeated consecutive numbers
//         - sequence will be trimmed to contain only first max_seq_len entries
//   processes[]
//         - adjust finish_time and start_time for each process
//         - do not adjust other fields
//
void simulate_rr(
    int64_t quantum, 
    int64_t max_seq_len,
    std::vector<Process> & processes,
    std::vector<int> & seq
) 
{

    seq.clear();
    int64_t curr_time = 0;
    std::vector<Process> jobQueue;
    std::vector<Process> readyQueue; 
    std::vector<int64_t> remainingBursts;

    // Add all processes to the jobQueue
    for (const auto &p : processes){
        jobQueue.push_back(p);
    }


    while(1){
        // jq and rq both empty then simulation is over
        if(jobQueue.size() == 0 && readyQueue.size() == 0){
            return;
        }

        // jq !empty and rq empty
        if(jobQueue.size() > 0 && readyQueue.size() == 0){
            // add cpu is idle 
            if (curr_time < jobQueue.front().arrival_time){
                seq.push_back(-1);
            }
            // skip current time to first arrival
            curr_time = jobQueue.front().arrival_time;
            // add first item in jq to rq
            auto add = jobQueue.front();
            jobQueue.erase(jobQueue.begin());
            readyQueue.push_back(add);
            // add to remaining bursts
            remainingBursts.push_back(add.burst);
            continue;
        }


        // jq empty and rq not empty
        if(jobQueue.size() == 0 && readyQueue.size() > 0){
            
            // add current job to seq
            if (seq.back() != readyQueue.front().id){
                seq.push_back(readyQueue.front().id);
            }
            

            // add start time if hasn't been done already
            for (auto &j : processes){
                if(j.id == readyQueue.front().id && j.start_time < 0){
                    j.start_time = curr_time;
                }
            }
            // find minimum remaining time in rq
            int64_t minimum_remaining_bursts = INT64_MAX;
            for (auto &j : remainingBursts){
                if(j < minimum_remaining_bursts){
                    minimum_remaining_bursts = j;
                }
            }
            //int crazy = 1;
            int64_t n = minimum_remaining_bursts / quantum;
            // check if job finishes during the quantum
            if(remainingBursts.front() <= quantum){ // quantum
                
                // increment the time step
                auto time_step = remainingBursts.front();
                curr_time += time_step;
                
                // set the finish time for the current job
                for (auto &j : processes){
                    if(j.id == readyQueue.front().id){
                        j.finish_time = curr_time;
                    }
                }

                // Remove the job from ready queue and bursts vector
                remainingBursts.erase(remainingBursts.begin());
                readyQueue.erase(readyQueue.begin());
            }
            else if (remainingBursts.front() > n){ //quantum
                // job does not finish
                // subtract the quantum
                auto skip = n * readyQueue.size() * quantum;
                // remainingBursts.front() -= quantum;
                remainingBursts.front() -= skip;
                // remove the first item and add it to back of queue
                auto add = remainingBursts.front();
                remainingBursts.erase(remainingBursts.begin());
                remainingBursts.push_back(add);
                // do the same for the ready queue
                auto add2 = readyQueue.front();
                readyQueue.erase(readyQueue.begin());
                readyQueue.push_back(add2);

                // adjust the time
                // curr_time += quantum;
                curr_time += skip;
            }
            continue;
        }

        // jq !empty and rq !empty
        if (jobQueue.size() > 0 && readyQueue.size() > 0){

            // add current job to sequence
            if (seq.back() != readyQueue.front().id){
                seq.push_back(readyQueue.front().id);
            }
            // assign a start time for current job if hasnt been done yet 
            for (auto &j : processes){
                if(j.id == readyQueue.front().id && j.start_time < 0){
                    j.start_time = curr_time;
                }
            }

            // check if job finishes during the quantum
            if(remainingBursts.front() <= quantum){
                
                // increment the time step
                auto time_step = remainingBursts.front();
                curr_time += time_step;
                
                // set finish time for current job if hasn't been done yet
                for (auto &j : processes){
                    if(j.id == readyQueue.front().id){
                        j.finish_time = curr_time;
                    }
                }
                

                // job finishes
                remainingBursts.erase(remainingBursts.begin());
                readyQueue.erase(readyQueue.begin());
                
                for (size_t i = 0; i < jobQueue.size(); i++){
                    // check if job arrived during time step
                    if(curr_time > jobQueue[i].arrival_time){
                        // add to ready queue
                        // remove from job queue
                        readyQueue.push_back(jobQueue[i]);
                        remainingBursts.push_back(jobQueue[i].burst);
                        jobQueue.erase(jobQueue.begin());
                        i--;
                    }
                    
                }
            }
            else if (remainingBursts.front() > quantum){
                // job does not finish
                // adjust the time
            // find minimum remaining time in rq

                int64_t n1 = (jobQueue[0].arrival_time - curr_time) / (readyQueue.size() * quantum);
                
                //curr_time += quantum;

                // find minimum remaining time in rq
                int64_t minimum_remaining_bursts = INT64_MAX;
                for (auto &j : remainingBursts){
                    if(j < minimum_remaining_bursts){
                        minimum_remaining_bursts = j;
                    }
                }
                //int crazy = 1;
                int64_t n2 = minimum_remaining_bursts / quantum;
                int64_t n = std::min(n1, n2);
                if(n < quantum){
                    n = quantum;
                }
                curr_time += quantum;
                //std::cout << curr_time << "\n";
                // check if jobs arrived during time slice
                for (size_t i = 0; i < jobQueue.size(); i++){
                    if(curr_time > jobQueue[i].arrival_time){
                        // add to ready queue
                        // remove from job queue
                        readyQueue.push_back(jobQueue[i]);
                        remainingBursts.push_back(jobQueue[i].burst);
                        jobQueue.erase(jobQueue.begin());
                        i--;
                    }
                }

                // decremenet the time of current job
                //remainingBursts.front() -= quantum;
                remainingBursts.front() -= quantum;

                // remove the first item and add it to back of queue
                auto add = remainingBursts.front();
                remainingBursts.erase(remainingBursts.begin());
                remainingBursts.push_back(add);
                // do the same for the ready queue
                auto add2 = readyQueue.front();
                readyQueue.erase(readyQueue.begin());
                readyQueue.push_back(add2);


            }
            continue;

        }



    }
}
