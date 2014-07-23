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

public class skelVisualizer {
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
		
		
		StdDraw.setCanvasSize(512,512);
		StdDraw.setXscale(128,-128);
		StdDraw.setYscale(-128,128);
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
		StdDraw.setPenColor(StdDraw.BLACK);
		int offset = frame * n;
		double[] x = new double[n + 1];
		double[] y = new double[n + 1];
		double len = 3.0;
		for (int i = 0; i < n; i++) {
			x[i+1] = x[i] + (len * Math.sin(vectors[offset+i]));
			y[i+1] = y[i] + (len * Math.cos(vectors[offset+i]));
		}
		double _x = 0;
		double _y = 0;
		for (int i = 0; i < n + 1; i++) {
			_x += x[i];
			_y += y[i];
		}
		_x /= n + 1;
		_y /= n + 1;
		StdDraw.show(10);
		StdDraw.clear();
		StdDraw.text(0, 120, "" + frame);
		for (int i = 0; i < n + 1; i++) {
			x[i] -= _x;
			y[i] -= _y;
			StdDraw.point(x[i], y[i]);
		}
		

		StdDraw.setPenColor(StdDraw.RED);

		Color[] col = {StdDraw.RED,StdDraw.GREEN,StdDraw.BLUE,StdDraw.MAGENTA};
		//System.out.println("====FRAME " + frame + "====");
		for (int j = 0; j < 4; j++) {
			StdDraw.setPenColor(col[j]);
			double[] projected = new double[n];
			offset = frame * e;
			int dataoffset = frame * n;
			for (int i = 0 ; i < n; i ++) {
					projected[i] += vectors[dataoffset + i] * eigenworm[j][i];
					//System.out.println(vectors[dataoffset + i] + "\t" + eigenworm[j][i]);
			}
	
			x = new double[n + 1];
			y = new double[n + 1];
			for (int i = 0; i < n; i++) {
				x[i+1] = x[i] + (len * Math.sin(projected[i]));
				y[i+1] = y[i] + (len * Math.cos(projected[i]));
			}
			_x = 0;
			_y = 0;
			for (int i = 0; i < n + 1; i++) {
				_x += x[i];
				_y += y[i];
			}
			_x /= n + 1;
			_y /= n + 1;
			for (int i = 0; i < n + 1; i++) {
				x[i] -= _x;
				y[i] -= _y;
				StdDraw.point(x[i], y[i]);
			}
		}
		StdDraw.show();
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
