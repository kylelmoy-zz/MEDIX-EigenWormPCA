package skeleton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class Vectorize {
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		Scanner in = new Scanner (new File("data/skeleton.txt"));
		//int smallest = Integer.MAX_VALUE;
		PrintWriter writer = new PrintWriter("data/downSampled40.txt", "UTF-8");
		int c = 0;
		int n = 71; //Max 71
		//int smallest = Integer.MAX_VALUE;
		while (in.hasNext()) {
			 String line = in.nextLine();
			 CoordSet set = new CoordSet(line);
			 //if (set.n < smallest) smallest = set.n;
			 set.sample(n);
			 for (int i = 0; i < set.n - 1; i++)
				 writer.print(set.vec[i] + " ");
			 //for (int i = 0; i < set.n; i++)
				 //writer.print("|" + set.x[i] + ";" + set.y[i]);
			 writer.println();
			 c++;
			 if (c % 256 == 0) System.out.println(c);
		}
		//System.out.println(smallest);
		writer.close();
		in.close();
	}
}
