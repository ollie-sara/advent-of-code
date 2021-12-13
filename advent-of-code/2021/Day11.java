import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class Day11 implements AoCTask {

	int[][] octo1 = new int[10][10];
	int[][] octo2 = new int[10][10];
	
	@Override
	public void readInput(Scanner scan) {
		for(int i = 0; i < 10; i++) {
			char[] line = scan.nextLine().toCharArray();
			for(int j = 0; j < 10; j++) {
				octo1[i][j] = Integer.parseInt("" + line[j]);
				octo2[i][j] = Integer.parseInt("" + line[j]);
			}
		}
	}

	@Override
	public String task1() {
		int count = 0;
		for(int r = 0; r < 100; r++) {
			LinkedList<Octo> queue = new LinkedList<>();
			boolean[][] marked = new boolean[10][10];
			
			// Increment each octo
			for(int i = 0; i < 10; i++) {
				for(int j = 0; j < 10; j++) {
					octo1[i][j]++;
					if(octo1[i][j] > 9) queue.add(new Octo(i,j));
				}
			}
			
			// Iterate through all flashing ones
			while(!queue.isEmpty()) {
				Octo current = queue.poll();
				int cx = current.x;
				int cy = current.y;
				
				if(marked[cx][cy]) continue;
				marked[cx][cy] = true;
				
				if(cx > 0) {
					if(cy > 0) {
						octo1[cx-1][cy-1]++;
						if(octo1[cx-1][cy-1] > 9) queue.add(new Octo(cx-1, cy-1));
					}
					if(cy < 9) {
						octo1[cx-1][cy+1]++;
						if(octo1[cx-1][cy+1] > 9) queue.add(new Octo(cx-1, cy+1));
					}
					octo1[cx-1][cy]++;
					if(octo1[cx-1][cy] > 9) queue.add(new Octo(cx-1, cy));
				}
				
				if(cx < 9) {
					if(cy > 0) {
						octo1[cx+1][cy-1]++;
						if(octo1[cx+1][cy-1] > 9) queue.add(new Octo(cx+1, cy-1));
					}
					if(cy < 9) {
						octo1[cx+1][cy+1]++;
						if(octo1[cx+1][cy+1] > 9) queue.add(new Octo(cx+1, cy+1));
					}
					octo1[cx+1][cy]++;
					if(octo1[cx+1][cy] > 9) queue.add(new Octo(cx+1, cy));
				}
				
				if(cy > 0) {
					octo1[cx][cy-1]++;
					if(octo1[cx][cy-1] > 9) queue.add(new Octo(cx, cy-1));
				}
				if(cy < 9) {
					octo1[cx][cy+1]++;
					if(octo1[cx][cy+1] > 9) queue.add(new Octo(cx, cy+1));
				}
			}
			
			// Set flashed ones to 0 and count
			for(int i = 0; i < 10; i++) {
				for(int j = 0; j < 10; j++) {
					if(octo1[i][j] > 9) {
						octo1[i][j] = 0;
						count++;
					}
				}
			}
		}
		return "" + count;
	}
	
	class Octo {
		int x, y;
		
		Octo(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	@Override
	public String task2() {
		int count = 0;
		int iter = 0;
		while(count != 100) {
			count = 0;
			LinkedList<Octo> queue = new LinkedList<>();
			boolean[][] marked = new boolean[10][10];
			
			// Increment each octo
			for(int i = 0; i < 10; i++) {
				for(int j = 0; j < 10; j++) {
					octo2[i][j]++;
					if(octo2[i][j] > 9) queue.add(new Octo(i,j));
				}
			}
			
			// Iterate through all flashing ones
			while(!queue.isEmpty()) {
				Octo current = queue.poll();
				int cx = current.x;
				int cy = current.y;
				
				if(marked[cx][cy]) continue;
				marked[cx][cy] = true;
				
				if(cx > 0) {
					if(cy > 0) {
						octo2[cx-1][cy-1]++;
						if(octo2[cx-1][cy-1] > 9) queue.add(new Octo(cx-1, cy-1));
					}
					if(cy < 9) {
						octo2[cx-1][cy+1]++;
						if(octo2[cx-1][cy+1] > 9) queue.add(new Octo(cx-1, cy+1));
					}
					octo2[cx-1][cy]++;
					if(octo2[cx-1][cy] > 9) queue.add(new Octo(cx-1, cy));
				}
				
				if(cx < 9) {
					if(cy > 0) {
						octo2[cx+1][cy-1]++;
						if(octo2[cx+1][cy-1] > 9) queue.add(new Octo(cx+1, cy-1));
					}
					if(cy < 9) {
						octo2[cx+1][cy+1]++;
						if(octo2[cx+1][cy+1] > 9) queue.add(new Octo(cx+1, cy+1));
					}
					octo2[cx+1][cy]++;
					if(octo2[cx+1][cy] > 9) queue.add(new Octo(cx+1, cy));
				}
				
				if(cy > 0) {
					octo2[cx][cy-1]++;
					if(octo2[cx][cy-1] > 9) queue.add(new Octo(cx, cy-1));
				}
				if(cy < 9) {
					octo2[cx][cy+1]++;
					if(octo2[cx][cy+1] > 9) queue.add(new Octo(cx, cy+1));
				}
			}
			
			// Set flashed ones to 0 and count
			for(int i = 0; i < 10; i++) {
				for(int j = 0; j < 10; j++) {
					if(octo2[i][j] > 9) {
						octo2[i][j] = 0;
						count++;
					}
				}
			}
			
			iter++;
		}
		
		return "" + iter;
	}

}
