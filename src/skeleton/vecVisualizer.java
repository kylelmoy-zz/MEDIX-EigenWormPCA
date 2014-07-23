package skeleton;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.DoubleBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Scanner;

import stdlib.StdDraw;

public class vecVisualizer {
	static long time, elapsed;
	static double[] vectors;
	static double[] eigenvalue;
	static int n = 60;
	static int e = 9;
	static double[][] eigenworm = new double[e][n];
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		//Read Vectors
		vectors = loadRaw(args[0]);
		eigenvalue = loadRaw(args[1]);

		System.out.println("Reading eigenworm data...");
		long time = System.nanoTime();
		Scanner in = new Scanner(new File("data/components.txt"));
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < e; j++) {
				eigenworm[j][i] = in.nextDouble();
			}
		}
		in.close();
		long elapsed = System.nanoTime() - time;
		System.out.println("Read eigenworm data complete... " + (elapsed/1000000) + "ms");
		
		
		StdDraw.setCanvasSize(800,800);
		StdDraw.setXscale(0,n);
		StdDraw.setYscale(-6,6);
		StdDraw.show(100);
		StdDraw.setPenRadius(0.005);
		int f = 0;
		while (true) {
			if (StdDraw.isKeyPressed(KeyEvent.VK_Q)) {
				break;
			}
			if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
				if (StdDraw.isKeyPressed(KeyEvent.VK_SHIFT)) {
					f += 10;
					draw(f);
				}
				else draw(f++);
			}
			if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
				if (StdDraw.isKeyPressed(KeyEvent.VK_SHIFT)) {
					f -= 10;
					draw(f);
				}
				else draw(f--);
			}
			StdDraw.show(100);
		}
	}
	private static void draw(int frame) {
		StdDraw.show(10);
		StdDraw.clear();
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.001);
		StdDraw.text(-2, 0, "" + frame);
		StdDraw.line(0, 0, n, 0);
		StdDraw.line(0, -7, 0, 7);
		int offset = frame * n;
		StdDraw.setPenColor(StdDraw.BLUE);
		StdDraw.setPenRadius(0.001);
		
		for (int i= 1; i < n; i ++) {
			StdDraw.line(i - 1, vectors[offset + i - 1], i, vectors[offset + i]);
		}
		StdDraw.setPenRadius(0.005);
		StdDraw.setPenColor(StdDraw.RED);
		for (int i= 0; i < n; i ++) {
			StdDraw.point(i, vectors[offset + i]);
		}
		/* This doesn't actually mean anything
		Color[] col = {StdDraw.RED,StdDraw.GREEN,StdDraw.BLUE,StdDraw.MAGENTA};
		for (int j = 0; j < 4; j++) {
			double[] projected = new double[n];
			offset = frame * e;
			for (int i = 0 ; i < n; i ++) {
				projected[i] += eigenvalue[offset + j] * eigenworm[j][i];
			}
	
			StdDraw.setPenRadius(0.003);
			StdDraw.setPenColor(col[j]);
			for (int i= 1; i < n; i ++) {
				StdDraw.line(i - 1, projected[i - 1], i, projected[i]);
			}
			StdDraw.setPenRadius(0.005);
			StdDraw.setPenColor(StdDraw.BLACK);
			for (int i= 0; i < n; i ++) {
				
			StdDraw.point(i, projected[i]);
			}
		}
		*/
	}
	private static double[] loadRaw(String source) throws Exception {
		System.out.println("Loading data...");
		time = System.nanoTime();
		RandomAccessFile raf = new RandomAccessFile(source, "r");
		FileChannel inChannel = raf.getChannel();
		MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
		DoubleBuffer dbuf = buffer.asDoubleBuffer();
		double[] dest = new double[dbuf.remaining()];
		dbuf.get(dest);
		elapsed = System.nanoTime() - time;
		System.out.println("Load complete... " + (elapsed/1000000) + "ms");
		raf.close();
		return dest;
	}
}
