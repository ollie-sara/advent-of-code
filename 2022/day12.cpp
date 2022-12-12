#include <fstream>
#include <iostream>
#include <vector>
#include <queue>

class HeightMap {
    public:
        struct Point {
            int x, y;
        };

        struct PriorityPoint {
            int x, y;
            enum Dir {LEFT = 1, RIGHT = 2, DOWN = 3, UP = 4} dir;
            int prio = 0;

            PriorityPoint(Point &p, int pr) {
                x = p.x;
                y = p.y;
                prio = pr;
            }

            PriorityPoint(int xx, int yy, int pr, Dir d) {
                x = xx;
                y = yy;
                prio = pr;
                dir = d;
            }

            bool operator==(Point &s) {
                return (x == s.x) && (y == s.y);
            } 

            bool operator!=(Point &s) {
                return !(*this == s);
            }
        };

        struct PointCompare {
            bool operator()(PriorityPoint const &a, PriorityPoint const &b) {
                return (a.prio > b.prio);
            }
        };

        typedef std::vector<std::vector<int>> Map;

    private:
        Map map;
        Point start, goal;
        int width, height;

    public:
        HeightMap(const std::string &path) {
            std::ifstream input(path);
            std::string line;

            map = Map();
            int y = 0;
            while(getline(input, line)) {
                int x = 0;
                map.push_back(std::vector<int>());
                for(int i = 0; i < line.length(); i++) {
                    map[y].push_back(0);
                    if(line[i] == 'S') {
                        start = {x,y};
                        map[y][x++] = 0;
                    }
                    else if(line[i] == 'E') {
                        goal = {x,y};
                        map[y][x++] = 25;
                    }
                    else map[y][x++] = int(line[i]) - 97;
                }
                y++;
            }

            width = map[0].size();
            height = map.size();
        }

        int find_a_path() {
            Point temp = start;
            int min = 99999999;

            for(int i = 0; i < height; i++) {
                for(int j = 0; j < width; j++) {
                    if(map[i][j] == 0) start = {j, i};
                    int length = find_path();
                    if(length < min) min = length;
                }
            } 

            start = temp;
            return min;
        }

        int find_path() {
            auto eligible = [this](PriorityPoint from, PriorityPoint to) {
                Point diff = {std::abs(from.x - to.x), std::abs(from.y - to.y)};
                bool out = (this->map[to.y][to.x] - this->map[from.y][from.x] <= 1)
                        && (diff.x + diff.y == 1);
                return out;
            };


            std::priority_queue<PriorityPoint, std::vector<PriorityPoint>, PointCompare> queue;
            PriorityPoint c = PriorityPoint(start, 0);

            int visited[height][width];
            int dist[height][width];
            for(int i = 0; i < height; i++) {
                for(int j = 0; j < width; j++) {
                    visited[i][j] = 0;
                    dist[i][j] = 999999;
                }
            }
            
            queue.push(c);
            int i = 0;
            while(c != goal && !queue.empty()) {
                c = queue.top();
                queue.pop();
                
                if(dist[c.y][c.x] > c.prio) dist[c.y][c.x] = c.prio;
                if(visited[c.y][c.x]) continue;
                visited[c.y][c.x] = c.dir;

                PriorityPoint p = {c.x-1, c.y, c.prio+1, PriorityPoint::LEFT};
                if(c.x != 0 && eligible(c, p)) queue.push(p);

                p = {c.x+1, c.y, c.prio+1, PriorityPoint::RIGHT};
                if(c.x != width-1 && eligible(c, p)) queue.push(p);

                p = {c.x, c.y-1, c.prio+1, PriorityPoint::UP};
                if(c.y != 0 && eligible(c, p)) queue.push(p);
                
                p = {c.x, c.y+1, c.prio+1, PriorityPoint::DOWN};
                if(c.y != height-1 && eligible(c, p)) queue.push(p);
            }

            // for(int i = 0; i < height; i++) {
            //     for(int j = 0; j < width; j++) {
            //         switch(visited[i][j]) {
            //             case 0: std::cout << "."; break;
            //             case PriorityPoint::LEFT: std::cout << "<"; break;
            //             case PriorityPoint::UP: std::cout << "^"; break;
            //             case PriorityPoint::DOWN: std::cout << "v"; break;
            //             case PriorityPoint::RIGHT: std::cout << ">"; break;
            //         }
            //     }
            //     std::cout << std::endl;
            // }

            return dist[goal.y][goal.x];
        }
};

int main() {
    HeightMap map("input/day12.txt");
    std::cout << map.find_path() << std::endl;
    std::cout << map.find_a_path() << std::endl;
    return 0;
}