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

public class Vectorize {
	public static void main(String[] args) throws Exception {
		Scanner in = new Scanner (new File("data/skeleton.txt"));
		//int smallest = Integer.MAX_VALUE;
		//PrintWriter writer = new PrintWriter("I:/vectors.dat", "UTF-8");
		String out = "I:/vectors.dat";
		FileOutputStream fos = new FileOutputStream(out);
		DataOutputStream dos = new DataOutputStream(fos);
		int c = 0;
		int n = 71; //Max 71
		//int smallest = Integer.MAX_VALUE;
		ArrayList<CoordSet> setList = new ArrayList<CoordSet>();
		while (in.hasNext()) {
			 String line = in.nextLine();
			 CoordSet set = new CoordSet(line);
			 //if (set.n < smallest) smallest = set.n;
			 set.sample(n);
			 setList.add(set);
			 //for (int i = 0; i < set.n - 1; i++)
				 //dos.writeDouble(set.vec[i]);
				 //writer.print(set.vec[i] + " ");
			 //for (int i = 0; i < set.n; i++)
				 //writer.print("|" + set.x[i] + ";" + set.y[i]);
			 //writer.println();
			 c++;
			 if (c % 256 == 0) System.out.println(c);
		}
		dos.writeInt(n-1);
		dos.writeInt(c);
		for (CoordSet set : setList) {
			for (int i = 0; i < set.n - 1; i++)
				 dos.writeDouble(set.vec[i]);
		}
		dos.close();
		fos.close();
		//System.out.println(smallest);
		//writer.close();
		in.close();
	}
}
