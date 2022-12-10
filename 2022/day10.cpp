#include <iostream>
#include <fstream>
#include <vector>
#include <chrono>

class CPU {
    private:
        int X = 1;
        int in_cycle = 0;
        int cycles_left = 0;
        int addx_value = 0;
        std::vector<long> signal_strength;
        std::ifstream instr_mem;
        bool draw = false;

        bool is_nl() {
            return (in_cycle+1)%40 == 0;
        }

        bool is_lit() {
            return in_cycle%40 >= X-1 && in_cycle%40 <= X+1;
        }

        bool is_measurement() {
            return (in_cycle-20)%40 == 0;
        }

        void cycle() {
            if(draw) {
                std::cout << (is_lit() ? '#' : '.');
                if(is_nl()) std::cout << std::endl;
            }

            in_cycle++;
            cycles_left--;
            if(is_measurement()) signal_strength.push_back(X * in_cycle);
            if(cycles_left == 0) X += addx_value;
        }

    public:
        CPU(std::string path, bool to_draw = false) {
            instr_mem = std::ifstream(path);
            draw = to_draw;
        }

        void run() {
            std::string instr;

            while(instr_mem >> instr) {
                if(instr == "addx") {
                    cycles_left = 2;
                    instr_mem >> addx_value;
                } else if(instr == "noop") {
                    cycles_left = 1;
                    addx_value = 0;
                }

                while(cycles_left > 0) {
                    cycle();
                }
            }
        }        

        long get_sum_signal_strength() {
            long sum = 0L;
            for(const auto& sig : signal_strength) {
                sum += sig;
            }
            return sum;
        }

        void print_signal_strength() {
            int val = 20;
            for(const auto& sig : signal_strength) {
                std::cout << val << ":\t" << sig << std::endl;
                val += 40;
            }
        }
};

void task() {
    CPU cpu("input/day10.txt", true);
    cpu.run();

    // cpu.print_signal_strength();
    std::cout << cpu.get_sum_signal_strength() << std::endl;

    return;
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