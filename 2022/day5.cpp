#include <iostream>
#include <fstream>
#include <stack>
#include <vector>

using namespace std;

string get_msg(vector<stack<char>> &stacks) {
    string out = "";
    
    for(int i = 0; i < 9; i++) {
        if(!stacks[i].empty()) {
            out += stacks[i].top();
        }
    }

    return out;
}

vector<stack<char>> init_stacks(ifstream &input) {
    vector<stack<char>> stacks(9);
    for(int i = 0; i < 9; i++) stacks[0] = stack<char>();
    for(int i = 0; i < 8; i++) {
        string line;
        getline(input, line);
        for(int j = 0; j < 9; j++) {
            if(line[1+j*4] != ' ') stacks[j].push(line[1+j*4]);
        }
    }

    string sizes = "";
    for(int i = 0; i < 9; i++) {
        stack<char> temp;
        while(!stacks[i].empty()) {
            temp.push(stacks[i].top());
            stacks[i].pop();
        }
        stacks[i] = temp;
        sizes += to_string(stacks[i].size());
    }

    string temp;

    cout << "init: " << get_msg(stacks) << endl;
    cout << "size: " << sizes << endl;

    getline(input, temp);
    getline(input, temp);
    
    return stacks;
}

int* get_instr(ifstream &input) {
    static int instr[3];
    string temp;
    input >> temp;
    input >> temp;
    instr[0] = stoi(temp);
    input >> temp;
    input >> temp;
    instr[1] = stoi(temp) - 1;
    input >> temp;
    input >> temp;
    instr[2] = stoi(temp) - 1;
    return instr;
}

void exec_instr(int* instr, vector<stack<char>> &stacks) {
    
    for(int i = 0; i < instr[0] && !stacks[instr[1]].empty(); i++) {
        stacks[instr[2]].push(stacks[instr[1]].top());
        stacks[instr[1]].pop();
        if(stacks[instr[1]].empty() && i < instr[0]-1) cout << "emptied before finishing operation" << endl;
    }
    
    return;
}

void exec_instr_9001(int* instr, vector<stack<char>> &stacks) {
    
    stack<char> temp;
    for(int i = 0; i < instr[0] && !stacks[instr[1]].empty(); i++) {
        temp.push(stacks[instr[1]].top());
        stacks[instr[1]].pop();
        if(stacks[instr[1]].empty() && i < instr[0]-1) cout << "emptied before finishing operation" << endl;
    }

    while(!temp.empty()) {
        stacks[instr[2]].push(temp.top());
        temp.pop();
    }
    
    return;
}

void task1() {
    ifstream input("./input/day5.txt");
    vector<stack<char>> stacks = init_stacks(input);
    
    while(input.good()) {
        int* instr = get_instr(input);
        exec_instr(instr, stacks);
    }

    cout << get_msg(stacks) << endl;

    return;
}

void task2() {
    ifstream input("./input/day5.txt");
    vector<stack<char>> stacks = init_stacks(input);
    
    while(input.good()) {
        int* instr = get_instr(input);
        exec_instr_9001(instr, stacks);
    }

    cout << get_msg(stacks) << endl;

    return;
}

int main() {
    
    task1();
    task2();

    return 0;
}