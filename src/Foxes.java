public class Foxes {
	public static void main(String[] args) {

		int black = 20;
		int foxes = 1;
		for(int year = 0; year < 20; year ++) {
			if (foxes == 0) foxes = 1;
			if (black < 10) black = 10;
			System.out.println("Year: " + (year + 1));
			System.out.println("Init Prey: " + black);
			System.out.println("Init Pred(foxes) " + foxes);
			int offspring = 0;
			int killed = 0;
			int survives = 0;
			int dies = 0;
			for (int j = 0; j < foxes; j++) {
				System.out.print("Fox " + (j + 1) +": ");
				int beansPicked = 0;
				for (int i = 0; i < 15; i++) {
					if (Math.random() < (black / 100.0)) {
						killed ++;
						beansPicked ++;
						black --;
					}
				}
				int off = (int)(beansPicked / 5);
				if (beansPicked < 5) dies ++;
				else survives ++;
				System.out.println(beansPicked + " kills\t" + off + " offspring");
				offspring += off;
			}
			System.out.println("Prey Survived: " + black);
			System.out.println("Prey Killed: " + killed);
			System.out.println("Pred Survived: " + survives);
			System.out.println("Offspring: " + offspring);
			black *= 2;
			foxes -= dies;
			foxes += offspring;
		}
	}
}