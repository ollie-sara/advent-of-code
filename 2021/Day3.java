import java.util.LinkedList;
import java.util.Scanner;

public class Day3 implements AoCTask {

	String[] binNums = new String[1000];
	String most, least;
	
	@Override
	public void readInput(Scanner scan) {
		for(int i = 0; i < 1000; i++) {
			binNums[i] = scan.nextLine();
		}
	}

	@Override
	public String task1() {
		int[] count = new int[12];
		for(int i = 0; i < 1000; i++) {
			for(int j = 0; j < 12; j++) {
				if(binNums[i].charAt(j) == '1') {
					count[j] += 1;
				}
			}
		}
		
		most = "";
		least = "";
		
		for(int i = 0; i < 12; i++) {
			if(count[i] >= 500) {
				most += "1";
				least += "0";
			} else {
				most += "0";
				least += "1";
			}
		}
		
		return "" + (Integer.parseInt(most, 2) * Integer.parseInt(least, 2));
	}

	@Override
	public String task2() {
		LinkedList<String> oxy = new LinkedList<>();
		LinkedList<String> co2 = new LinkedList<>();
		for(String s : binNums) {
			oxy.add(s);
			co2.add(s);
		}
		
		char mostCom = most.charAt(0);
		int i = 0;
		while(oxy.size() > 1) {
			LinkedList<String> newOxy = new LinkedList<>();
			for(String s : oxy) {
				if(s.charAt(i) == mostCom) newOxy.add(s);
			}
			
			if(newOxy.size() == 0) {
				newOxy.add(oxy.getLast());
				oxy = newOxy;
				break;
			}
			
			if(newOxy.size() == 1) {
				oxy = newOxy;
				break;
			}
			
			int count = 0;
			i++;
			for(String s : newOxy) {
				if(s.charAt(i) == '1') {
					count++;
				}
			}
			if(count >= (double) newOxy.size()/2.0) mostCom = '1';
			else mostCom = '0';
			
			oxy = newOxy;
		}
		
		i = 0;
		char leastCom = least.charAt(0);
		while(co2.size() > 1) {
			LinkedList<String> newCo2 = new LinkedList<>();
			for(String s : co2) {
				if(s.charAt(i) == leastCom) newCo2.add(s);
			}
			
			if(newCo2.size() == 0) {
				newCo2.add(co2.getLast());
				co2 = newCo2;
				break;
			}
			
			if(newCo2.size() == 1) {
				co2 = newCo2;
				break;
			}
			
			int count = 0;
			i++;
			for(String s : newCo2) {
				if(s.charAt(i) == '1') {
					count++;
				}
			}
			if(count >= (double) newCo2.size()/2.0) leastCom = '0';
			else leastCom = '1';
			
			co2 = newCo2;
		}
		
		return "" + (Integer.parseInt(co2.getFirst(), 2) * Integer.parseInt(oxy.getFirst(), 2));
	}

}
