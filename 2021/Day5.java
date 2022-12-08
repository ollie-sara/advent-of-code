import java.util.ArrayList;
import java.util.Scanner;

public class Day5 implements AoCTask {
	
	ArrayList<Line> lines = new ArrayList<>();
	
	int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

	@Override
	public void readInput(Scanner scan) {
		while(scan.hasNextLine()) {
			String line = scan.nextLine();
			String[] points = line.split(" -> ");
			String[] point1 = points[0].split(",");
			String[] point2 = points[1].split(",");
			int x1 = Integer.parseInt(point1[0]);
			int y1 = Integer.parseInt(point1[1]);
			int x2 = Integer.parseInt(point2[0]);
			int y2 = Integer.parseInt(point2[1]);
			
			maxX = Math.max(maxX, Math.max(x1, x2));
			maxY = Math.max(maxY, Math.max(y1, y2));
			
			lines.add(new Line(x1, y1, x2, y2));
		}
	}

	@Override
	public String task1() {
		int[][] grid = new int[maxX+1][maxY+1];
		for(Line l : lines) {
			if(!l.isHorizontal) continue;
			if(l.x1 == l.x2) {
				int smY = Math.min(l.y1, l.y2);
				int lgY = Math.max(l.y1, l.y2);
				for(int i = smY; i <= lgY; i++) {
					grid[l.x1][i] += 1;
				}
			} else {
				int smX = Math.min(l.x1, l.x2);
				int lgX = Math.max(l.x1, l.x2);
				for(int i = smX; i <= lgX; i++) {
					grid[i][l.y1] += 1;
				}
			}
		}
		
		int count = 0;
		for(int i = 0; i < maxX+1; i++) {
			for(int j = 0; j < maxY+1; j++) {
				if(grid[i][j] > 1) count++;
			}
		}
		return "" + count;
	}

	@Override
	public String task2() {
		int[][] grid = new int[maxX+1][maxY+1];
		for(Line l : lines) {
			if(l.x1 == l.x2) {
				int smY = Math.min(l.y1, l.y2);
				int lgY = Math.max(l.y1, l.y2);
				for(int i = smY; i <= lgY; i++) {
					grid[l.x1][i] += 1;
				}
			} else if(l.y1 == l.y2){
				int smX = Math.min(l.x1, l.x2);
				int lgX = Math.max(l.x1, l.x2);
				for(int i = smX; i <= lgX; i++) {
					grid[i][l.y1] += 1;
				}
			} else {
				int x = l.x1;
				int y = l.y1;
				while(x != l.x2 && y != l.y2) {
					grid[x][y] += 1;
					if(x < l.x2) x++;
					else x--;
					if(y < l.y2) y++;
					else y--;
				}
				grid[x][y] += 1;
			}
		}
		
		int count = 0;
		for(int i = 0; i < maxX+1; i++) {
			for(int j = 0; j < maxY+1; j++) {
				if(grid[i][j] > 1) count++;
			}
		}
		return "" + count;
	}
	
	class Line {
		int x1, x2, y1, y2;
		boolean isHorizontal = false;
		
		Line(int x1, int y1, int x2, int y2) {
			this.x1 = x1;
			this.x2 = x2;
			this.y1 = y1;
			this.y2 = y2;
			
			if(x1 == x2 || y1 == y2) {
				isHorizontal = true;
			}
		}
	}

}
