import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Day8 implements AoCTask {
	
	String[][] input = new String[200][10];
	String[][] output = new String[200][4];
	

	@Override
	public void readInput(Scanner scan) {
		for(int i = 0; i < 200; i++) {
			String[] line = scan.nextLine().split(" ");
			for(int j = 0; j < 15; j++) {
				if(j < 10) {
					input[i][j] = line[j];
				} else if(j > 10) {
					output[i][j-11] = line[j];
				}
			}
		}
	}

	@Override
	public String task1() {
		int count = 0;
		for(int i = 0; i < 200; i++) {
			for(int j = 0; j < 4; j++) {
				int len = output[i][j].length();
				if(len == 2 || len == 4 || len == 3 || len == 7) count++;
			}
		}
		return "" + count;
	}

	@Override
	public String task2() {
		int sum = 0;
		
		for(int i = 0; i < 200; i++) {
			String[] in = input[i];
			HashMap<Integer, String> numToPat = new HashMap<>();
			
			// get easy ones
			for(String s : in) {
				if(s.length() == 2) {
					numToPat.put(1, s);
				} else if(s.length() == 3) {
					numToPat.put(7, s);
				} else if(s.length() == 4) {
					numToPat.put(4, s);
				} else if(s.length() == 7) {
					numToPat.put(8, s);
				}
			}
			
			// find 6
			String weird1 = setMinus(numToPat.get(8), numToPat.get(1));
			for(String s : in) {
				if(countCommon(s, weird1) == 5 && s.length() == 6) {
					numToPat.put(6, s);
				}
			}
			
			// find 5
			for(String s : in) {
				if(s.length() == 5 && countCommon(s, numToPat.get(6)) == 5) {
					numToPat.put(5, s);
				}
			}
			
			// find 2 and 3
			String weird2 = setMinus(numToPat.get(5), setMinus(numToPat.get(7), numToPat.get(1)));
			for(String s : in) {
				if(s.length() == 5) {
					int com = countCommon(weird2, s);
					if(com == 3) {
						numToPat.put(3, s);
					} else if(com == 2) {
						numToPat.put(2, s);
					}
				}
			}
			
			// find 0 and 9
			String weird3 = setMinus(numToPat.get(2), numToPat.get(3));
			for(String s : in) {
				if(s.length() == 6) {
					if(s.equals(numToPat.get(6))) continue;
					int com = countCommon(s, weird3);
					if(com == 1) {
						numToPat.put(0, s);
					} else if(com == 0) {
						numToPat.put(9, s);
					}
				}
			}
			
			// decode output
			String out = "";
			for(int j = 0; j < 4; j++) {
				for(int o : numToPat.keySet()) {
					if(setEquals(numToPat.get(o), output[i][j])) {
						out += "" + o;
						break;
					}
				}
			}
			
			sum += Integer.parseInt(out);
		}
		return "" + sum;
	}
	
	private int countCommon(String a, String b) {
		int count = 0;
		for(char c : a.toCharArray()) {
			if(b.contains("" + c)) count++;
		}
		return count;
	}
	
	private boolean setEquals(String a, String b) {
		boolean out = true;
		for(char c : a.toCharArray()) {
			if(!b.contains("" + c)) {
				out = false;
				break;
			}
		}
		if(a.length() != b.length()) out = false;
		return out;
	}
	
	private String setMinus(String a, String b) {
		String out = "";
		for(char c : a.toCharArray()) {
			if(b.contains("" + c)) continue;
			else {
				out += c;
			}
		}
		return out;
	}

}
