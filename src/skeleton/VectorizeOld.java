package skeleton;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

public class VectorizeOld {
	public static void main(String[] args) throws Exception {

		System.out.println("Processing skeleton data...");
		long time = System.nanoTime();
		Scanner in = new Scanner (new File("data/skeleton.txt"));
		//int smallest = Integer.MAX_VALUE;
		PrintWriter writer = new PrintWriter("data/vectors10.txt", "UTF-8");
		//String out = "I:/vectors.dat";
		//FileOutputStream fos = new FileOutputStream(out);
		//DataOutputStream dos = new DataOutputStream(fos);
		int c = 0;
		int less = 0;
		int n = 101; //Max 71
		double sum = 0;
		ArrayList<CoordSet> setList = new ArrayList<CoordSet>();
		while (in.hasNext()) {
			 String line = in.nextLine();
			 CoordSet set = new CoordSet(line);
			 if (set.n < n) continue;
			 set.sample(n);
			 setList.add(set);
			 for (int i = 0; i < set.n - 1; i++) {
				 writer.print(String.format("%.14g", set.vec[i]) + " ");
			 }
				 //dos.writeDouble(set.vec[i]);
			 //for (int i = 0; i < set.n; i++)
				 //writer.print("|" + set.x[i] + ";" + set.y[i]);
			 //writer.println();
			 c++;
			 if (c % 256 == 0) System.out.println(c);
		}

		long elapsed = System.nanoTime() - time;
		System.out.println("Complete... " + (elapsed/1000000) + "ms");
		/*dos.writeInt(n-1);
		dos.writeInt(c);
		for (CoordSet set : setList) {
			for (int i = 0; i < set.n - 1; i++)
				 dos.writeDouble(set.vec[i]);
		}
		dos.close();
		fos.close();
		*/
		//System.out.println(smallest);
		writer.close();
		in.close();
	}
}
