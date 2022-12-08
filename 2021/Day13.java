import java.util.ArrayList;
import java.util.Scanner;

public class Day13 implements AoCTask {
	
	ArrayList<Point> points = new ArrayList<>();
	ArrayList<Fold> folds = new ArrayList<>();
	int maxX = 0;
	int maxY = 0;

	@Override
	public void readInput(Scanner scan) {
		for(int i = 0; i < 827; i++) {
			String[] point = scan.nextLine().split(",");
			int x = Integer.parseInt(point[0]);
			int y = Integer.parseInt(point[1]);
			maxX = Math.max(x, maxX);
			maxY = Math.max(y, maxY);
			points.add(new Point(x, y));
		}
		scan.nextLine();
		while(scan.hasNextLine()) {
			String[] fold = scan.nextLine().split("=");
			if(fold[0].equals("fold along x")) {
				folds.add(new Fold(false, Integer.parseInt(fold[1])));
			} else {
				folds.add(new Fold(true, Integer.parseInt(fold[1])));
			}
		}
	}

	@Override
	public String task1() {
		boolean[][] dots = new boolean[maxX+1][maxY+1];
		for(Point p : points) {
			dots[p.x][p.y] = true; 
		}
		Fold f = folds.get(0);
		if(f.isHorizontal) {
			boolean[][] newDots = new boolean[dots.length][Math.max(f.val, dots[0].length-f.val)];
			// transfer dots before the fold
			for(int x = 0; x < dots.length; x++) {
				for(int y = 0; y < f.val; y++) {
					newDots[x][Math.max(0,  dots[0].length-(2*f.val)) + y] = dots[x][y];
				}
			}
			
			// transfer dots after the fold
			for(int x = 0; x < dots.length; x++) {
				for(int y = f.val+1; y < dots[0].length; y++) {
					if(dots[x][y]) {
						newDots[x][newDots[0].length - y + f.val] = dots[x][y];
					}
				}
			}
			dots = newDots;
		} else {
			boolean[][] newDots = new boolean[Math.max(f.val, dots.length-f.val)][dots[0].length];
			// transfer dots before the fold
			for(int x = 0; x < f.val; x++) {
				for(int y = 0; y < dots[0].length; y++) {
					newDots[Math.max(0,  dots.length-(2*f.val)) + x][y] = dots[x][y];
				}
			}
			
			// transfer dots after the fold
			for(int x = f.val+1; x < dots.length; x++) {
				for(int y = 0; y < dots[0].length; y++) {
					if(dots[x][y]) {
						newDots[newDots.length - x + f.val][y] = dots[x][y];
					}
				}
			}
			dots = newDots;
		}
		
		int count = 0;
		for(int i = 0; i < dots.length; i++) {
			for(int j = 0; j < dots[i].length; j++) {
				if(dots[i][j]) count++;
			}
		}
		return "" + count;
	}

	@Override
	public String task2() {
		boolean[][] dots = new boolean[maxX+1][maxY+1];
		for(Point p : points) {
			dots[p.x][p.y] = true; 
		}
		for(Fold f : folds) {
			if(f.isHorizontal) {
				boolean[][] newDots = new boolean[dots.length][Math.max(f.val, dots[0].length-f.val-1)];
				// transfer dots before the fold
				for(int x = 0; x < dots.length; x++) {
					for(int y = 0; y < f.val; y++) {
						newDots[x][Math.max(0,  dots[0].length-(2*f.val)-1) + y] = dots[x][y];
					}
				}
				
				// transfer dots after the fold
				for(int x = 0; x < dots.length; x++) {
					for(int y = f.val+1; y < dots[0].length; y++) {
						if(dots[x][y]) {
							newDots[x][newDots[0].length - y + f.val] = dots[x][y];
						}
					}
				}
				dots = newDots;
			} else {
				boolean[][] newDots = new boolean[Math.max(f.val, dots.length-f.val-1)][dots[0].length];
				// transfer dots before the fold
				for(int x = 0; x < f.val; x++) {
					for(int y = 0; y < dots[0].length; y++) {
						newDots[Math.max(0,  dots.length-(2*f.val)-1) + x][y] = dots[x][y];
					}
				}
				
				// transfer dots after the fold
				for(int x = f.val+1; x < dots.length; x++) {
					for(int y = 0; y < dots[0].length; y++) {
						if(dots[x][y]) {
							newDots[newDots.length - x + f.val][y] = dots[x][y];
						}
					}
				}
				dots = newDots;
			}
		}
		
		for(int y = 0; y < dots[0].length; y++) {
			for(int x = 0; x < dots.length; x++) {
				System.out.print(dots[x][y] ? "#" : ".");
			}
			System.out.println();
		}
		return "output given above";
	}
	
	class Point {
		int x, y;
		
		Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	class Fold {
		boolean isHorizontal;
		int val;
		
		Fold(boolean isHorizontal, int val) {
			this.isHorizontal = isHorizontal;
			this.val = val;
		}
	}

}
