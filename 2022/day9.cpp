#include <iostream>
#include <fstream>
#include <unordered_set>
#include <stdexcept>
#include <math.h>
#include <vector>
#include <chrono>

#define _USE_MATH_DEFINES

using namespace std;

struct pairhash {
    size_t operator()(const std::pair<int, int>& p) const {
        return p.first ^ p.second;
    }
};

typedef unordered_set<pair<int, int>, pairhash> pointlist;

struct vec2 {
    double x;
    double y;

    bool operator==(const vec2& oth) {
        return x == oth.x && y == oth.y;
    }

    vec2 operator/(const double& div) {
        return vec2{x/div, y/div};
    }

    vec2 clamp() {
        return vec2{min(max(x, -1.), 1.), min(max(y, -1.), 1.)};
    }

    vec2 operator+(const vec2& oth) {
        return vec2{x+oth.x, y+oth.y};
    }

    pair<int, int> tp() {
        return pair(x, y);
    }

    double norm() {
        return sqrt(x*x + y*y);
    }
};

struct snake {
    int size;
    vector<vec2> members;
    pointlist visited;

    snake(int n) {
        size = n;
        members = vector<vec2>(n);
        for(int i = 0; i < n; i++) {
            members[i] = {0,0};
        }
        visited = pointlist();
        visited.insert(pair(0,0));
    }

    void move_head(char dir) {
        vec2* h = &members[0];
        switch(dir) {
            case 'D':
                h->y--;
                break;
            case 'U':
                h->y++;
                break;
            case 'L':
                h->x--;
                break;
            case 'R':
                h->x++;
                break;
            default:
                throw invalid_argument("dir has to be U,D,L,R");
        }

        for(int i = 1; i < size; i++) {
            vec2* t = &members[i];
            if(vec2{h->x - t->x, h->y - t->y}.norm() > 1.42) {
                vec2 wv = vec2{ h->x - t->x, h->y - t->y }.clamp();
                *t = *t + wv;
                if(i == size-1) visited.insert(t->tp());
            }
            h = t;
        }
    }
};

void task(int n) {
    ifstream input("input/day9.txt");
    snake snake(n);

    char dir;
    int times;
    input >> dir;
    while(input >> times) {
        for(int i = 0; i < times; i++) {
            snake.move_head(dir);
        }
        input >> dir;
    }

    cout << snake.visited.size() << endl;
}

int main() {
    auto start = chrono::high_resolution_clock::now();
    task(2);
    task(10);
    auto stop = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::microseconds>(stop - start);
    cout << duration.count() << " microseconds" << endl;
    return 0;
}