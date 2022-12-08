import java.util.Scanner;

public class Day1 implements AoCTask {
	
	int[] input = new int[2000];
	
	@Override
	public void readInput(Scanner scan) {
		for(int i = 0; i < 2000; i++) {
			input[i] = scan.nextInt();
		}
	}

	@Override
	public String task1() {
		int count = 0;
		for(int i = 1; i < 2000; i++) {
			count += input[i] > input[i-1] ? 1 : 0;
		}
		return "" + count;
	}

	@Override
	public String task2() {
		int count = 0;
		int[] windows = new int[1998];
		for(int i = 0; i < 2000; i++) {
			for(int j = 0; j >= -2; j--) {
				if(i-j >= 0 && i-j < 1998) {
					windows[i-j] += input[i];
				}
			}
		}
		
		for(int i = 1; i < 1998; i++) {
			count += windows[i] > windows[i-1] ? 1 : 0;
		}
		
		return "" + count;
	}

}
