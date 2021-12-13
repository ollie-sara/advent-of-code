import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Day12 implements AoCTask {

	Graph map = new Graph();
	
	@Override
	public void readInput(Scanner scan) {
		while(scan.hasNextLine()) {
			String[] edge = scan.nextLine().split("-");
			map.addEdge(edge[0], edge[1]);
		}
	}

	@Override
	public String task1() {
		return "" + map.countPaths();
	}

	@Override
	public String task2() {
		return "" + map.countPaths2();
	}
	
	class Graph {
		
		HashMap<String, HashSet<String>> edges = new HashMap<String, HashSet<String>>();
		
		void addEdge(String from, String to) {
			addVertex(from);
			addVertex(to);
			edges.get(from).add(to);
			edges.get(to).add(from);
		}
		
		void addVertex(String vert) {
			if(edges.keySet().contains(vert)) return;
			edges.put(vert, new HashSet<>());
		}
		
		int countPaths() {
			HashSet<String> visited = new HashSet<>();
			visited.add("start");
			return countPaths("start", visited);
		}
		
		int countPaths(String lastVert, HashSet<String> visited) {
			int count = 0;
			if(lastVert.equals("end")) return 1;
			for(String s : edges.get(lastVert)) {
				if(!visited.contains(s)) {
					if(s.toLowerCase().equals(s)) visited.add(s);
					count += countPaths(s, visited); 
					if(s.toLowerCase().equals(s)) visited.remove(s);
				}
			}
			return count;
		}
		
		int countPaths2() {
			HashSet<String> visited = new HashSet<>();
			visited.add("start");
			return countPaths2("start", visited, null);
		}
		
		int countPaths2(String lastVert, HashSet<String> visited, String visitedTwice) {
			int count = 0;
			if(lastVert.equals("end")) return 1;
			for(String s : edges.get(lastVert)) {
				if(s.equals("start")) continue;
				if(!visited.contains(s) || visitedTwice == null) {
					if(visited.contains(s)) visitedTwice = s;
					else if(s.toLowerCase().equals(s)) visited.add(s);
					count += countPaths2(s, visited, visitedTwice);
					if(visitedTwice != null && visitedTwice.equals(s)) visitedTwice = null;
					else if(s.toLowerCase().equals(s)) visited.remove(s);
				}
			}
			return count;
		}
		
	}

}
