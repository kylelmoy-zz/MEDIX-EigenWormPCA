import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class PCA {
	static ArrayList<ArrayList<Double>> components = new ArrayList<ArrayList<Double>>();
	static double[] means;
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("data/vectors.txt"));
		int col = 70;
		int count = 0;
		while (in.hasNext()) {
			ArrayList<Double> pointset = new ArrayList<Double>();
			for (int i = 0; i < col; i++)
				pointset.add(in.nextDouble());
			components.add(pointset);
			count ++;
			if (count % 100 == 0) System.out.println(count);
		}
		System.out.println(count);
		means = new double[count];
		for (int i = 0; i < count; i++) {
			means[i] = mean(components.get(i));
		}
	}
	public static double mean(int i) {
		return means[i];
	}
	public static double mean(ArrayList<Double> x) {
		double sum = 0;
		for(double d : x)
			sum += d;
		return sum / x.size();
	}
	public static double cov(ArrayList<Double> x, ArrayList<Double> y){
		double cov = 0;
		double xBar = mean(x);
		double yBar = mean(y);
		for (int i = 0; i < x.size(); i++) {
			cov += (x.get(i) - xBar) * (y.get(i) - yBar);
		}
		cov /= (x.size() - 1);
		return cov;
	}
}
