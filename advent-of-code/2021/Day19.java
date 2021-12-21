import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeSet;

public class Day19 implements AoCTask {
	
	ArrayList<TreeSet<Beacon>> scanners = new ArrayList<>();
	TreeSet<Beacon> scannerPos = new TreeSet<>();

	@Override
	public void readInput(Scanner scan) {
		while(scan.hasNextLine()) {
			scan.nextLine();
			TreeSet<Beacon> scanner = new TreeSet<>();
			String nextLine = scan.nextLine();
			do {
				String[] beacon = nextLine.split(",");
				scanner.add(new Beacon(Integer.parseInt(beacon[0]), Integer.parseInt(beacon[1]), Integer.parseInt(beacon[2])));
				nextLine = scan.nextLine();
			} while(!nextLine.equals(""));
			scanners.add(scanner);
		}
	}

	@Override
	public String task1() {
		TreeSet<BeaconDiff> all = new TreeSet<>();
		TreeSet<Beacon> points = new TreeSet<>();
		ArrayList<TreeSet<BeaconDiff>> diffs = new ArrayList<>();
		for(TreeSet<Beacon> scanner : scanners) {
			Object[] sset = scanner.toArray();
			TreeSet<BeaconDiff> diff = new TreeSet<>();
			for(int k = 0; k < sset.length; k++) {
				for(int p = 0; p < sset.length; p++) {
					Beacon d = ((Beacon) sset[k]).minus((Beacon) sset[p]);
					diff.add(new BeaconDiff(d.x, d.y, d.z));
				}
			}
			diffs.add(diff);
		}
		
		for(BeaconDiff b : diffs.get(0)) all.add(b);
		for(Beacon b : scanners.get(0)) points.add(b);
		
		HashSet<Integer> toAdd = new HashSet<>();
		for(int i = 1; i < scanners.size(); i++) toAdd.add(i);
		
		LOOP: while(!toAdd.isEmpty()) {
			HashSet<Integer> check = new HashSet<>(toAdd);
			for(int i : check) {
				for(int rx = 0; rx < 4; rx++) {
					for(int ry = 0; ry < 4; ry++) {
						for(int rz = 0; rz < 4; rz++) {
							TreeSet<BeaconDiff> toTest = new TreeSet<>();
							for(BeaconDiff diff : diffs.get(i)) {
								toTest.add(diff.rotate(rx, ry, rz));
							}
							toTest.retainAll(all);
							
							// FOUND CORRECT ROTATION
							if(toTest.size() > 100) {
								for(BeaconDiff diff : diffs.get(i)) {
									all.add(diff.rotate(rx, ry, rz));
								}
								
								// START FINDING TRANSPOSITION
								for(Beacon ip : scanners.get(i)) {
									TreeSet<Beacon> pToAdd = new TreeSet<>();
									for(Beacon pp : points) {
										Beacon di = (ip.rotate(rx, ry, rz)).minus(pp);
										TreeSet<Beacon> pointTest1 = new TreeSet<>();
										TreeSet<Beacon> pointTest2 = new TreeSet<>();
										for(Beacon b : scanners.get(i)) {
											pointTest1.add((b.rotate(rx, ry, rz)).plus(di));
											pointTest2.add((b.rotate(rx, ry, rz)).minus(di));
										}
										pointTest1.retainAll(points);
										pointTest2.retainAll(points);
										
										// FOUND RIGHT TRANSPOSITION
										if(pointTest1.size() >= 12) {
											for(Beacon b : scanners.get(i)) {
												pToAdd.add((b.rotate(rx, ry, rz)).plus(di));
												scannerPos.add(di);
											}
										} else if(pointTest2.size() >= 12) {
											for(Beacon b : scanners.get(i)) {
												pToAdd.add((b.rotate(rx, ry, rz)).minus(di));
												scannerPos.add(di.negate());
											}
										}
									}
									if(pToAdd.size() != 0) {
										points.addAll(pToAdd);
										break;
									}
								}
								
								toAdd.remove(i);
								continue LOOP;
							}
						}
					}
				}
			}
		}
		
		return "" + points.size();
	}

	@Override
	public String task2() {
		Object[] poss = scannerPos.toArray();
		int max = Integer.MIN_VALUE;
		for(int i = 0; i < poss.length; i++) {
			for(int k = i+1; k < poss.length; k++) {
				max = Math.max(max, ((Beacon) poss[i]).dist((Beacon) poss[k]));
			}
		}
		
		return "" + max;
	}
	
	class Beacon implements Comparable<Beacon> {
		int x, y, z;
		
		Beacon(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		void set(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		Beacon rotate(int rx, int ry, int rz) {
			Beacon out = new Beacon(x, y, z);
			for(int ix = 0; ix < rx%4; ix++) {
				out.set(out.x, out.z, -out.y);
			}
			for(int iy = 0; iy < ry%4; iy++) {
				out.set(-out.z, out.y, out.x);
			}
			for(int iz = 0; iz < rz%4; iz++) {
				out.set(-out.y, out.x, out.z);
			}
			return out;
		}
		
		Beacon transform(int tx, int ty, int tz) {
			return new Beacon(x+tx, y+ty, z+tz);
		}
		
		Beacon negate() {
			return new Beacon(-x, -y, -z);
		}
		
		Beacon minus(Beacon b) {
			return new Beacon(x-b.x, y-b.y, z-b.z);
		}
		
		Beacon plus(Beacon b) {
			return new Beacon(x+b.x, y+b.y, z+b.z);
		}
		
		boolean equalsTo(Beacon o) {
			if(x == o.x && y == o.y && z == o.z) return true;
			else if(x == -o.x && y == -o.y && z == -o.z) return true;
			else return false;
		}
		
		int dist(Beacon o) {
			Beacon diff = minus(o);
			return Math.abs(diff.x) + Math.abs(diff.y) + Math.abs(diff.z);
		}

		public int compareTo(Day19.Beacon o) {
			if(x < o.x) return -1;
			if(x > o.x) return 1;
			else {
				if(y < o.y) return -1;
				if(y > o.y) return 1;
				else {
					if(z < o.z) return -1;
					if(z > o.z) return 1;
					else return 0;
				}
			}
		}
		
		public String toString() {
			return "(" + x + "," + y + "," + z + ")";
		}
	}
	
	class BeaconDiff extends Beacon {
		BeaconDiff(int x, int y, int z) {
			super(x, y, z);
		}

		public int compareTo(Day19.Beacon o) {
			if(this.equalsTo(o)) return 0;
			if(x < o.x) return -1;
			if(x > o.x) return 1;
			else {
				if(y < o.y) return -1;
				if(y > o.y) return 1;
				else {
					if(z < o.z) return -1;
					if(z > o.z) return 1;
					else return 0;
				}
			}
		}
		
		BeaconDiff rotate(int rx, int ry, int rz) {
			Beacon b = super.rotate(rx, ry, rz);
			return new BeaconDiff(b.x, b.y, b.z);
		}
	}

}
