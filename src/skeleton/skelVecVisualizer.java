package skeleton;

import java.awt.event.KeyEvent;
import java.io.RandomAccessFile;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import stdlib.StdDraw;

public class skelVecVisualizer {
	static int n = 60;
	static int[] skel;
	static double[] vec;
	static int[] pos;
	public static void main(String[] args) throws Exception{
		skel = loadRawInteger("data/skeleton.dat");
		vec = loadRawDouble("data/vectorsCorrected.dat");
		pos = new int[2000];
		int seek = 0;
		int frame = 0;
		while (frame < pos.length) {
			pos[frame++] = seek;
			seek += (skel[seek] * 2) + 1;
		}
		StdDraw.setCanvasSize(512,512);
		StdDraw.setXscale(-128,128);
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
		//Draw Worm
		int offset = pos[frame] + 1;
		int len = skel[offset - 1];
		double[] x = new double[len];
		double[] y = new double[len];
		double _x = 0;
		double _y = 0;
		for (int i = 0; i < len; i++) {
			int s = i * 2;
			x[i] = skel[offset + s];
			y[i] = skel[offset + s + 1];
			_x += x[i];
			_y += y[i];
		}
		_x /= len;
		_y /= len;
		StdDraw.clear();
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.text(0, 120, "" + frame);
		StdDraw.setPenRadius(0.003);
		StdDraw.setXscale(-128,128);
		StdDraw.setYscale(-128,128);
		for (int i = 0; i < len; i++) {
			x[i] -= _x;
			y[i] -= _y;
			StdDraw.point(x[i], y[i]);
		}
		StdDraw.setPenColor(StdDraw.GREEN);
		StdDraw.setPenRadius(0.01);
		StdDraw.point(x[0], y[0]);

		

		StdDraw.setXscale(-128,128);
		StdDraw.setYscale(128,-128);
		StdDraw.setPenColor(StdDraw.MAGENTA);
		StdDraw.setPenRadius(0.005);
		//Recreate from vectors
		offset = frame * n;
		x = new double[n + 1];
		y = new double[n + 1];
		double dist = 3.0;
		for (int i = 0; i < n; i++) {
			x[i+1] = x[i] + (dist * Math.sin(vec[offset + i]));
			y[i+1] = y[i] + (dist * Math.cos(vec[offset + i]));
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
	private static double[] loadRawDouble(String source) throws Exception {
		System.out.println("Loading data...");
		long time = System.nanoTime();
		RandomAccessFile raf = new RandomAccessFile(source, "r");
		FileChannel inChannel = raf.getChannel();
		MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
		DoubleBuffer dbuf = buffer.asDoubleBuffer();
		double[] dest = new double[dbuf.remaining()];
		dbuf.get(dest);
		long elapsed = System.nanoTime() - time;
		System.out.println("Load complete... " + (elapsed/1000000) + "ms");
		raf.close();
		return dest;
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
