#include <fstream>
#include <iostream>
#include <set>
#include <unordered_map>
#include <unordered_set>
#include <sstream>
#include <vector>
#include <chrono>

class RangeMark {
    struct Range {
        int start, end;

        Range(int s, int e) {
            start = s;
            end = e;
        }

        bool operator<(const Range& b) const {
            return end <= b.end;
        }

        bool operator>(const Range& b) const {
            return end >= b.end;
        }

        int size() const {
            return end - start + 1;
        }
    };

    typedef std::set<Range, std::less<Range>> rangeset;

    private:
        rangeset marked;
    
    public:
        void mark(const Range& r) {
            if(r.start > r.end) throw std::invalid_argument("start cannot be after end");

            if(marked.size() == 0) {
                marked.insert(r);
                return;
            }

            Range to_add(r.start, r.end);
            rangeset::iterator up = marked.upper_bound(to_add);
            rangeset::iterator low = up;

            if(up != marked.end() && (*up).start-1 <= to_add.end) {
                to_add.end = (*up).end;
                up++;
            }
            else if((*up).start-1 > to_add.start) {
                if(up == marked.begin()) {
                    marked.insert(to_add);
                    return;
                } 
            } 

            while(low == marked.end() || (low != marked.begin() && (*low).start >= to_add.start)) low--;
            if((*low).start < to_add.start && (*low).end >= to_add.start-1) to_add.start = (*low).start;
            else if((*low).start < to_add.start) low++;

            marked.erase(low, up);
            marked.insert(to_add);
        }

        void print() {
            for(Range r : marked) {
                std::cout << r.start << "-" << r.end << ";" << std::endl;
            }
        }

        int amt_marked() {
            int sum = 0;
            for(const Range& r : marked) {
                sum += r.size();
            }
            return sum;
        }

        bool contained(const Range& r) const {
            rangeset::iterator upper = marked.upper_bound(r);
            if(upper == marked.end() || (*upper).start > r.start) return false;
            return true;
        }

        int num_ranges() {
            return marked.size();
        }

        int first_free() {
            return (*marked.begin()).end + 1;
        }

};

using std::string;
using std::vector;
using std::stringstream;

struct FileData {

    struct SensorData {
        int sx, sy, bx, by;
    };

    vector<SensorData> data;

    FileData(const string& path) {
        std::ifstream input(path);
        string line;

        while(getline(input, line)) {
            int sx, sy, bx, by;
            int pos1 = line.find("=")+1;
            int pos2 = line.find("=", pos1)+1;
            int pos3 = line.find("=", pos2)+1;
            int pos4 = line.find("=", pos3)+1;

            stringstream(line.substr(pos1)) >> sx;
            stringstream(line.substr(pos2)) >> sy;
            stringstream(line.substr(pos3)) >> bx;
            stringstream(line.substr(pos4)) >> by;

            data.push_back({sx, sy, bx, by});
        }
    }
};

FileData task1(int y = 2000000) {
    FileData data("input/day15.txt");

    auto mandist = [](const FileData::SensorData& d) {
        return std::abs(d.bx - d.sx) + std::abs(d.by - d.sy);
    };

    RangeMark rm;
    for(FileData::SensorData& d : data.data) {
        int md = mandist(d);
        int dist_to_y = mandist({d.sx, d.sy, d.sx, y});
        if(dist_to_y <= md) {
            rm.mark({d.sx - (md-dist_to_y), d.sx + (md-dist_to_y)});
        }
    }

    std::unordered_set<int> locs;
    for(FileData::SensorData& d : data.data) {
        if(d.sy == y && rm.contained({d.sx, d.sx})) locs.emplace(d.sx);
        if(d.by == y && rm.contained({d.bx, d.bx})) locs.emplace(d.bx);
    }
    std::cout << (rm.amt_marked() - locs.size()) << std::endl;
    return data;
}

using std::unordered_map;

void task2(FileData& dt) {
    auto mandist = [](const FileData::SensorData& d) {
        return std::abs(d.bx - d.sx) + std::abs(d.by - d.sy);
    };

    unordered_map<int, RangeMark> marks;

    for(FileData::SensorData& d : dt.data) {
        int md = mandist(d);
        for(int i = std::max(d.sy-md, 0); i <= std::min(d.sy+md, 4'000'000); i++) {
            int y = std::abs(d.sy - i);
            if(!marks.contains(i)) marks[i] = RangeMark();
            marks[i].mark({std::max(d.sx - (md - y), 0), std::min(d.sx + (md - y), 4'000'000)});
        }
    }

    for(std::pair<const int, RangeMark>& rm : marks) {
        if(rm.second.num_ranges() > 1) {
            long long num = (rm.second.first_free())*4'000'000L + rm.first;
            std::cout << num << std::endl;
        }
    }
} 

int main() {
    auto start = std::chrono::high_resolution_clock::now();
    FileData dt = task1();
    task2(dt);
    auto stop = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::microseconds>(stop - start);
    double time = duration.count();
    std::cout << "elapsed (ms): " << (time * 0.001) << std::endl;

    return 0;
}