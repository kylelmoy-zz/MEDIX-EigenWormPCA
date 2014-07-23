package skeleton;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;


/**
 * Converts text skeleton points to raw data file.
 * Takes a UTF-8 encoded text file of the skeleton points (output from Ron Neihaus's feature extraction) and writes it as byte data.
 * This allows near instant read speeds when processing (the entire file can be copied into memory and referenced as an array).
 * 
 * File size can be reduced by writing values as short rather than int, since the max value is 1024 (video resolution), however this
 * will negatively impact read speeds as the shorts will need to be converted to ints (in linear time) to be copied to memory.
 * Input:
 * 		|x1;y1|x2;y2|x3;y3...|xn;yn
 * 
 * Output:
 * 		The output is formatted as follows: For each skeleton, the number (int) of coordinate pairs, followed by x1, then y1, ... xn then yn.
 * 
 * Arg 1: input file
 * Arg 2: output file
 * @author Kyle Moy
 *
 */
public class skelProcess {
	public static void main(String[] args) throws Exception {
		if (args.length < 2) throw new Error("Insufficient arguments");
		Scanner in = new Scanner (new File(args[0]));
		FileOutputStream fos = new FileOutputStream(args[1]);
		DataOutputStream dos = new DataOutputStream(fos);
		System.out.println("Converting...");
		long time = System.nanoTime();
		int c = 0;
		while (in.hasNext()) {
			String skl = in.nextLine();
			int n = 0;
			for (char a : skl.toCharArray()) {
				if (a == '|')
					n++;
			}
			//Ignore skeleton entries with too few points
			if (n < 100) continue;
			skl = skl.replaceAll("\\||;", " ");
			Scanner line = new Scanner(skl);
			dos.writeInt(n);
			for (int i = 0; i < n; i++) {
				dos.writeInt(line.nextInt());
				dos.writeInt(line.nextInt());
			}
			line.close();
			if (c++ % 100 == 0) System.out.println(c);
		}
		System.out.println(c);
		long elapsed = System.nanoTime() - time;
		System.out.println("Conversion complete... " + (elapsed/1000000) + "ms");
		dos.flush();
		dos.close();
		fos.close();
		in.close();
	}
}
