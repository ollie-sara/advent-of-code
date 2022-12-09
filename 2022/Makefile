DAY=9
CC=g++

all: run clean
run: $(addprefix day, $(addsuffix .cpp, $(DAY)))
	$(CC) $(addprefix day, $(addsuffix .cpp, $(DAY))) -o a.out -std=c++2a
	./a.out

clean:
	@rm a.out