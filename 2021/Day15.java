import java.util.Arrays;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Comparator;

public class Day15 implements AoCTask {
	
	int[][] grid = new int[100][100];

	@Override
	public void readInput(Scanner scan) {
		for(int i = 0; i < 100; i++) {
			int j = 0;
			for(char c : scan.nextLine().toCharArray()) {
				grid[i][j++] = Integer.parseInt("" + c);
			}
		}
	}

	@Override
	public String task1() {
		int[][] bf = new int[102][102];
		for(int i = 0; i < 102; i++) {
			for(int j = 0; j < 102; j++) {
				bf[i][j] = Integer.MAX_VALUE;
			}
		}
		
		for(int v = 0; v < (100*100)-1; v++) {
			for(int i = 1; i < 101; i++) {
				for(int j = 1; j < 101; j++) {
					if(i == 1 && j == 1) bf[i][j] = 0;
					else bf[i][j] = Math.min(bf[i-1][j], Math.min(bf[i+1][j], Math.min(bf[i][j-1], bf[i][j+1]))) + grid[i-1][j-1];
				}
			}
		}
		
		return "" + bf[100][100];
	}

	@Override
	public String task2() {
		
		PriorityQueue<Point> q = new PriorityQueue<Point>();
		
		int[][] bf = new int[500][500];
		boolean[][] reached = new boolean[500][500];
		for(int i = 0; i < 500; i++) {
			for(int j = 0; j < 500; j++) {
				bf[i][j] = Integer.MAX_VALUE;
			}
		}
		
		q.add(new Point(0,0,0));
		
		while(!q.isEmpty()) {
			Point current = q.poll();
			int x = current.x;
			int y = current.y;
			int dist = current.dist;
			
			bf[x][y] = Math.min(bf[x][y], dist);
			if(reached[x][y]) continue;
			reached[x][y] = true;
			
			if(x > 0) {
				int val = (grid[(x-1)%100][y%100] + ((x-1)/100) + (y/100));
				int temp = dist + (val > 9 ? val-9 : val);
				if(bf[x-1][y] > temp) {
					bf[x-1][y] = temp;
					q.add(new Point(x-1, y, temp));
				}
			}
			
			if(y > 0) {
				int val = (grid[x%100][(y-1)%100] + (x/100) + ((y-1)/100));
				int temp = dist + (val > 9 ? val-9 : val);
				if(bf[x][y-1] > temp) {
					bf[x][y-1] = temp;
					q.add(new Point(x, y-1, temp));
				}
			}
			
			if(x < 499) {
				int val = grid[(x+1)%100][y%100] + ((x+1)/100) + (y/100);
				int temp = dist + (val > 9 ? val-9 : val);
				if(bf[x+1][y] > temp) {
					bf[x+1][y] = temp;
					q.add(new Point(x+1, y, temp));
				}
			}
			
			if(y < 499) {
				int val = (grid[x%100][(y+1)%100] + (x/100) + ((y+1)/100));
				int temp = dist + (val > 9 ? val-9 : val);
				if(bf[x][y+1] > temp) {
					bf[x][y+1] = temp;
					q.add(new Point(x, y+1, temp));
				}
			}
		}
		
		return "" + bf[499][499];
	}
	
	class Point implements Comparable<Point> {
		int x, y, dist;
		Point(int x, int y, int dist) {
			this.x = x;
			this.y = y;
			this.dist = dist;
		}
		
		public int compareTo(Point o) {
			if(this.dist > o.dist) return 1;
			else if (this.dist < o.dist) return -1;
			else return 0;
		}
	}

}
