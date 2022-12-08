import java.util.Scanner;

public class Day18 implements AoCTask {

	Pair[] pairs = new Pair[100];
	
	@Override
	public void readInput(Scanner scan) {
		int i = 0;
		while(scan.hasNextLine()) {
			Scanner line = new Scanner(scan.nextLine()).useDelimiter("");
			line.next();
			pairs[i++] = readPair(line, 0, null);
		}
	}

	@Override
	public String task1() {
		Pair current = pairs[0].copy(null);
		for(int i = 1; i < 100; i++) {
			current = current.add(pairs[i]);
		}
		return "" + current.getMagnitude();
	}

	@Override
	public String task2() {
		long maxMag = Long.MIN_VALUE;
		for(Pair p1 : pairs) {
			for(Pair p2 : pairs) {
				if(p1 == p2) continue;
				maxMag = Math.max(maxMag, (p1.add(p2)).getMagnitude());
			}
		}
		return "" + maxMag;
	}
	
	Pair readPair(Scanner scan, int depth, Pair parent) {
		Pair pair = new Pair(parent);
		pair.depth = depth;
		
		// Left
		String chr = scan.next();
		
		if(chr.equals("[")) {
			pair.regL = false;
			pair.left = readPair(scan, depth+1, pair);
		} else {
			pair.regL = true;
			pair.iLeft = Integer.parseInt(chr);
		}
		
		// Middle - Sanity Check
		if(!scan.next().equals(",")) throw new IllegalStateException("Expected \",\"");
		
		// Right
		chr = scan.next();
		
		if(chr.equals("[")) {
			pair.regR = false;
			pair.right = readPair(scan, depth+1, pair);
		} else {
			pair.regR = true;
			pair.iRight = Integer.parseInt(chr);
		}
		
		// Close Pair
		if(!scan.next().equals("]")) throw new IllegalStateException("Expected \"]\"");
		
		return pair;
	}
	
	class Pair {
		Pair left, right;
		Pair parent;
		int iLeft, iRight;
		boolean regL, regR;
		int depth;
		
		public Pair() {
			regL = regR = false;
			iLeft = iRight = -1;
			left = right = null;
			parent = null;
			depth = 0;
		}
		
		public Pair(Pair parent) {
			regL = regR = false;
			iLeft = iRight = -1;
			left = right = null;
			this.parent = parent;
			depth = 0;
		}
		
		public String toString() {
			String out = "[";
			if(regL) out += iLeft;
			else out += left.toString();
			out += ",";
			if(regR) out += iRight;
			else out += right.toString();
			out += "]";
			return out;
		}
		
		public Pair add(Pair p) {
			Pair out = new Pair();
			out.regL = out.regR = false;
			out.left = this.copy(out);
			out.right = p.copy(out);
			out.left.incDepth();
			out.right.incDepth();
//			System.out.println("unreduced: " + out.toString());
			out.reduce();
			return out;
		}
		
		public void incDepth() {
			depth++;
			if(!regL) left.incDepth();
			if(!regR) right.incDepth();
		}
		
		public void reduce() {
			while(true) {
				if(explode()) {
//					System.out.println("explode: " + toString());
					continue;
				}
				if(split()) {
//					System.out.println("split: " + toString());
					continue;
				}
				break;
			}
		}
		
		public boolean split() {
			if(!regL && left.split()) return true;
			
			if(regL && iLeft > 9) {
				regL = false;
				Pair p = new Pair(this);
				p.depth = depth+1;
				p.regL = p.regR = true;
				p.iLeft = iLeft/2;
				p.iRight = iLeft - p.iLeft;
				iLeft = -1;
				left = p;
				return true;
			}
			
			if(!regR && right.split()) return true;
			
			if(regR && iRight > 9) {
				regR = false;
				Pair p = new Pair(this);
				p.depth = depth+1;
				p.regL = p.regR = true;
				p.iLeft = iRight/2;
				p.iRight = iRight - p.iLeft;
				iRight = -1;
				right = p;
				return true;
			}
			
			return false;
		}
		
		public boolean explode() {
			if(depth == 4) {
				Pair current = this;
				while(current.parent != null && current.parent.left == current) current = current.parent;
				if(current.parent != null) {
					current = current.parent;
					if(!current.regL) {
						current = current.left;
						while(!current.regR) current = current.right;
						current.iRight += iLeft;
					} else {
						current.iLeft += iLeft;
					}
				}
				
				current = this;
				while(current.parent != null && current.parent.right == current) current = current.parent;
				if(current.parent != null) {
					current = current.parent;
					if(!current.regR) {
						current = current.right;
						while(!current.regL) current = current.left;
						current.iLeft += iRight;
					} else {
						current.iRight += iRight;
					}
					
				}
				
				if(parent.right == this) {
					parent.regR = true;
					parent.iRight = 0;
					parent.right = null;
				} else {
					parent.regL = true;
					parent.iLeft = 0;
					parent.left = null;
				}
				
				return true;
			}
			
			boolean l = false;
			boolean r = false;
			if(!regL) l = left.explode();
			if(!regR) r = right.explode();
			return l || r;
		}
		
		public long getMagnitude() {
			long l, r;
			if(regL) l = iLeft;
			else l = left.getMagnitude();
			if(regR) r = iRight;
			else r = right.getMagnitude();
			return 3*l + 2*r;
		}
		
		public Pair copy(Pair parent) {
			Pair p = new Pair(parent);
			p.depth = depth;
			
			if(regL) {
				p.regL = true;
				p.iLeft = iLeft;
			} else {
				p.regL = false;
				p.left = left.copy(p);
			}
			
			if(regR) {
				p.regR = true;
				p.iRight = iRight;
			} else {
				p.regR = false;
				p.right = right.copy(p);
			}
			
			return p;
		}
	}

}
