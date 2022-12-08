import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Day9 implements AoCTask {
	
	int[][] grid = new int[102][102];
	LinkedList<Point> lowPoints;
	
	@Override
	public void readInput(Scanner scan) {
		for(int row = 0; row < 100; row++) {
			String[] gridLine = scan.nextLine().split("");
			for(int i = 0; i < 100; i++) {
				grid[row+1][i+1] = Integer.parseInt(gridLine[i]);
			}
		}
		for(int i = 0; i < 102; i++) {
			grid[0][i] = Integer.MAX_VALUE;
			grid[i][0] = Integer.MAX_VALUE;
			grid[101][i] = Integer.MAX_VALUE;
			grid[i][101] = Integer.MAX_VALUE;
		}
	}

	@Override
	public String task1() {
		lowPoints = new LinkedList<>();
		
		for(int x = 1; x <= 100; x++) {
			for(int y = 1; y <= 100; y++) {
				if(grid[x][y] < grid[x+1][y] && grid[x][y] < grid[x][y+1] && grid[x][y] < grid[x-1][y] && grid[x][y] < grid[x][y-1]) {
					lowPoints.add(new Point(x,y));
				}
			}
		}
			
		int sum = 0;
		for(Point p : lowPoints) sum += grid[p.x][p.y] + 1;
		return "" + sum;
	}
	
	class Point {
		int x, y;
		Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	@Override
	public String task2() {
		PriorityQueue<Integer> basins = new PriorityQueue<>(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				if(o1 == o2) return 0;
				return -(o1-o2)/Math.abs(o1-o2);
			}});
		
		for(Point p : lowPoints) {
			basins.add(basinfinder(p.x, p.y, new boolean[102][102]));
		}
		
		return "" + (basins.poll() * basins.poll() * basins.poll());
	}

	public int basinfinder(int x, int y, boolean[][] marked) {
		if(marked[x][y]) return 0;
		marked[x][y] = true;
		
		int count = 0;
		if(grid[x][y] < 9) count++;
		if(x > 0 && !marked[x-1][y] && grid[x-1][y] > grid[x][y]) count += basinfinder(x-1, y, marked); 
		if(y > 0 && !marked[x][y-1] && grid[x][y-1] > grid[x][y]) count += basinfinder(x, y-1, marked);
		if(x < 101 && !marked[x+1][y] && grid[x+1][y] > grid[x][y]) count += basinfinder(x+1, y, marked); 
		if(y < 101 && !marked[x][y+1] && grid[x][y+1] > grid[x][y]) count += basinfinder(x, y+1, marked);
		
		return count;
	}
}
