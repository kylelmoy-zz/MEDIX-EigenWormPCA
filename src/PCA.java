import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.DoubleBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Scanner;


public class PCA {
	//static ArrayList<ArrayList<Double>> components = new ArrayList<ArrayList<Double>>();
	static ArrayList<Double[]> comp;
	static double[] means;
	public static void main(String[] args) throws Exception {
		long time = System.nanoTime();
		/*
		Scanner in = new Scanner(new File("I:/vectors.txt"));
		int col = 70;
		int count = 0;
		while (in.hasNext()) {
			ArrayList<Double> pointset = new ArrayList<Double>();
			for (int i = 0; i < col; i++)
				pointset.add(in.nextDouble());
			components.add(pointset);
			count ++;
			if (count % 500 == 0) System.out.println(count);
		}
		in.close();
		*/
		RandomAccessFile raf = new RandomAccessFile("I:/vectors.dat", "r");
		FileChannel inChannel = raf.getChannel();
		MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
		int col = buffer.getInt();
		int count = buffer.getInt();
		DoubleBuffer dbuf = buffer.asDoubleBuffer();
		final double[] pointset = new double[dbuf.remaining()];
		dbuf.get(pointset);
		long elapsed = System.nanoTime() - time;
		System.out.println("Columns: " + col);
		System.out.println("Rows: " + count);
		/*
		for (int j = 0; j < count; j++) {
			ArrayList<Double> pointset = new ArrayList<Double>();
			for (int i = 0; i < col; i++)
				pointset.add(buffer.getDouble());
			components.add(pointset);
		}*/
		System.out.println("Read time: " + (elapsed/1000000) + "ms");
		inChannel.close();
		raf.close();
		//Transpose
		System.out.println("Transposing...");
		//ArrayList<ArrayList<Double>> tcomp = new ArrayList<ArrayList<Double>>();
		comp = new ArrayList<Double[]>();
		//double[][] comp = new double[col][count];
		for (int i = 0; i < col; i++) {
			//ArrayList<Double> column = new ArrayList<Double>();
			Double[] list = new Double[count];
			for (int j = 0; j < count; j++) {
				list[j] = pointset[(j * col) + i];
				//comp[i][j] = pointset[(j * col) + i];
						//components.get(j).get(i);
				//column.add(components.get(j).get(i));
			}
			//tcomp.add(column);
			comp.add(list);
		}
		//components = tcomp;
		means = new double[col];
		for (int i = 0; i < col; i++) {
			means[i] = mean(comp.get(i));
		}
	}
	public static double mean(int i) {
		return means[i];
	}
	public static double mean(Double[] x) {
		double sum = 0;
		for(double d : x)
			sum += d;
		return sum / x.length;
	}
	public static double cov(int x, int y){
		double cov = 0;
		double xBar = mean(x);
		double yBar = mean(y);
		Double[] xList = comp.get(x);
		Double[] yList = comp.get(y);
		for (int i = 0; i < xList.length; i++) {
			cov += (xList[i] - xBar) * (yList[i] - yBar);
		}
		cov /= (xList.length - 1);
		return cov;
	}
}
