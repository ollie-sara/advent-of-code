#include <iostream>
#include <fstream>
#include <map>
#include <stdexcept>
#include <sstream>
#include <cstring>
#include <cctype>

using namespace std;

struct item {
    string name;
    u_long size;
    enum {FILE, DIR} type;
};

struct dir : item {
    map<string, item*> items;
    dir* parent;
    dir(string n, dir* p){
        name = n;
        parent = p;
        items = map<string, item*>();
        size = 0L;
        type = DIR;
    }
};

struct file : item {
    dir* parent;
    file(string n, u_long i, dir* p){
        name = n;
        size = i;
        parent = p;
        type = FILE;
    }
};

u_long update_sizes(item* itm) {
    if(itm->type == item::FILE) {
        return itm->size;
    } else {
        u_long sum = 0L;
        map<string, item*>::iterator el = ((dir*) itm)->items.begin();
        while(el != ((dir*) itm)->items.end()) {
            sum += update_sizes(el->second);
            el++;
        }
        itm->size = sum;
        return sum;
    }
}

dir* create_filesystem() {
    ifstream input("input/day7.txt");

    string line;
    dir* root = new dir("", NULL);
    dir* curr = root;
    while(getline(input, line)) {
        if(line[0] == '$') {
            string cmd = line.substr(2,2);

            if(cmd == "cd") {
                string dirname = line.substr(5);
                dirname.erase(std::remove_if(dirname.begin(), dirname.end(), ::isspace), dirname.end());
                if(dirname == "..") {
                    if(curr->parent == NULL) 
                        throw invalid_argument("could not do <cd ..> as we are in sentinel");
                    curr = (dir*) curr->parent;
                } else {
                    if(!curr->items.contains(dirname)) {
                        cout << "+dir " + dirname + " in " + curr->name << endl;
                        dir* n_dir = new dir(dirname, curr);
                        curr->items[dirname] = n_dir;
                    }
                    curr = (dir*) (curr->items[dirname]);
                }
            } else if(cmd != "ls") {
                throw invalid_argument("command could not be parsed : " + cmd);
            }
        } else {
            stringstream it(line);
            if(line[0] == 'd') {
                string dirname;
                it >> dirname;
                it >> dirname;
                dirname.erase(std::remove_if(dirname.begin(), dirname.end(), ::isspace), dirname.end());
                if(!curr->items.contains(dirname)) {
                    cout << "+dir " + dirname + " in " + curr->name << endl;
                    dir* n_dir = new dir(dirname, curr);
                    curr->items[dirname] = n_dir;
                }
            } else {
                u_long size;
                string filename;
                it >> size;
                it >> filename;
                filename.erase(std::remove_if(filename.begin(), filename.end(), ::isspace), filename.end());
                if(!curr->items.contains(filename)) {
                    cout << "+file " + filename + " in " + curr->name << endl;
                    file* n_file = new file(filename, size, curr);
                    curr->items[filename] = n_file;
                }
            }
        }
    }
    update_sizes(root);
    return root;
}

u_long sum_max_100k(dir* d) {
    u_long sum = 0L;

    if(d->size <= 100'000L) sum += d->size;

    map<string, item*>::iterator el = d->items.begin();
    while(el != d->items.end()) {
        dir* it = (dir*) el->second;
        if(it->type == item::DIR){
            sum += sum_max_100k(it);
        }
        el++;
    }

    return sum;
}


dir* which_to_delete(u_long space, u_long needed, u_long current, dir* root) {
    u_long free = space - current;
    u_long to_free = needed - free;

    dir* min_dir = root;
    long frees = root->size - to_free;

    map<string, item*>::iterator iter = root->items.begin();
    while(iter != root->items.end()) {
        item* el = iter->second;
        if(el->type == item::DIR) {
            dir* n_min_dir = which_to_delete(space, needed, current, (dir*) el);
            long n_frees = n_min_dir->size - to_free;
            if(n_frees >= 0 && n_frees < frees) {
                min_dir = n_min_dir;
                frees = n_min_dir->size - to_free;
            }
        }

        iter++;
    }

    return min_dir;
}

void print_filesystem(item* itm, int tabs) {
    int increase = 1;

    auto tabprint = [tabs](string in){
        for(int o; o < tabs; o++) cout << ".   ";
        cout << in << endl;
    };
    
    if(itm->type == item::FILE) {
        char line[100] = {0};
        snprintf(line, 99, "f [%lu] %s", itm->size, itm->name.c_str());
        tabprint(line);
    } else {
        dir* itm2 = (dir*) itm;

        char line[100] = {0};
        snprintf(line, 99, "d [%lu] %s {", itm2->size, itm2->name.c_str());
        tabprint(line);
        
        for(auto iter = itm2->items.begin(); iter != itm2->items.end(); iter++) {
            print_filesystem(iter->second, tabs+increase);
        }

        tabprint("}");
    }
}

int main() {
    dir* root = create_filesystem();
    
    cout << sum_max_100k(root) << endl;
    cout << which_to_delete(70'000'000L, 30'000'000L, root->size, root)->size << endl;

    print_filesystem((item*) root, 0);

    return 0;
}





























//  frische sneaker ja genau so muss das sein