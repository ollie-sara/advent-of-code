import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;

public class Day10 implements AoCTask {

	String[] lines = new String[106];
	
	@Override
	public void readInput(Scanner scan) {
		for(int i = 0; i < 106; i++) {
			lines[i] = scan.nextLine();
		}
	}

	@Override
	public String task1() {
		int sum = 0;
		for(int i = 0; i < 106; i++) {
			sum += checkChunks(i);
		}
		return "" + sum;
	}
	
	private int checkChunks(int i) {
		String line = lines[i];
		LinkedList<Character> expected = new LinkedList<>();
		for(char c : line.toCharArray()) {
			if(c == '(') {
				expected.push(')');
			} else if(c == '{') {
				expected.push('}');
			} else if(c == '<') {
				expected.push('>');
			} else if(c == '[') {
				expected.push(']');
			} else {
				char expC = expected.pop();
				if(expC != c) {
					switch(c) {
					case ')': return 3;
					case ']': return 57;
					case '}': return 1197;
					case '>': return 25137;
					}
				}
			}
		}
		
		return 0;
	}

	@Override
	public String task2() {
		ArrayList<Long> scores = new ArrayList<>();
		for(int i = 0; i < 106; i++) {
			long newScore = completeChunks(i);
			if(newScore != -1) scores.add(newScore);
		}
		
		scores.sort(Comparator.<Long>naturalOrder());
		
		return "" + scores.get(scores.size()/2);
	}
	
	private long completeChunks(int i) {
		String line = lines[i];
		LinkedList<Character> expected = new LinkedList<>();
		for(char c : line.toCharArray()) {
			if(c == '(') {
				expected.push(')');
			} else if(c == '{') {
				expected.push('}');
			} else if(c == '<') {
				expected.push('>');
			} else if(c == '[') {
				expected.push(']');
			} else if(expected.pop() != c) {
				return -1;
			}
		}
		
		long sum = 0;
		while(!expected.isEmpty()) {
			sum *= 5;
			switch(expected.pop()) {
			case ')': sum += 1; break;
			case ']': sum += 2; break;
			case '}': sum += 3; break;
			case '>': sum += 4; break;
			}
		}
		return sum;
	}

}
