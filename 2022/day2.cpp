#include <iostream>
#include <fstream>
#include <string>
#include <stdexcept>

using namespace std;

enum choice { rock, paper, scissors };

choice stc(string &s) {
    switch(s[0]) {
        case 'A':
        case 'X':
            return rock;
        case 'B':
        case 'Y':
            return paper;
        case 'C':
        case 'Z':
            return scissors;
        default:
            throw invalid_argument("not a valid choice: <" + s + ">");
    }
}

int score(choice en, choice pl) {
    bool iswin = (pl == 0) ? ((pl+3)-en == 1) : (pl-en == 1);
    bool isdraw = en == pl;
    return isdraw ? 3 : (iswin ? 6 : 0);
}

void task1() {
    fstream input("input/day2.txt");

    // FIRST TASK
    int sum = 0;
    while(input.good()) {
        string enemy, player;
        input >> enemy;
        input >> player;
        sum += score(stc(enemy), stc(player)) + stc(player) + 1;
    }

    cout << sum << endl;
    return;
}

enum outcome { lose, draw, win };

outcome sto(string &s) {
    switch(s[0]) {
        case 'X': return lose;
        case 'Y': return draw;
        case 'Z': return win;
        default: throw invalid_argument("not a valid outcome: <" + s + ">");
    }
}

choice player(choice en, outcome ot) {
    switch(ot) {
        case lose: return static_cast<choice>((en+2)%3);
        case draw: return static_cast<choice>(en);
        case win: return static_cast<choice>((en+1)%3);
        default: throw invalid_argument("enum has to be one of the three options");
    }
}

void task2() {
    fstream input("input/day2.txt");

    // FIRST TASK
    int sum = 0;
    while(input.good()) {
        string enemy, outcome;
        input >> enemy;
        input >> outcome;
        choice pl = player(stc(enemy), sto(outcome));
        sum += score(stc(enemy), pl) + pl + 1;
    }

    cout << sum << endl;
    return;
}

int main() {
    
    task1();
    task2();

    return 0;
}