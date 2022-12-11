#include <fstream>
#include <iostream>
#include <queue>
#include <stdio.h>
#include <functional>
#include <unordered_map>
#include <sstream>
#include <numeric>
#include <chrono>

class Monkey {
    public:
        struct Monkeys {
            std::unordered_map<int, Monkey*> list;
            long lcm = 1;

            Monkey* operator[](const int &i) {
                return list[i];
            }

            long get_monkey_business() {
                long max = 0;
                long max2 = 0;
                
                for(const auto& mon : list) {
                    long level = mon.second->get_activity();
                    if(level > max) {
                        max2 = max;
                        max = level;
                    } else if(level > max2) {
                        max2 = level;
                    }
                }

                std::cout << max << " " << max2 << std::endl;
                return max * max2;
            }

            void round(bool sane) {
                for(int i = 0; i < list.size(); i++) {
                    list[i]->round(sane);
                }
            }

            void rounds(int rounds, bool sane = true) {
                for(int i = 0; i < rounds; i++) {
                    round(sane);
                }
            }

            void update_lcm() {
                for(int i = 0; i < list.size(); i++) {
                    if(i == 0) lcm = list[i]->divisor;
                    else lcm = std::lcm(lcm, list[i]->divisor);
                }
            }
        };

    private:
        std::queue<long> items;
        std::function<long(long&)> operation;
        long divisor;
        std::function<void(Monkey*, long&)> do_true;
        std::function<void(Monkey*, long&)> do_false;
        long activity = 0;
        Monkeys* monkeys;

        /**
         * @brief Parses the first line from the monkey declaration,
         * and saves it in Monkey::items.
         * 
         * @param line "Starting items: <int>, <int>, ..., <int>"
         */
        void parse_items(std::string &line) {
            std::string temp;
            long item;
            std::stringstream stream(line);
            
            stream >> temp >> temp;

            while(stream >> item) {
                items.push(item);
                stream >> temp;
            }
        }

        /**
         * @brief Parses the second line from the monkey declaration,
         * and saves it in Monkey::operation.
         * 
         * @param line "Operation: new = <string> <binop> <string>" 
         * 
         */
        void parse_operation(std::string &line) {
            std::string opr1, opr2;
            char op;

            std::stringstream stream(line);
            stream >> opr1 >> opr1 >> opr1 >> opr1 >> op >> opr2;

            auto binop = [op](long a, long b) {
                switch(op) {
                    case '*': return a * b;
                    case '+': return a + b;
                    case '-': return a - b;
                    case '/': return a / b;
                }

                return (long) 1;
            };

            auto func = [opr1, opr2, binop](long &worry) {
                return binop((opr1 == "old" ? worry : stoi(opr1)), (opr2 == "old" ? worry : stoi(opr2)));
            };

            operation = func;
        }

        /**
         * @brief Parses the third line from monkey declaration,
         * and saves it in Monkey::test
         * 
         * @param line "Test: divisible by <int>"
         */
        void parse_test(std::string &line) {
            std::string temp;
            std::stringstream stream(line);
            stream >> temp >> temp >> temp >> divisor;
        }

        /**
         * @brief Parses last two lines from monkey declaration,
         * and saves it in do_true/do_false.
         * 
         * @param line "If <is_true>: throw to monkey <int>"
         * @param is_true 
         */
        void parse_do(std::string &line, bool is_true) {
            int to;
            std::string temp;
            std::stringstream stream(line);

            stream >> temp >> temp >> temp >> temp >> temp >> to;

            auto func = [to](Monkey *monk, long &worry){
                monk->items.pop();
                (*monk->monkeys)[to]->catch_item(worry);
            };

            if(is_true)
                do_true = func;
            else
                do_false = func;
        }

    public:
        Monkey(Monkeys &mon, std::ifstream &input) {
            monkeys = &mon;

            std::string line;
            getline(input, line); // Monkey #:

            getline(input, line); // Starting items: ...
            items = std::queue<long>();
            parse_items(line);

            getline(input, line); // Operation: ...
            parse_operation(line);

            getline(input, line); // Test: ...
            parse_test(line);

            getline(input, line); // If true: ...
            parse_do(line, true);

            getline(input, line); // If false: ...
            parse_do(line, false);

            getline(input, line);
        }

        void round(bool sane) {
            while(!items.empty()){
                long item = items.front();
                item = operation(item);
                if(sane) item = item / 3;
                else item = item % monkeys->lcm;
                activity++;
                if(item % divisor == 0) do_true(this, item);
                else do_false(this, item);
            }
        }

        long get_activity() {
            return activity;
        }

        void catch_item(long &item) {
            items.push(item);
        }
};

Monkey::Monkeys* get_monkeys() {
    std::ifstream input("input/day11.txt");
    
    static Monkey::Monkeys monkeys;
    int i = 0;
    while(input.good()) {
        monkeys.list[i++] = new Monkey(monkeys, input);
    }
    monkeys.update_lcm();

    return &monkeys;
}

int main() {
    auto start = std::chrono::high_resolution_clock::now();
    
    Monkey::Monkeys monkeys1 = *get_monkeys();
    monkeys1.rounds(20);
    std::cout << monkeys1.get_monkey_business() << std::endl;

    Monkey::Monkeys monkeys2 = *get_monkeys();
    monkeys2.rounds(10'000, false);
    std::cout << monkeys2.get_monkey_business() << std::endl;

    auto stop = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::microseconds>(stop - start);
    double time = duration.count();
    std::cout << "elapsed (ms): " << (time * 0.001) << std::endl;

    return 0;
}