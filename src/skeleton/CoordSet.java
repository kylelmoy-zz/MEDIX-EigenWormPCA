package skeleton;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import stdlib.StdDraw;

public class CoordSet {
	int[] x;
	int[] y;
	double xMean;
	double yMean;
	double[] vec;
	int n = 0;
	public CoordSet (String skl) {
		for (char a : skl.toCharArray()) {
			if (a == '|')
				n++;
		}
		x = new int[n];
		y = new int[n];
		skl = skl.replaceAll("\\||;", " ");
		Scanner in = new Scanner(skl);
		for (int i = 0; i < n; i++) {
			x[i] = in.nextInt();
			y[i] = in.nextInt();
			//System.out.println(x[i] + ", " + y[i]);
		}
		in.close();
		calculateVectors();
		centroid();
		//System.out.println(skl);
	}
	public void centroid() {
		double xSum = 0;
		double ySum = 0;

		for (int i = 0; i < n; i++) {
			xSum += x[i];
			ySum += y[i];
		}
		xSum /= n;
		ySum /= n;
		xMean = xSum;
		yMean = ySum;
	}
	private void calculateVectors() {
		double[] v = new double[n - 1];
		
		//calc vectors
		double sum = 0;
		for (int i = 0; i < n - 1; i++) {
			int j = i + 1;
			int yDiff = (y[j] - y[i]);
			int xDiff = (x[j] - x[i]);
			double angle;
			if (xDiff == 0) angle = Math.PI/2;
			else angle = Math.atan2(yDiff,xDiff);
			v[i] = angle;
			sum += angle;
		}
		
		//Subtract mean
		double mean = sum / (n - 1);
		for (int i = 0; i < n - 1; i++) {
			v[i] -= mean;
		}
		vec = v;
	}
	public void draw() {
		for (int i = 0; i < n; i ++) {
			StdDraw.point(x[i], y[i]);
		}
	}
	public void draw(double xoff, double yoff) {
		for (int i = 0; i < n; i ++) {
			StdDraw.point(x[i] - xoff, y[i] - yoff);
		}
	}
	public void sample(int n) {
		int[] newX = new int[n];
		int[] newY = new int[n];
		double s = ((double)this.n / (double)n);
		double j = 0;
		//System.out.println(this.n + ", " + n);
		for (int i = 0; i < n; i++) {
			newX[i] = x[(int)j];
			newY[i] = y[(int)j];
			j += s;
		}
		
		//Assure original tail is in the downsampled set
		newX[n - 1] = x[this.n - 1];
		newY[n - 1] = y[this.n - 1];
		x = newX;
		y = newY;
		this.n = n;
		calculateVectors();
		centroid();
	}
}
