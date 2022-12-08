#include <iostream>
#include <fstream>
#include <cstring>

using namespace std;

int* string_to_range(const string &line) {
    static int ranges[4];

    int ix = 0;
    string substr = "";
    for(int i = 0; i <= line.length(); i++) {
        if((line.length() == i) || (line[i] == '-') || (line[i] == ',')) {
            ranges[ix++] = stoi(substr);
            substr = "";
        } else {
            substr = substr + line.substr(i,1);
        }
    }

    return ranges;
}

bool fully_covers(const int* inp) {
    return (inp[0] <= inp[2] && inp[1] >= inp[3]) || (inp[0] >= inp[2] && inp[1] <= inp[3]);
}

bool overlaps(const int* inp) {
    bool ab = inp[0] < inp[2] && inp[1] < inp[2];
    bool ba = inp[2] < inp[0] && inp[3] < inp[0];
    return !(ab || ba);
}

void task1_2() {
    ifstream input("./input/day4.txt");

    int sum1 = 0;
    int sum2 = 0;
    while(input.good()) {
        string line;
        getline(input, line);
        int* ranges = string_to_range(line);
        sum1 += fully_covers(ranges);
        sum2 += overlaps(ranges);
    }

    cout << sum1 << endl;
    cout << sum2 << endl;

    input.close();
    return;
}

int main() {
    
    task1_2();

    return 0;
}