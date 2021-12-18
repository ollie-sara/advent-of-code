import java.util.LinkedList;
import java.util.Scanner;

public class Day16 implements AoCTask {
	
	String bin = "";
	Scanner reader;
	long pos;
	long rem;
	int versionSum = 0;

	@Override
	public void readInput(Scanner scan) {
		scan.useDelimiter("");
		while(scan.hasNext()) {
			char c = scan.next().toCharArray()[0];
			switch(c) {
			case '0': bin += "0000"; break;
			case '1': bin += "0001"; break;
			case '2': bin += "0010"; break;
			case '3': bin += "0011"; break;
			case '4': bin += "0100"; break;
			case '5': bin += "0101"; break;
			case '6': bin += "0110"; break;
			case '7': bin += "0111"; break;
			case '8': bin += "1000"; break;
			case '9': bin += "1001"; break;
			case 'A': bin += "1010"; break;
			case 'B': bin += "1011"; break;
			case 'C': bin += "1100"; break;
			case 'D': bin += "1101"; break;
			case 'E': bin += "1110"; break;
			case 'F': bin += "1111"; break;
			default: break;
			}
		}
		pos = 0;
		rem = bin.length();
		
	}

	@Override
	public String task1() {
		reader = new Scanner(bin).useDelimiter("");
		packet();
		return "" + versionSum;
	}

	@Override
	public String task2() {
		reader = new Scanner(bin).useDelimiter("");
		return "" + packet();
	}
	
	public long packet() {
		int version = Integer.parseInt(read(3), 2);
		int type = Integer.parseInt(read(3), 2);
		
		versionSum += version;
		
		switch(type) {
		case 4: return literal();
		default: return operator(type);
		}
	}
	
	public long operator(int type) {
		LinkedList<Long> literals = new LinkedList<>();
		
		if(read(1).equals("1")) {
			int numPackets = Integer.parseInt(read(11), 2);
			for(int p = 0; p < numPackets; p++) literals.add(packet());
		} else {
			int totalLength = Integer.parseInt(read(15), 2);
			long start = pos;
			while(pos < start + totalLength) literals.add(packet());
		}
		
		switch(type) {
		case 0:
			long sum = 0;
			for(long l : literals) sum += l;
			return sum;
		case 1:
			long prod = 1;
			for(long l : literals) prod *= l;
			return prod;
		case 2:
			long min = Long.MAX_VALUE;
			for(long l : literals) min = Math.min(min, l);
			return min;
		case 3:
			long max = Long.MIN_VALUE;
			for(long l : literals) max = Math.max(max, l);
			return max;
		case 5:
			return (literals.getFirst() > literals.getLast()) ? 1 : 0;
		case 6:
			return (literals.getFirst() < literals.getLast()) ? 1 : 0;
		case 7:
			return (literals.getFirst() == literals.getLast()) ? 1 : 0;
		default:
			return 0;
		}
	}
	
	public long literal() {
		String literal = "";
		while(read(1).equals("1")) {
			literal += read(4);
		}
		literal += read(4);
		return Long.parseLong(literal, 2);
	}
	
	public String read(int n) {
		String out = "";
		pos += n;
		rem -= n;
		while(n-- > 0) out += reader.next();
		return out;
	}

}
