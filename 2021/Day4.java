import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

public class Day4 implements AoCTask {

	int[] rounds;
	LinkedList<Card> cards = new LinkedList<>();
	
	@Override
	public void readInput(Scanner scan) {
		String[] roundString = scan.nextLine().split(",");
		rounds = new int[roundString.length];
		for(int i = 0; i < roundString.length; i++) {
			rounds[i] = Integer.parseInt(roundString[i]);
		}
		
		scan.nextLine();
		
		while(scan.hasNext()) {
			int[][] card = new int[5][5];
			for(int i = 0; i < 25; i++) {
				card[i/5][i%5] = scan.nextInt();
			}
			cards.add(new Card(card));
		}
	}

	@Override
	public String task1() {
		for(int val : rounds) {
			for(Card c : cards) {
				int o = c.mark(val);
				if(o > -1) return "" + o;
			}
		}
		return null;
	}

	@Override
	public String task2() {
		HashSet<Card> hasNotWon = new HashSet<>();
		for(Card c : cards) hasNotWon.add(c);
		
		for(int val : rounds) {
			for(Card c : cards) {
				int o = c.mark(val);
				if(o > -1 && hasNotWon.size() == 1 && hasNotWon.contains(c)) {
					return "" + o;
				}
				if(o > -1) hasNotWon.remove(c);
			}
		}
		return null;
	}
	
	class Card {
		int[][] card;
		boolean[][] marked;
		
		Card(int[][] card) {
			this.card = card;
			this.marked = new boolean[5][5];
		}
		
		public int mark(int val) {
			int row = -1, col = -1;
			
			for(int i = 0; i < 5; i++) {
				for(int j = 0; j < 5; j++) {
					if(card[i][j] == val) {
						row = i;
						col = j;
						marked[i][j] = true;
					}
				}
			}
			
			if(row == -1 && col == -1) return -1;
			
			// is row/col marked
			boolean isRow = true, isCol = true;
			for(int i = 0; i < 5; i++) {
				if(!marked[row][i]) isRow = false;
				if(!marked[i][col]) isCol = false;
			}
			
			if(isRow || isCol) {
				int sum = 0;
				for(int i = 0; i < 5; i++) {
					for(int j = 0; j < 5; j++) {
						if(!marked[i][j]) {
							sum += card[i][j];
						}
					}
				}
				return sum * val;
			}
			return -1;
		}
	}

}
