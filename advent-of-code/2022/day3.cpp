#include <iostream>
#include <fstream>
#include <unordered_set>

using namespace std;

void task1() {
    ifstream input("./input/day3.txt");

    int sum = 0;
    while(input.good()){
        unordered_set<char> in_first;
        string line;
        getline(input, line);

        for(int i = 0; i < line.length()/2; i++) {
            in_first.insert(line[i]);
        }

        for(int i = line.length()/2; i < line.length(); i++) {
            if(in_first.count(line[i]) != 0) {
                int val = int(line[i]);
                val -= (val < 97) ? 38 : 96;
                sum += val;
                break;
            }
        }

    }
    cout << sum << endl;

    input.close();
    return;
}

void task2() {
    ifstream input("./input/day3.txt");

    int sum = 0;
    while(input.good()) {
        unordered_set<char> maybe_badge;
        for(int e = 0; e < 3; e++) {
            string elf;
            getline(input, elf);

            if(e == 0) {
                maybe_badge.clear();
                for(int i = 0; i < elf.length(); i++) {
                    maybe_badge.insert(elf[i]);
                }
            } else {
                unordered_set<char> new_maybe_badge;
                for(int i = 0; i < elf.length(); i++) {
                    if(maybe_badge.count(elf[i]) != 0) new_maybe_badge.insert(elf[i]);
                }
                maybe_badge = new_maybe_badge;
            }

            if(e == 2) {
                int val = int(*(maybe_badge.begin()));
                val -= (val < 97) ? 38 : 96;
                sum += val;
                break;
            }
        }
    }
    
    cout << sum << endl;

    input.close();
    return;
}

int main() {
    
    task1();
    task2();

    return 0;
}