import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Day7 implements AoCTask {

	int[] crabs;
	int maxPos = Integer.MIN_VALUE;
	
	@Override
	public void readInput(Scanner scan) {
		ArrayList<Integer> out = new ArrayList<>();
		scan.useDelimiter(",|[\n\r\s]+");
		while(scan.hasNextInt()) {
			out.add(scan.nextInt());
		}
		crabs = new int[out.size()];
		for(int i = 0; i < out.size(); i++) {
			crabs[i] = out.get(i);
			maxPos = Math.max(maxPos, crabs[i]);
		}
	}

	@Override
	public String task1() {
		int[] fuel = new int[maxPos+1];
		int minFuelIndex = -1;
		int minFuel = Integer.MAX_VALUE;
		
		for(int i = 0; i < fuel.length; i++) {
			for(int c = 0; c < crabs.length; c++) {
				fuel[i] += Math.abs(i - crabs[c]);
			}
			if(fuel[i] < minFuel) {
				minFuel = fuel[i];
				minFuelIndex = i;
			}
		}
		
		return "" + minFuel;
	}

	@Override
	public String task2() {
		int[] fuel = new int[maxPos+1];
		int minFuelIndex = -1;
		int minFuel = Integer.MAX_VALUE;
		
		for(int i = 0; i < fuel.length; i++) {
			for(int c = 0; c < crabs.length; c++) {
				int n = Math.abs(i - crabs[c]);
				fuel[i] += (n*(n+1))/2;
			}
			if(fuel[i] < minFuel) {
				minFuel = fuel[i];
				minFuelIndex = i;
			}
		}
		
		return "" + minFuel;
	}

}
