package skeleton;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import stdlib.StdDraw;

/**
 * Produces a text file of angles from a raw integer file of skeleton points.
 * Produces a UTF-8 encoded text file containing a list of space delimited angles (in radians, double, truncated to 14 decimal points) from a byte file of skeleton points.
 * The skeleton is first down sampled to n points, then the (n - 1) angles are written to the file. One case per line, angles space delimited.
 * The mean of the angles are subtracted from each set.
 * 
 * By pre-processing the skeleton points into a byte file (see skelProcess.java), moving the data into memory is near instantaneous (51ms for 36000 skeletons).
 * This also increased the speed over the previous processing method (see VectorizeOld.java, 24348ms for 36000 skeletons) to 6622ms.
 * 
 * The output of this can easily be imported into SPSS
 * @author Kyle moy
 *
 */
public class skelVectorize {
	final private static int n = 61;
	private static boolean raw = false;
	private static int[] data;
	static PrintWriter writer;
	static DataOutputStream dos;
	public static void main(String[] args) throws Exception {
		if (args[2].equals("1")) raw = true;
		data = loadRawInteger(args[0]);
		long time = System.nanoTime();
		System.out.println("Processing...");
		if (raw) {
			FileOutputStream fos = new FileOutputStream(args[1]);
			dos = new DataOutputStream(fos);
		} else {
			writer = new PrintWriter(args[1], "UTF-8");
		}
		int c = 0;
		int progress = 0;
		while (c < data.length) {
			int len = data[c++];
			double[] vec = calculateVectors(c,len,n);
			if (raw) {
				for (double v : vec) {
					 dos.writeDouble(v);
				}
			} else {
				for (double v : vec) {
					 writer.print(String.format("%.14g", v) + " ");
				}
			}
			if (!raw) writer.println();
			c += len * 2;
			if (progress++ % 1000 == 0) System.out.println(progress + ": " + c + " / " + data.length);
		}
		System.out.println((progress - 1) + ": " + c + " / " + data.length);
		long elapsed = System.nanoTime() - time;
		System.out.println("Processing complete... " + (elapsed/1000000) + "ms");
		if (raw) {
			dos.flush();
			dos.close();
		} else {
			writer.flush();
			writer.close();
		}
	}
	/**
	 * Produces a list of angles in radians for a set of data
	 * @param offset The beginning of the skeleton in data
	 * @param n The number of skeleton points
	 * @param sampleLength The number of points to sample
	 * @return A double array of (sampleLength - 1) angles between the skeleton points
	 */
	private static double[] calculateVectors(int offset, int len, int d) {
		//Create isolated copy... Could be optimized (ie. removed)
		double[] x = new double[len];
		double[] y = new double[len];
		for (int i = 0; i < len; i++) {
			int s = i * 2;
			x[i] = data[offset + s];
			y[i] = data[offset + s + 1];
		}
		
		//Down Sample to d
		double s = (double)len/(double)(d-1);
		double[] dX = new double[d];
		double[] dY = new double[d];
		double c = 0;
		for (int i = 0; i < (d-1); i++) {
			dX[i] = x[(int)c];
			dY[i] = y[(int)c];
			c += s;
		}
		//Always include tail
		dX[d - 1] = x[len-1];
		dY[d - 1] = y[len-1];
		
		//Calculate Vectors
		int n = d - 1;
		double[] vec = new double[n];
		double sum = 0;
		for (int i = 0; i < n; i++) {
			double yDiff = (dY[i] - dY[i + 1]);
			double xDiff = (dX[i] - dX[i + 1]);
			double angle;
			if (xDiff == 0) {
				if (yDiff > 0) {
					angle = Math.PI/2;
				} else {
					angle = (Math.PI/2) * 3;
				}
			} else angle = Math.atan2(yDiff,xDiff);
			vec[i] = angle;
			sum += angle;
		}
		sum /= n;
		for (int i = 0; i < n; i++) {
			vec[i] -= sum;
		}
		return vec;
	}
	private static int[] loadRawInteger(String source) throws Exception {
		System.out.println("Loading data...");
		long time = System.nanoTime();
		RandomAccessFile raf = new RandomAccessFile(source, "r");
		FileChannel inChannel = raf.getChannel();
		MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
		IntBuffer dbuf = buffer.asIntBuffer();
		int[] dest = new int[dbuf.remaining()];
		dbuf.get(dest);
		long elapsed = System.nanoTime() - time;
		System.out.println("Load complete... " + (elapsed/1000000) + "ms");
		raf.close();
		return dest;
	}
}
