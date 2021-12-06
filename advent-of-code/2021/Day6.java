import java.util.ArrayList;
import java.util.Scanner;

public class Day6 implements AoCTask {

	ArrayList<Integer> fish = new ArrayList<>();
	
	@Override
	public void readInput(Scanner scan) {
		for(String s : scan.nextLine().split(",")) {
			fish.add(Integer.parseInt(s));
		}
	}

	@Override
	public String task1() {
		ArrayList<Integer> fish = new ArrayList<>();
		fish.addAll(this.fish);
		
		for(int i = 0; i < 80; i++) {
			ArrayList<Integer> newFish = new ArrayList<>();
			for(int j = 0; j < fish.size(); j++) {
				int o = fish.get(j);
				if(--o == -1) {
					newFish.add(8);
					o = 6;
				}
				fish.set(j, o);
			}
			fish.addAll(newFish);
		}
		
		return "" + fish.size();
	}

	@Override
	public String task2() {
		long[] fish = new long[9];
		for(Integer f : this.fish) {
			fish[f]++;
		}
		
		for(int i = 0; i < 256; i++) {
			long temp = fish[0];
			for(int j = 1; j < 9; j++) {
				fish[j-1] = fish[j];
			}
			fish[8] = temp;
			fish[6] += temp;
		}
		
		long sum = 0;
		for(long l : fish) {
			sum += l;
		}
		
		return "" + sum;
	}

}
