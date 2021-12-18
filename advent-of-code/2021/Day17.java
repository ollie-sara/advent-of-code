import java.util.Scanner;

public class Day17 implements AoCTask {
	
	int xMin, xMax;
	int yMin, yMax;

	@Override
	public void readInput(Scanner scan) {
		xMin = 244;
		xMax = 303;
		yMin = -91;
		yMax = -54;
	}

	@Override
	public String task1() {
		int highestY = Integer.MIN_VALUE;
		
		for(int x = 0; x < 400; x++) {
			for(int y = 0; y < 400; y++) {
				highestY = Math.max(shootProbe(x,y), highestY);
			}
		}
		
		return "" + highestY;
	}
	
	private int shootProbe(int xi, int yi) {
		int vx = xi;
		int vy = yi;
		int x = 0;
		int y = 0;
		
		int highestY = 0;
		boolean hit = false;
		
		while(x <= xMax && y >= yMin) {			
			//check if it will overflow
			long lx = x, ly = y;
			if(lx + vx > Integer.MAX_VALUE || lx + vx < Integer.MIN_VALUE) break;
			if(ly + vy > Integer.MAX_VALUE || ly + vy < Integer.MIN_VALUE) break;
			
			x += vx;
			y += vy;
			
			highestY = Math.max(y, highestY);
			if(x >= xMin && x <= xMax && y >= yMin && y <= yMax) {
				hit = true;
				break;
			}
			
			vx += vx == 0 ? 0 : vx > 0 ? -1 : 1;
			vy += -1;
		}
		
		return hit ? highestY : -1;
	}

	@Override
	public String task2() {
		int count = 0;
		
		for(int x = 0; x < 400; x++) {
			for(int y = -100; y < 1000; y++) {
				if(shootProbe(x,y) != -1) count++;
			}
		}
		
		return "" + count;
	}

}
