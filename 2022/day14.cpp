#include <fstream>
#include <iostream>
#include <unordered_set>
#include <vector>
#include <sstream>

using std::ifstream;
using std::stringstream;
using std::string;

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
        typedef std::unordered_set<point, pair_hash> pset;

    private:
        pset rocks;
        pset set_sand;
        plist sand;
        point spawn;
        int max_depth;
        int min_x, max_x;
        
        bool move_sand(point& p, plist* n, bool floor) {
            auto blocked = [n, this, floor](point p) {
                auto blocked_sand = [n](point& p) {
                    for(const auto& s : (*n)) {
                        if(p == s) return true;
                    }
                    return false;
                };
                return (floor && p.second == max_depth+2) || this->rocks.contains(p) || this->set_sand.contains(p) || blocked_sand(p);
            };

            if(p.first+1 > max_x) max_x = p.first+1;
            if(p.first-1 < min_x) min_x = p.first-1;

            if(!blocked({p.first, p.second+1})) { // directly down
                if(p.second <= max_depth+2) (*n).push_back({p.first, p.second+1});
                return true;
            } else if(!blocked({p.first-1, p.second+1})) { // down to the left
                if(p.second <= max_depth+2) (*n).push_back({p.first-1, p.second+1});
                return true;
            } else if(!blocked({p.first+1, p.second+1})) { // down to the right 
                if(p.second <= max_depth+2) (*n).push_back({p.first+1, p.second+1});
                return true;
            } else { // rest
                set_sand.emplace(p);
                return false;
            }
        }

        bool spawn_blocked() {
            for(point p : sand) {
                if(p == spawn) return true;
            }
            return false;
        }

        int run_to_fixpoint(bool print = false, bool floor = false) {
            plist old_sand;
            do {
                old_sand = sand;
                sand = plist();
                for(point p : old_sand) {
                    !move_sand(p, &sand, floor);
                }
                if(!spawn_blocked()) sand.push_back(spawn);
                if(print) print_map();
            } while(!(old_sand == sand));
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
            sand = plist();
            max_depth = -1;
            min_x = 999999;
            max_x = -1;
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

    std::cout << sim.get_number_of_immobile() << std::endl;
    std::cout << sim.get_number_of_immobile_floored() << std::endl;
}