#include <iostream>
#include <fstream>

using namespace std;

bool is_unique_fixlen(string &s, int size) {
    if(s.length() != size) return false;
    bool contained[26] = {false};
    for(auto &c : s) {
        int vl = int(c) - 97;
        if(contained[vl]) return false;
        contained[vl] = true;
    }
    return true;
}

void task(int size) {
    ifstream file("./input/day6.txt");
    
    char ch;
    string buff = "";
    int count = 0;
    while (file >> ch) {
        buff += ch;
        if(buff.length() == size+1) buff = buff.substr(1,size);
        count++;
        if(is_unique_fixlen(buff, size)) {
            cout << count << endl;
            return;
        }
    }
}

int main() {

    task(4);
    task(14);

    return 0;
}