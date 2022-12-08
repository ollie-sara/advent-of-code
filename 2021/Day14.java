import java.util.HashMap;
import java.util.Scanner;

public class Day14 implements AoCTask {
	
	String polymer;
	HashMap<String, String> rules = new HashMap<>();

	@Override
	public void readInput(Scanner scan) {
		polymer = scan.nextLine();
		scan.nextLine();
		while(scan.hasNextLine()) {
			String[] rule = scan.nextLine().split(" -> ");
			rules.put(rule[0], rule[1]);
		}
	}

	@Override
	public String task1() {
		InsertList newPoly = new InsertList();
		InsertListNode curr = newPoly.sentinelFirst;
		for(char c : polymer.toCharArray()) {
			curr.insertAfter("" + c);
			curr = curr.next;
		}
		for(int i = 0; i < 10; i++) {
			curr = newPoly.sentinelFirst.next;
			while(curr.getDouble() != null) {
				curr.insertAfter(rules.get(curr.getDouble()));
				curr = curr.next.next;
			}
		}
		
		return "" + newPoly.solveTask();
	}

	@Override
	public String task2() {
		HashMap<String, Integer> tupleToIndex = new HashMap<>();
		HashMap<Integer, String> indexToTuple = new HashMap<>();
		HashMap<String, Integer> elemToIndex = new HashMap<>();
		
		int iter = 0;
	
		for(String r : rules.keySet()) {
			if(tupleToIndex.containsKey(r)) continue;
			else {
				indexToTuple.put(iter, r);
				tupleToIndex.put(r, iter++);
			}
		}
		
		long[] tuples = new long[iter];
		
		iter = 0;
		for(String e : rules.values()) {
			if(elemToIndex.containsKey(e)) continue;
			else elemToIndex.put(e, iter++);
		}
		
		long[] elems = new long[iter];
		
		for(int i = 0; i < polymer.length(); i++) {
			if(i < polymer.length()-1) tuples[tupleToIndex.get(polymer.substring(i, i+2))]++;
			elems[elemToIndex.get(polymer.substring(i, i+1))]++;
		}
		
		for(int r = 0; r < 40; r++) {
			long[] newTuples = new long[tuples.length];
			for(int i = 0; i < tuples.length; i++) {
				String tuple = indexToTuple.get(i);
				newTuples[tupleToIndex.get(tuple.substring(0,1) + rules.get(tuple))] += tuples[i];
				newTuples[tupleToIndex.get(rules.get(tuple) + tuple.substring(1,2))] += tuples[i];
				elems[elemToIndex.get(rules.get(tuple))] += tuples[i];
			}
			tuples = newTuples;
		}
		
		long min = Long.MAX_VALUE;
		long max = Long.MIN_VALUE;
		for(long l : elems) {
			min = min > l ? l : min;
			max = max < l ? l : max;
		}
		
		return "" + (max - min);
	}
	
	class InsertList {
		InsertListNode sentinelFirst;
		InsertListNode sentinelLast;
		
		InsertList() {
			sentinelFirst = new InsertListNode(null);
			sentinelLast = new InsertListNode(null);
			sentinelFirst.next = sentinelLast;
			sentinelLast.prev = sentinelFirst;
		}
		
		public boolean isEmpty() {
			return sentinelFirst.next == sentinelLast;
		}
		
		public String toString() {
			InsertListNode current = sentinelFirst;
			String s = "";
			while(current.next != null) {
				if(current.str != null) s += current.str;
				current = current.next;
			}
			return s;
		}
		
		public long solveTask() {
			HashMap<Character, Long> tally = new HashMap<>();
			String list = toString();
			
			long maxN = Integer.MIN_VALUE;
			long minN = Integer.MAX_VALUE;
			
			for(char c : list.toCharArray()) {
				if(tally.keySet().contains(c)) {
					tally.put(c, tally.get(c) + 1);
				} else {
					tally.put(c, 1L);
				}
			}
			
			for(char c : tally.keySet()) {
				if(tally.get(c) > maxN) maxN = tally.get(c);
				if(tally.get(c) < minN) minN = tally.get(c);
			}
			
			return maxN - minN;
		}
	}
	
	class InsertListNode {
		String str;
		InsertListNode next;
		InsertListNode prev;
		
		InsertListNode(String str) {
			this.str = str;
		}
		
		public void insertAfter(String str) { 
			if(next == null) {
				next = new InsertListNode(str);
				next.prev = this;
			} else {
				InsertListNode temp = next;
				next = new InsertListNode(str);
				next.next = temp;
				temp.prev = next;
				next.prev = this;
			}
		}
		
		public String getDouble() {
			if(next == null) return null;
			if(next.str == null) return null;
			return str + next.str;
		}
	}

}
