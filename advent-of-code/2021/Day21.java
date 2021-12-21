import java.util.Scanner;

public class Day21 implements AoCTask {

	int spos1 = 0;
	int spos2 = 0;
	
	@Override
	public void readInput(Scanner scan) {
		spos1 = Integer.parseInt(scan.nextLine().split(": ")[1]);
		spos2 = Integer.parseInt(scan.nextLine().split(": ")[1]);
	}

	@Override
	public String task1() {
		int p1 = spos1-1;
		int p2 = spos2-1;
		int score1, score2, roll;
		int dice = 0;
		score1 = score2 = roll = 0;
		
		while(score1 < 1000 && score2 < 1000) {
			if(roll++%2 == 0) {
				for(int i = 0; i < 3; i++) {
					p1 += dice++ + 1;
					dice %= 100;
				}
				p1 %= 10;
				score1 += p1+1;
			} else {
				for(int i = 0; i < 3; i++) {
					p2 += dice++ + 1;
					dice %= 100;
				}
				p2 %= 10;
				score2 += p2+1;
			}
		}
		
		return "" + (roll%2 == 0 ? (score1 * roll * 3) : (score2 * roll * 3));
	}

	@Override
	public String task2() {
		
		long[][][][][] game = new long[301][31][31][10][10];
		game[0][0][0][spos1-1][spos2-1] = 1;
		
		for(int r = 0; r < 300; r++) {
			for(int s1 = 0; s1 < 31; s1++) {
				for(int s2 = 0; s2 < 31; s2++) {
					if(s1 >= 21 || s2 >= 21) continue;
					for(int p1 = 0; p1 < 10; p1++) {
						for(int p2 = 0; p2 < 10; p2++) {
							if(r%3 == 2) {
								if(((r-(r%3))/3)%2 == 0) {
									game[r+1][s1+((p1+1)%10)+1][s2][(p1+1)%10][p2] += game[r][s1][s2][p1][p2];
									game[r+1][s1+((p1+2)%10)+1][s2][(p1+2)%10][p2] += game[r][s1][s2][p1][p2];
									game[r+1][s1+((p1+3)%10)+1][s2][(p1+3)%10][p2] += game[r][s1][s2][p1][p2];
								} else {
									game[r+1][s1][s2+((p2+1)%10)+1][p1][(p2+1)%10] += game[r][s1][s2][p1][p2];
									game[r+1][s1][s2+((p2+2)%10)+1][p1][(p2+2)%10] += game[r][s1][s2][p1][p2];
									game[r+1][s1][s2+((p2+3)%10)+1][p1][(p2+3)%10] += game[r][s1][s2][p1][p2];
								}
							} else {
								if(((r-(r%3))/3)%2 == 0) {
									game[r+1][s1][s2][(p1+1)%10][p2] += game[r][s1][s2][p1][p2];
									game[r+1][s1][s2][(p1+2)%10][p2] += game[r][s1][s2][p1][p2];
									game[r+1][s1][s2][(p1+3)%10][p2] += game[r][s1][s2][p1][p2];
								} else {
									game[r+1][s1][s2][p1][(p2+1)%10] += game[r][s1][s2][p1][p2];
									game[r+1][s1][s2][p1][(p2+2)%10] += game[r][s1][s2][p1][p2];
									game[r+1][s1][s2][p1][(p2+3)%10] += game[r][s1][s2][p1][p2];
								}
							}
						}
					}
				}
			}
		}
		
		long count1, count2;
		count1 = count2 = 0L;
		
		for(int r = 0; r < 301; r++) {
			for(int s1 = 0; s1 < 31; s1++) {
				for(int s2 = 0; s2 < 31; s2++) {
					for(int p1 = 0; p1 < 10; p1++) {
						for(int p2 = 0; p2 < 10; p2++) {
							if(s1 >= 21) count1 += game[r][s1][s2][p1][p2];
							if(s2 >= 21) count2 += game[r][s1][s2][p1][p2];
						}
					}
				}
			}
		}
		
		return "" + Math.max(count1, count2);
	}

}
