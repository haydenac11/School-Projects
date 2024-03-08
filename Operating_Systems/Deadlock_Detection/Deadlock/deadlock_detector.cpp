
/// this is the ONLY file you should edit and submit to D2L

//#include "find_deadlock.h"
#include "deadlock_detector.h"
#include <string>
#include <unordered_map>
#include <vector>
#include <iostream>

#include <queue>

/// feel free to use the code below if you like:
/// ----------------------------------------------------------------
/// split(s) splits string s into vector of strings (words)
/// the delimiters are 1 or more whitespaces
static std::vector<std::string> split(const std::string & s)
{
    auto linec = s + " ";
    std::vector<std::string> res;
    bool in_str = false;
    std::string curr_word = "";
    for (auto c : linec) {
        if (isspace(c)) {
            if (in_str)
                res.push_back(curr_word);
            in_str = false;
            curr_word = "";
        } else {
            curr_word.push_back(c);
            in_str = true;
        }
    }
    return res;
}


class Graph{
    public:
    std::unordered_map<std::string, std::vector<std::string>> adj_list;
    std::unordered_map<std::string, int> out_counts;

    
    //function to add to adj list
    void addAdj(const std::string & node, const std::string & end){
        // Add our node and end node 
        adj_list[end].push_back(node);
        adj_list[node];
        out_counts[end]+= 0;
        out_counts[node] += 1;
    }

    std::unordered_map<std::string, std::vector<std::string>> getAdjList(){
        return adj_list;
    }

} graph;

/// this is the function you need to (re)implement
/// -------------------------------------------------------------------------
/// parameter edges[] contains a list of request- and assignment- edges
///   example of a request edge, process "p1" resource "r1"
///     "p1 -> r1"
///   example of an assignment edge, process "XYz" resource "XYz"
///     "XYz <- XYz"
///
/// You need to process edges[] one edge at a time, and run a deadlock
/// detection after each edge. As soon as you detect a deadlock, your function
/// needs to stop processing edges and return an instance of Result structure
/// with 'index' set to the index that caused the deadlock, and 'procs' set
/// to contain names of processes that are in the deadlock.
///
/// To indicate no deadlock was detected after processing all edges, you must
/// return Result with index=-1 and empty procs.


// Main function to detect deadlock
Result detect_deadlock(const std::vector<std::string> &edges) {

    Result result;
    std::vector<std::string> ret_dl;
    int ret_edge = -1;
   
    for (long unsigned int i = 0; i < edges.size(); i++){
        
        // Get a line
        auto line = edges[i];

        // Get the strings in the line
        auto toks = split(line);

        // Add $ to RHS to distinguish process and resource
        toks[2] = toks[2] + '$';

        // Request edge
        if (toks[1] == "->"){
            // toks[0] -> toks[2]
            graph.addAdj(toks[0], toks[2]);
        }
        // Assignment edge
        if (toks[1] == "<-"){
            // toks[0] <- toks [2]
            graph.addAdj(toks[2], toks[0]);
        }

        // Create deep copy of out counts 
        std::unordered_map<std::string, int> out;
        for (const auto& pair : graph.out_counts) {
            out[pair.first] = pair.second;
        }

        // get a copy of adjacency list to work with
        std::unordered_map<std::string, std::vector<std::string>> adj_list = graph.getAdjList();

        // create zeros
        std::vector<std::string> zeros;
        for (const auto& node : out) {
            if (node.second == 0) {
                zeros.push_back(node.first);
            }
        }

        // algorithm
        while (!zeros.empty()){
            std::string n = zeros.back();
            zeros.pop_back();

            for (const auto& n2: adj_list[n]){
                if (out[n2] > 0){
                out[n2]--;
                }
                if (out[n2] == 0){
                    zeros.push_back(n2);
                } 
            }

        }

        // check for nodes in deadlock not containing $
        if (zeros.empty()){
            for (const auto& b : out){
                if (b.second > 0 && (b.first.find('$') == std::string::npos)){
                    ret_edge = (int) i;
                    ret_dl.push_back(b.first);
                }
            }

        }

        // if deadlock then return info
        if (ret_dl.size() > 0){
            result.edge_index = ret_edge;
            for (const auto &n : ret_dl){
                result.dl_procs.push_back(n);
            }
            return result;
        }

    }

    result.edge_index = ret_edge;
    return result;
}
