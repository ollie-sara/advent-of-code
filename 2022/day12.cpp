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
            int prio = 0;

            PriorityPoint(Point &p, int pr) {
                x = p.x;
                y = p.y;
                prio = pr;
            }

            PriorityPoint(int xx, int yy, int pr) {
                x = xx;
                y = yy;
                prio = pr;
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

        int find_path() {
            auto eligible = [this](PriorityPoint x, PriorityPoint y) {
                Point diff = {std::abs(x.x - y.x), std::abs(x.y - y.y)};
                return (std::abs(this->map[x.y][x.x] - this->map[y.y][y.x]) <= 1)
                    && (diff.x + diff.y == 1);
            };

            std::priority_queue<PriorityPoint, std::vector<PriorityPoint>, PointCompare> queue;
            PriorityPoint c = PriorityPoint(start, 0);

            bool visited[height][width];
            int dist[height][width];
            for(int i = 0; i < height; i++) {
                for(int j = 0; j < width; j++) {
                    visited[i][j] = false;
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
                visited[c.y][c.x] = true;

                PriorityPoint p = {c.x-1, c.y, c.prio+1};
                if(c.x != 0 && eligible(c, p)) queue.push(p);

                p = {c.x+1, c.y, c.prio+1};
                if(c.x != width-1 && eligible(c, p)) queue.push(p);

                p = {c.x, c.y-1, c.prio+1};
                if(c.y != 0 && eligible(c, p)) queue.push(p);
                
                p = {c.x, c.y+1, c.prio+1};
                if(c.y != height-1 && eligible(c, p)) queue.push(p);
            }

            for(int i = 0; i < height; i++) {
                for(int j = 0; j < width; j++) {
                    std::cout << (visited[i][j] ? "#" : ".");
                }
                std::cout << std::endl;
            }

            return dist[goal.y][goal.x];
        }
};

int main() {
    HeightMap map("input/day12.txt");
    std::cout << map.find_path() << std::endl;
    return 0;
}