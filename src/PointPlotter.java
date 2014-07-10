import java.util.ArrayList;
import java.util.Scanner;

import stdlib.StdDraw;


public class PointPlotter {
	public static void main (String[] args) {
		Scanner in = new Scanner(System.in);
		ArrayList<Double> pointset = new ArrayList<Double>();
		double max = Double.MIN_VALUE;
		System.out.println("Enter point set:");
		while (in.hasNext()) {
			double num = in.nextDouble();
			if (num == 7) {
				break;
			}
			if (num > max) max = num;
			pointset.add(num);
		}

		System.out.println("Size: " + pointset.size());
		System.out.println("Max: " + max);
		int n = pointset.size();
		//Draw
		StdDraw.setCanvasSize(800,600);
		StdDraw.setXscale(0,n);
		StdDraw.setYscale(-max,max);
		StdDraw.show(100);
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.line(0,0,n,0);
		StdDraw.line(0,-max,0,max);
		StdDraw.setPenRadius(0.005);
		StdDraw.setPenColor(StdDraw.BLUE);
		for (int i = 0; i < n - 1; i++) {
			double num1 = pointset.get(i);
			double num2 = pointset.get(i + 1);
			StdDraw.line(i, num1, i + 1, num2);
		}
		StdDraw.setPenRadius(0.01);
		StdDraw.setPenColor(StdDraw.RED);
		for (int i = 0; i < n; i++) {
			double num = pointset.get(i);
			StdDraw.point(i, num);
		}
		StdDraw.show();
	}
}
