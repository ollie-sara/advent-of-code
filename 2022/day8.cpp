#include <iostream>
#include <fstream>
#include <vector>

using namespace std;

int count_visible(const vector<vector<int>> &map) {
    int w = map.size(), h = map[0].size();

    bool vis[h][w] = {{false}};
    
    // horizontal pass
    for(int x = 0; x < h; x++) {
        // up pass
        int lastsize = map[x][0];
        vis[x][0] = true;
        for(int y = 1; y < w; y++) {
            if(map[x][y] > lastsize) {
                lastsize = map[x][y];
                vis[x][y] = true;
            }
        }
        
        // down pass
        lastsize = map[x][w-1];
        vis[x][w-1] = true;
        for(int y = w-2; y >= 0; y--) {
            if(map[x][y] > lastsize) {
                lastsize = map[x][y];
                vis[x][y] = true;
            }
        }
    }

    // vertical pass
    for(int y = 0; y < w; y++) {
        // up pass
        int lastsize = map[0][y];
        vis[0][y] = true;
        for(int x = 1; x < h; x++) {
            if(map[x][y] > lastsize) {
                lastsize = map[x][y];
                vis[x][y] = true;
            }
        }
        
        // down pass
        lastsize = map[h-1][y];
        vis[h-1][y] = true;
        for(int x = h-2; x >= 0; x--) {
            if(map[x][y] > lastsize) {
                lastsize = map[x][y];
                vis[x][y] = true;
            }
        }
    }

    // count
    int count = 0;
    for(int x = 0; x < h; x++) {
        for(int y = 0; y < w; y++) {
            count += vis[x][y];
        }
    }

    return count;
}

int calc_scenic_score(const vector<vector<int>> &map, int x, int y) {
    int h = map.size(), w = map[0].size();
    int l = y != 0, r = y != w-1, u = x != 0, d = x != h-1;

    for(int i = x-1; i >= 0 && map[i][y] < map[x][y]; i--) u += i == 0 ? 0 : 1;
    for(int i = x+1; i < h && map[i][y] < map[x][y]; i++) d += i == h-1 ? 0 : 1;
    for(int i = y-1; i >= 0 && map[x][i] < map[x][y]; i--) l += i == 0 ? 0 : 1;
    for(int i = y+1; i < w && map[x][i] < map[x][y]; i++) r += i == w-1 ? 0 : 1;

    return l * r * u * d;
}

void task2(const vector<vector<int>> &map) {
    int max = -1;
    int h = map.size(), w = map[0].size();

    for(int x = 0; x < h; x++) {
        for(int y = 0; y < w; y++) {
            int score = calc_scenic_score(map, x, y);
            max = score > max ? score : max;
        }
    }

    cout << max << endl;

    return;
}

void task1(const vector<vector<int>> &map) {

    cout << count_visible(map) << endl;

    return;
}

vector<vector<int>> init_map() {
    // Helper function to turn characters to integers
    auto ctoi = [](char c){ return int(c)-48; };

    ifstream input("input/day8.txt");
    string line;

    vector<vector<int>> map;
 
    int r = 0;
    while(getline(input, line)) {
        map.push_back(vector<int>());
        for(auto &ch : line) {
            map[r].push_back(ctoi(ch));
        }
        r++;
    }

    return map;
}

int main() {

    vector<vector<int>> map = init_map();
    task1(map);
    task2(map);
    
    return 0;
}