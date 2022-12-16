#include <fstream>
#include <iostream>
#include <sstream>
#include <unordered_set>
#include <unordered_map>
#include <stack>
#include <chrono>

using std::unordered_set;
using std::unordered_map;
using std::string;
using std::stringstream;
using std::pair;

class TunnelGraph {
    struct Node {
        string name;
        int flow_rate;
        int pressure_released;
        unordered_map<string, int> node_dist;

        Node() {
            node_dist = unordered_map<string, int>();
        }

        Node(string n, int f) {
            name = n;
            flow_rate = f;
            node_dist = unordered_map<string, int>();
        }

        void print() {
            std::cout << name << " -> ";
            for(pair<const string, int>& s : node_dist) std::cout << s.first << ":" << s.second << "; ";
            std::cout << std::endl;
        }
    };

    private:
        unordered_map<string, Node> nodes;

        void calc_distances() {
            for(int i = 0; i < (int) nodes.size(); i++) {
                for(pair<const string, Node>& n : nodes) {
                    Node& curr = n.second;
                    
                    for(pair<const string, int>& out : curr.node_dist) {
                        for(pair<const string, int>& out_out : nodes[out.first].node_dist) {
                            int curr_dist = std::numeric_limits<int>::max();
                            if(curr.node_dist.contains(out_out.first)) {
                                curr_dist = curr.node_dist[out_out.first];
                            }
                            curr.node_dist[out_out.first] = std::min(curr_dist, out_out.second + out.second);
                        } 
                    }
                }
            }
        }

    public:
        TunnelGraph(string path) {
            std::ifstream input(path);
            string line;

            while(getline(input, line)) {
                int pos1 = line.find("Valve")+5;
                int pos2 = line.find("rate=", pos1)+5;
                int pos3 = line.find("to valve", pos2)+8;

                string name;
                int flow_rate;
                stringstream(line.substr(pos1)) >> name;
                stringstream(line.substr(pos2)) >> flow_rate;
                nodes.emplace(name, Node(name, flow_rate));
                nodes[name].node_dist.emplace(name, 0);
                
                string out_node;
                string n_out_node;
                stringstream out_nodes(line.substr(pos3));
                out_nodes >> out_node;
                if(out_node == "s") {
                    out_nodes >> out_node;
                    while(out_nodes >> n_out_node) {
                        nodes[name].node_dist.emplace(out_node.substr(0, out_node.size()-1), 1);
                        out_node = n_out_node;
                    }
                    nodes[name].node_dist.emplace(n_out_node, 1);
                } else {
                    nodes[name].node_dist.emplace(out_node, 1);
                }
            }

            calc_distances();
        }

        int max_pressure_released(int minutes) {
            string start = "AA";

            // depth_first_search
            struct dfs_trip {
                Node& n;
                int pressure;
                int pressure_per_min;
                int min_passed;
                bool returning = false;
            };

            std::stack<dfs_trip> ws;
            unordered_set<string> visited;
            int max = std::numeric_limits<int>::min();

            ws.emplace(dfs_trip{nodes[start], 0, 0, 0, false});
            int it = 0;
            int log_freq = 1'000'000;

            while(!ws.empty()) {
                dfs_trip curr = ws.top();
                ws.pop();

                if(curr.returning) {
                    visited.erase(curr.n.name);
                    continue;
                }

                if(curr.min_passed == minutes) {
                    max = std::max(max, curr.pressure);
                }

                if(visited.contains(curr.n.name)) continue;
                visited.emplace(curr.n.name);

                ws.emplace(dfs_trip{curr.n, curr.pressure, curr.pressure_per_min, curr.min_passed, true});

                for(pair<const string, int>& v : curr.n.node_dist) {
                    if(!visited.contains(v.first) && (curr.min_passed + v.second + 1) <= minutes && nodes[v.first].flow_rate != 0) {
                        Node& n_to = nodes[v.first];
                        ws.emplace(dfs_trip{n_to,
                                            curr.pressure + ((v.second + 1) * curr.pressure_per_min),
                                            curr.pressure_per_min + n_to.flow_rate,
                                            curr.min_passed + v.second + 1,
                                            false});
                    }
                }

                ws.emplace(dfs_trip{curr.n,
                                    curr.pressure + ((minutes-curr.min_passed) * curr.pressure_per_min),
                                    curr.pressure_per_min,
                                    minutes,
                                    false});

                if(it == log_freq-1) std::cout << "stack size: " << ws.size() << "; current max: " << max << std::endl;
                it = (it+1) % log_freq;
            }
            
            return max;
        }

        int max_pressure_released_duo(int minutes) {
            string start = "AA";

            // depth_first_search

            struct user {
                string dest;
                int to_move;
            };

            struct dfs_info {
                user me;
                user eleph;
                int press_rel;
                int p_per_min;
                int min_pass;
                unordered_set<string> visited;
            };

            std::stack<dfs_info> ws;
            int max = std::numeric_limits<int>::min();

            ws.emplace(dfs_info{user{start, 0},
                                user{start, 0},
                                0,
                                0,
                                0,
                                unordered_set<string>()});
            
            int it = 0;
            int log_freq = 1'000'000;

            while(!ws.empty()) {
                dfs_info c = ws.top();
                ws.pop();

                if(c.min_pass == minutes) {

                    // std::cout << c.me.dest 
                    //             << ": " << c.me.to_move 
                    //             << " - " << c.eleph.dest 
                    //             << ": " << c.eleph.to_move 
                    //             << " -- t " << c.min_pass 
                    //             << " -- p/m " << c.p_per_min
                    //             << " -- p " << c.press_rel
                    //             << std::endl; 
                    max = std::max(max, c.press_rel);
                    continue;
                }

                if(c.visited.contains(c.me.dest)) continue;
                if(c.visited.contains(c.eleph.dest)) continue;

                if(c.eleph.to_move == 0 && c.me.to_move == 0) {
                    c.visited.emplace(c.me.dest);
                    c.visited.emplace(c.eleph.dest);

                    int n_flow = c.p_per_min + nodes[c.me.dest].flow_rate + (c.me.dest != c.eleph.dest ? nodes[c.eleph.dest].flow_rate : 0);
                    for(pair<const string, int>& v : nodes[c.me.dest].node_dist) {
                        for(pair<const string, int>& w : nodes[c.eleph.dest].node_dist) {
                            if(v.first != w.first && 
                               !c.visited.contains(v.first) && !c.visited.contains(w.first) &&
                                nodes[v.first].flow_rate != 0 && nodes[w.first].flow_rate != 0 &&
                                v.second + c.min_pass + 1 <= minutes && w.second + c.min_pass + 1 <= minutes) { 

                                ws.emplace(dfs_info{user{v.first, v.second + 1},
                                                    user{w.first, w.second + 1},
                                                    c.press_rel,
                                                    n_flow,
                                                    c.min_pass,
                                                    c.visited});
                            }
                        }
                    }

                    ws.emplace(dfs_info{c.me,
                                        c.eleph,
                                        c.press_rel + ((minutes - c.min_pass) * n_flow),
                                        n_flow,
                                        minutes,
                                        c.visited});
                } else if(c.me.to_move == 0) {
                    c.visited.emplace(c.me.dest);

                    int n_flow = c.p_per_min + nodes[c.me.dest].flow_rate;
                    for(pair<const string, int>& v : nodes[c.me.dest].node_dist) {
                        if(!c.visited.contains(v.first) &&
                            nodes[v.first].flow_rate != 0 &&
                            v.second + c.min_pass + 1 <= minutes && 
                            v.first != c.eleph.dest) {           

                            ws.emplace(dfs_info{user{v.first, v.second + 1},
                                                c.eleph,
                                                c.press_rel,
                                                n_flow,
                                                c.min_pass,
                                                c.visited});
                        }
                    }

                    int nn_flow = n_flow + nodes[c.eleph.dest].flow_rate;
                    ws.emplace(dfs_info{c.me,
                                        user{c.eleph.dest, 0},
                                        c.press_rel + (c.eleph.to_move * n_flow) + ((minutes - c.eleph.to_move - c.min_pass) * nn_flow),
                                        c.p_per_min,
                                        minutes,
                                        c.visited});
                } else if(c.eleph.to_move == 0) {
                    c.visited.emplace(c.eleph.dest);

                    int n_flow = c.p_per_min + nodes[c.eleph.dest].flow_rate;
                    for(pair<const string, int>& v : nodes[c.eleph.dest].node_dist) {
                        if(!c.visited.contains(v.first) &&
                            nodes[v.first].flow_rate != 0 &&
                            v.second + c.min_pass + 1 <= minutes && 
                            v.first != c.me.dest) {        

                            ws.emplace(dfs_info{c.me,
                                                user{v.first, v.second + 1},
                                                c.press_rel,
                                                n_flow,
                                                c.min_pass,
                                                c.visited});
                        }
                    }

                    int nn_flow = n_flow + nodes[c.me.dest].flow_rate;
                    ws.emplace(dfs_info{user{c.me.dest, 0},
                                        c.eleph,
                                        c.press_rel + (c.me.to_move * n_flow) + ((minutes - c.me.to_move - c.min_pass) * nn_flow),
                                        c.p_per_min,
                                        minutes,
                                        c.visited});
                } else {
                    c.me.to_move--;
                    c.eleph.to_move--;
                    ws.emplace(dfs_info{c.me,
                                        c.eleph,
                                        c.press_rel + c.p_per_min,
                                        c.p_per_min,
                                        c.min_pass + 1,
                                        c.visited});
                }

                if(it == log_freq-1) std::cout << "stack size: " << ws.size() << "; current max: " << max << std::endl;
                it = (it+1) % log_freq;
            }
            
            return max;
        }
};

int main() {
    auto start = std::chrono::high_resolution_clock::now();
    TunnelGraph tg("input/day16.txt");
    std::cout << tg.max_pressure_released(30) << std::endl;
    std::cout << tg.max_pressure_released_duo(26) << std::endl;
    auto stop = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::microseconds>(stop - start);
    double time = duration.count();
    std::cout << "elapsed (ms): " << (time * 0.001) << std::endl;
    return 0;
}