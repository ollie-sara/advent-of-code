import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TaskRunner {

	static AoCTask[] tasks = new AoCTask[] { new Day1(),
											 new Day2(),
											 new Day3()
											};
	
	public static void main(String[] args) throws FileNotFoundException {
		for(AoCTask task : tasks) {			
			Scanner input = null;
			try {
				input = new Scanner(new File("./2021/inputs/" + task.getClass().getName()));
				System.out.println("---- " + task.getClass().getName().replace("Day", "Day ") + ": ----");
			} catch(FileNotFoundException e) {
				continue;
			}
			
			task.readInput(input);
			System.out.println("Task1:\t" + task.task1());
			System.out.println("Task2:\t" + task.task2());
			System.out.println();
		}
	}

}
