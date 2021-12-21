import java.util.Scanner;

public class Day20 implements AoCTask {

	boolean[] algo = new boolean[512];
	boolean[][] img;
	int enhanced = 0;
	
	@Override
	public void readInput(Scanner scan) {
		int iter = 0;
		for(char c : scan.nextLine().toCharArray()) algo[iter++] = '#' == c ? true : false;
		scan.nextLine();
		img = new boolean[100][];
		int line = 0;
		while(scan.hasNextLine()) {
			iter = 0;
			char[] cline = scan.nextLine().toCharArray();
			img[line] = new boolean[cline.length];
			for(char c : cline) img[line][iter++] = '#' == c ? true : false;
			line++;
		}
	}

	@Override
	public String task1() {
		enhance();
		enhance();
		
		int count = 0;
		for(int i = 0; i < img.length; i++) {
			for(int j = 0; j < img[0].length; j++) {
				if(img[i][j]) count++;
			}
		}
		return "" + count;
	}
	
	public void enhance() {
		boolean[][] imgLrg = new boolean[img.length+4][img[0].length+4];
		boolean[][] output = new boolean[img.length+2][img[0].length+2];
		for(int i = 0; i < imgLrg.length; i++) {
			for(int j = 0; j < imgLrg[0].length; j++) {
				if(i < 2 || j < 2 || i > imgLrg.length-3 || j > imgLrg[0].length-3) imgLrg[i][j] = enhanced%2 == 1;
				else imgLrg[i][j] = img[i-2][j-2];
			}
		}
		
		for(int i = 0; i < output.length; i++) {
			for(int j = 0; j < output[0].length; j++) {
				output[i][j] = algo[getIndex(i+1, j+1, imgLrg)];
			}
		}
		
		enhanced++;
		img = output;
	}
	
	public int getIndex(int x, int y, boolean[][] input) {
		if(x == 0 || y == 0 || x == input.length-1 || y == input[0].length-1) throw new IllegalArgumentException("Has to be within bounds: x=" + x + ", y=" + y);
		String num = input[x-1][y-1] ? "1" : "0";
		num += input[x-1][y] ? "1" : "0";
		num += input[x-1][y+1] ? "1" : "0";
		num += input[x][y-1] ? "1" : "0";
		num += input[x][y] ? "1" : "0";
		num += input[x][y+1] ? "1" : "0";
		num += input[x+1][y-1] ? "1" : "0";
		num += input[x+1][y] ? "1" : "0";
		num += input[x+1][y+1] ? "1" : "0";
		return Integer.parseInt(num, 2);
	}

	@Override
	public String task2() {
		for(int i = 0; i < 48; i++) enhance();
		
		int count = 0;
		for(int i = 0; i < img.length; i++) {
			for(int j = 0; j < img[0].length; j++) {
				if(img[i][j]) count++;
			}
		}
		return "" + count;
	}

}
