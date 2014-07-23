package skeleton;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.DoubleBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Scanner;


public class vecToEigenworm {
	final private static int e = 9;
	final private static int n = 60;
	private static double[] data;
	private static double[][] eigenworm = new double[e][n];
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		//Read Eigenworms
		System.out.println("Reading eigenworm data...");
		long time = System.nanoTime();
		Scanner in = new Scanner(new File(args[0]));
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < e; j++) {
				eigenworm[j][i] = in.nextDouble();
			}
		}
		in.close();
		long elapsed = System.nanoTime() - time;
		System.out.println("Read eigenworm data complete... " + (elapsed/1000000) + "ms");
		
		//Read Vectors
		System.out.println("Reading vector data...");
		time = System.nanoTime();
		RandomAccessFile raf = new RandomAccessFile(args[1], "r");
		FileChannel inChannel = raf.getChannel();
		MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
		DoubleBuffer dbuf = buffer.asDoubleBuffer();
		data = new double[dbuf.remaining()];
		dbuf.get(data);
		elapsed = System.nanoTime() - time;
		System.out.println("Read vector data complete... " + (elapsed/1000000) + "ms");
		raf.close();
		
		//Calculate Eigenworm scores
		System.out.println("Calculating projection data...");
		time = System.nanoTime();
		FileOutputStream fos = new FileOutputStream(args[2]);
		DataOutputStream dos = new DataOutputStream(fos);
		int n = 36010;
		int v = 60;
		for (int i = 0; i < n; i ++) {
			int offset = i * v;
			double[] score = new double[e];
			for (int j = 0; j < v; j ++) {
				for (int k = 0; k < e; k ++) {
					score[k] += eigenworm[k][j] * data[offset + j];
				}
			}
			for (int k = 0; k < e; k ++) {
				dos.writeDouble(score[k]);
			}
			if (i % 1000 == 0) System.out.println(i);
		}
		elapsed = System.nanoTime() - time;
		System.out.println("Calculation complete... " + (elapsed/1000000) + "ms");
		dos.flush();
		dos.close();
	}
}
