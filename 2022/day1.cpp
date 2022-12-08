#include <iostream>
#include <fstream>
#include <queue>

using namespace std;

void task1() {
    ifstream file("./input/day1.txt");

    int max = -1;
    int curr = 0;
    while(file.good()) {
        string line;
        getline(file, line);
        
        if(isdigit(line[0])) {
            curr += stoi(line);
        } else {
            if(curr > max) max = curr;
            curr = 0;
        }
    }

    cout << max << endl;

    file.close();
    return;
}

void task2() {
    ifstream file("./input/day1.txt");

    priority_queue<int> max;
    int curr = 0;
    while(file.good()) {
        string line;
        getline(file, line);
        
        if(isdigit(line[0])) {
            curr += stoi(line);
        } else {
            max.push(curr);
            curr = 0;
        }
    }
       
    int total = 0;
    for(int i = 0; i < 3; i++) {
        total += max.top();
        max.pop();
    }
    cout << total << endl;

    file.close();
    return;
}

int main() {
    
    task1();
    task2();

    return 0;
}