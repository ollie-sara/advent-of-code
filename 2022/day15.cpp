#include <fstream>
#include <iostream>
#include <stack>
#include <memory>

using std::shared_ptr;
using std::make_shared;

class RangeMark {
    struct Range {
        int free;
        int start, end;
        shared_ptr<Range> subrange_l;
        shared_ptr<Range> subrange_r;

        Range(int st, int en) {
            start = st;
            end = en;

            int diff = en - st;

            if(diff != 1) {
                subrange_l = make_shared<Range>(start, diff/2);
                subrange_r = make_shared<Range>((diff/2)+1, end);
            } else {
                subrange_l = NULL;
                subrange_r = NULL;
            }
        }
    };
};

int main() {
    RangeMark rm;
    return 0;
}