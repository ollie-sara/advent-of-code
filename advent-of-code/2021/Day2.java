import java.util.Scanner;

public class Day2 implements AoCTask {

	String[] command = new String[1000];
	int[] amount = new int[1000];
	
	@Override
	public void readInput(Scanner scan) {
		for(int i = 0; i < 1000; i++) {
			command[i] = scan.next();
			amount[i] = scan.nextInt();
		}
	}

	@Override
	public String task1() {
		int hor = 0;
		int dep = 0;
		for(int i = 0; i < 1000; i++) {
			if(command[i].equals("up")) {
				dep -= amount[i];
			} else if(command[i].equals("forward")) {
				hor += amount[i];
			} else {
				dep += amount[i];
			}
		}
		
		return "" + (hor*dep);
	}

	@Override
	public String task2() {
		int hor = 0;
		int dep = 0;
		int aim = 0;
		for(int i = 0; i < 1000; i++) {
			if(command[i].equals("up")) {
				aim -= amount[i];
			} else if(command[i].equals("forward")) {
				hor += amount[i];
				dep += amount[i] * aim;
			} else {
				aim += amount[i];
			}
		}
		
		return "" + (hor*dep);
	}

}
