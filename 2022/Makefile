DAY=15
CC=g++

.PHONY: all run time clean

all: run clean
run: $(addprefix day, $(addsuffix .cpp, $(DAY)))
	$(CC) $(addprefix day, $(addsuffix .cpp, $(DAY))) -o a.out -std=c++2a -Ofast -I/home/olive/cpp-libs/or-tools/include -Wall
	./a.out

time: hyperfine clean
	
hyperfine: $(addprefix day, $(addsuffix .cpp, $(DAY)))
	$(CC) $(addprefix day, $(addsuffix .cpp, $(DAY))) -o a.out -std=c++2a -Ofast
	hyperfine './a.out' -N --warmup 10
	./a.out

clean:
	@rm a.out