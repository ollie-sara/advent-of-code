#include <fstream>
#include <iostream>
#include <vector>
#include <set>
#include <unordered_set>
#include <unordered_map>
#include <sstream>
#include <chrono>

using std::ifstream;
using std::stringstream;
using std::string;
using std::numeric_limits;

#define MIN_INT numeric_limits<int>::min()
#define MAX_INT numeric_limits<int>::max()

class SandSimulator {

    private:
        struct pair_hash
        {
            template <class T1, class T2>
            std::size_t operator() (const std::pair<T1, T2> &pair) const {
                return std::hash<T1>()(pair.first) ^ std::hash<T2>()(pair.second);
            }
        };

        typedef std::pair<int, int> point; // first = x, second = depth
        typedef std::vector<point> plist;

        struct pset {
            std::unordered_set<point, pair_hash> hashset;
            std::unordered_map<int, std::set<int>> treeset;

            bool contains(const point& p) {
                return hashset.contains(p);
            }

            void emplace(const point& p) {
                hashset.emplace(p);
                treeset[p.first].emplace(p.second);
            }

            int upper_bound(const point& p) {
                auto out = treeset[p.first].upper_bound(p.second);
                return (out == treeset[p.first].end() ? MAX_INT : *out);
            }

            int size() {
                return hashset.size();
            }
        };

    private:
        pset rocks;
        pset set_sand;
        point spawn;
        int max_depth;
        int min_x, max_x;
        
        void move_sand(point& p, bool floor) {
            auto blocked = [this, floor](point p) {
                return (floor && p.second == max_depth+2) || this->rocks.contains(p) || this->set_sand.contains(p);
            };

            point n_p = p;

            while(true) {
                // drop down as far as possible
                n_p = {n_p.first, std::min(set_sand.upper_bound(n_p), std::min(rocks.upper_bound(n_p), max_depth+2))-1};

                if(!floor && n_p.second > max_depth) {
                    return;
                } else if(!blocked({n_p.first-1, n_p.second+1})) { // down to the left
                    if(n_p.second <= max_depth+2) n_p = point(n_p.first-1, n_p.second+1);
                } else if(!blocked({n_p.first+1, n_p.second+1})) { // down to the right 
                    if(n_p.second <= max_depth+2) n_p = point(n_p.first+1, n_p.second+1);
                } else { // rest
                    if(n_p.first+1 > max_x) max_x = n_p.first+1;
                    if(n_p.first-1 < min_x) min_x = n_p.first-1;

                    set_sand.emplace(n_p);
                    return;
                }
            }
        }

        int run_to_fixpoint(bool print = false, bool floor = false) {
            int set_sand_size, sand_size;
            do {
                set_sand_size = set_sand.size();
                move_sand(spawn, floor);
                if(print) print_map();
            } while(set_sand.size() == 0 || !(set_sand_size == set_sand.size()));
            return set_sand.size();
        }

        void add_line(stringstream& line) {
            int x, y;
            char tempch;
            string tempstr;

            line >> x >> tempch >> y >> tempstr;
            point last = point(x,y);
            rocks.emplace(last);

            if(x > max_x) max_x = x;
            if(x < min_x) min_x = x;
            if(y > max_depth) max_depth = y;

            while(line >> x) {
                line >> tempch >> y >> tempstr;

                if(x > max_x) max_x = x;
                if(x < min_x) min_x = x;
                if(y > max_depth) max_depth = y;
            
                point diff_vec = point(x-last.first, y-last.second);
                diff_vec.first /= std::abs(diff_vec.first + diff_vec.second);
                diff_vec.second /= std::abs(diff_vec.first + diff_vec.second);

                point dest = point(x,y);
                point curr = last;
                while(curr != dest) {
                    curr.first += diff_vec.first;
                    curr.second += diff_vec.second;
                    rocks.emplace(curr);
                }

                last = dest;
            }
        }

    public:
        SandSimulator(string path, bool print = false) {
            ifstream input(path);
            rocks = pset();
            set_sand = pset();
            max_depth = MIN_INT;
            min_x = MAX_INT;
            max_x = MIN_INT;
            spawn = point(500,0);

            string line;
            while(getline(input, line)) {
                stringstream l(line);
                add_line(l);
            }

            if(print) {
                std::cout << "INITIAL SETUP" << std::endl;
                print_map();
            }
        }

        void print_map() {
            std::cout << "--------------------------------" << std::endl;
            pset set_moving_sand;
            for(int i = 0; i <= max_depth+1; i++) {
                for(int j = min_x; j <= max_x; j++) {
                    std::cout << (rocks.contains({j,i}) ? "#" : (set_sand.contains({j,i}) ? "o" : (point(j,i) == spawn ? "+" : ".")));
                }
                std::cout << std::endl;
            }

            for(int j = min_x; j <= max_x; j++) std::cout << "#";
            std::cout << std::endl;
        }

        int get_number_of_immobile(bool print = false) {
            return run_to_fixpoint(print);
        }

        int get_number_of_immobile_floored(bool print = false) {
            return run_to_fixpoint(print, true);
        }
};

int main() {
    SandSimulator sim("input/day14.txt");

    auto start = std::chrono::high_resolution_clock::now();
    std::cout << sim.get_number_of_immobile() << std::endl;
    std::cout << sim.get_number_of_immobile_floored() << std::endl;
    auto stop = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::microseconds>(stop - start);
    double time = duration.count();
    std::cout << "elapsed (ms): " << (time * 0.001) << std::endl;
}