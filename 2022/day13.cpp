#include <fstream>
#include <iostream>
#include <vector>
#include <sstream>
#include <stack>
#include <queue>
#include <chrono>

struct Value {
    enum Type {INT, LIST} type; 
    int integer;
    std::vector<Value*> list;

    Value(Type tp) {
        type = tp;
        if(tp == LIST) list = std::vector<Value*>();
    }

    Value(std::string &line) {
        std::stringstream feed(line.substr(1, line.size()-2));
        int num;
        char temp;

        type = LIST;
        list = std::vector<Value*>();
        Value* current = this;
        std::stack<Value*> last;
        while(feed >> num || !feed.eof()) {
            if(feed.fail()) {
                feed.clear();
                feed >> temp;
                if(temp == '[') {
                    Value* next = new Value(LIST);
                    current->list.push_back(next);
                    last.push(current);
                    current = next;
                } else if(temp == ']'){
                    current = last.top();
                    last.pop();
                }  
            } else {
                Value* n = new Value(INT);
                n->integer = num;
                current->list.push_back(n); 
            }
        }
    }

    std::string to_string() const {
        if(type == INT) return std::to_string(integer);
        std::ostringstream out;
        out << "[";

        for(int i = 0; i < list.size(); i++) {
            out << list[i]->to_string();
            if(i != list.size()-1) out << ",";
        }

        out << "]";
        return out.str();
    }

    bool operator==(const Value& p) const {
        if(type == INT && p.type == INT) return integer == p.integer;
        else if(type == LIST && p.type == LIST) {
            if(list.size() != p.list.size()) return false;
            for(int i = 0; i < list.size(); i++) {
                Value nlhs = *list[i];
                Value nrhs = *(p.list[i]);
                if(!(nlhs == nrhs)) return false;
            }

            return true;
        } else {
            if(p.type == INT) {
                Value v(LIST);
                Value i(INT);
                i.integer = p.integer;
                v.list.push_back(&i);
                return *this == v;
            } else {
                Value v(LIST);
                Value i(INT);
                i.integer = integer;
                v.list.push_back(&i);
                return v == p;
            }
        }
    }

    bool operator<(const Value& p) const {
        if(type == INT && p.type == INT) {
            return integer < p.integer;
        } else if(type == LIST && p.type == LIST) {
            for(int i = 0; i < list.size(); i++) {
                if(i == list.size()) return true;
                if(i == p.list.size()) return false;
                Value nlhs = *list[i];
                Value nrhs = *(p.list[i]);
                if(nlhs == nrhs) continue;
                return nlhs < nrhs;
            }

            return true;
        } else {
            if(p.type == INT) {
                Value v(LIST);
                Value i(INT);
                i.integer = p.integer;
                v.list.push_back(&i);
                return *this < v;
            } else {
                Value v(LIST);
                Value i(INT);
                i.integer = integer;
                v.list.push_back(&i);
                return v < p;
            }
        }
    }

    bool operator >(const Value& p) const {
        return p < *this;
    }
};

void task() {
    std::ifstream input("input/day13.txt");
    std::string line;

    int i = 1;
    int sum = 0;
    std::priority_queue<Value, std::vector<Value>, std::greater<Value>> vls;
    while(getline(input, line)) {
        Value v1(line);
        getline(input, line);
        Value v2(line);
        getline(input, line);

        bool isOrdered = v1 < v2;
        if(isOrdered) sum += i;

        vls.push(v1);
        vls.push(v2);

        i++;
    }

    std::cout << sum << std::endl;

    std::string st1 = "[[2]]";
    std::string st2 = "[[6]]";
    Value div1(st1);
    Value div2(st2);

    int mul = 1;
    i = 1;
    while(!vls.empty()) {
        Value v = vls.top();
        vls.pop();
        if(v == div1) mul *= i;
        if(v == div2) mul *= i;
        i++;
    }

    std::cout << mul << std::endl;
}

int main() {
    auto start = std::chrono::high_resolution_clock::now();
    task();
    auto stop = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::microseconds>(stop - start);
    double time = duration.count();
    std::cout << "elapsed (ms): " << (time * 0.001) << std::endl;
    return 0;
}