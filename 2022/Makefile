DAY=5
CC=g++

all: run clean
run: $(addprefix day, $(addsuffix .cpp, $(DAY)))
	$(CC) $(addprefix day, $(addsuffix .cpp, $(DAY))) -o a.out
	./a.out

clean:
	@rm a.out