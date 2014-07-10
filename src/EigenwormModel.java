import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

import stdlib.StdDraw;

public class EigenwormModel {
	static double[] segment = new double[40];
	static int[][] eigenworm = new int[4][40];
	static int selector = 0;
	static double[] amp = new double[4];
	public static void main(String[] args) throws Exception {
		Scanner fin;
		for (int j = 0; j < 4; j++) {
			System.out.println("Loading: " + "er" + j + ".txt");
			fin = new Scanner (new File("er" + j + ".txt"));
			for (int i = 0; i < 40; i++) {
				eigenworm[j][i] = fin.nextInt();
			}
		}
		StdDraw.setCanvasSize(800,400);
		StdDraw.setYscale(0, 100);
		StdDraw.setXscale(0, 120);
		for (int i = 0; i < 40; i++) {
			segment[i] = 50;
		}
		draw();
		while (true) {
			if (StdDraw.isKeyPressed(KeyEvent.VK_Q)) {
				break;
			}
			if (StdDraw.isKeyPressed(KeyEvent.VK_S)) {
				System.out.println("Enter file name to save as:");
				Scanner in = new Scanner(System.in);
				String fname = in.nextLine() + ".txt";
				in.close();
				File file = new File(fname);
				PrintWriter fout = new PrintWriter(new FileWriter(file));
				for (int i = 1; i < 40; i++) {
					fout.println(segment[i]);
				}
				fout.flush();
				fout.close();
				System.out.println("File written");
			}

			if (StdDraw.isKeyPressed(KeyEvent.VK_L)) {
				System.out.println("Enter file name to load from:");
				Scanner in = new Scanner(System.in);
				String fname = in.nextLine() + ".txt";
				in.close();
				File file = new File(fname);
				in = new Scanner(file);
				for (int i = 1; i < 40; i++) {
					segment[i] = in.nextInt();
				}
				in.close();
				System.out.println("File loaded");
				draw();
			}
			if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
				moveSelector(-1);
			}
			if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
				moveSelector(1);
			}
			if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) {
				moveValue(-1);
			}
			if (StdDraw.isKeyPressed(KeyEvent.VK_UP)) {
				moveValue(1);
			}
			if (StdDraw.isKeyPressed(KeyEvent.VK_R)) {
				resetValue();
			}
			StdDraw.show(100);
		}
	}
	public static void calc() {
		for (int i = 0; i < 40; i++) {
			segment[i] = 0;
		}

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 40; j++) {
				segment[j] += (eigenworm[i][j] - 50) * (amp[i]/100);
			}
		}
		for (int i = 0; i < 40; i++) {
			//segment[i] /= 2;
			segment[i] += 50;
		}

	}

	public static void resetValue() {
		amp[selector] = 0;
		draw();
	}
	public static void moveValue(int dir) {
		if (StdDraw.isKeyPressed(KeyEvent.VK_SHIFT)) {
			dir *= 5;
		}
		amp[selector] += dir;
		if (amp[selector] < -100) {
			amp[selector] = -100;
		}
		if (amp[selector] > 100) {
			amp[selector] = 100;
		}
		draw();
	}
	public static void moveSelector(int dir) {
		selector += dir;
		if (selector < 0) {
			selector = 0;
		}
		if (selector > 3) {
			selector = 3;
		}
		draw();
	}
	public static void draw() {
		calc();
		StdDraw.clear();
		StdDraw.setPenRadius(0.005);
		StdDraw.setPenColor(StdDraw.GREEN);
		StdDraw.line(0, 50, 78, 50);
		StdDraw.setPenColor(StdDraw.BLACK);

		StdDraw.setPenRadius(0.02);
		for (int i = 1; i < 40; i++) {
			//double dist1 = (1-((double)(Math.abs((i-1)-20))/20)) * 30.0;
			//double dist2 = (1-((double)(Math.abs(i-20))/20)) * 30.0;
			//double dist1 = 13 - Math.pow((i-19.5)-1, 2) / (30);
			//double dist2 = 13 - Math.pow((i-19.5), 2) / (30);
			//System.out.println(dist1);
			//StdDraw.line((i-1)*2,segment[i-1] - dist1, i*2,segment[i] - dist2);
			//StdDraw.line((i-1)*2,segment[i-1] + dist1, i*2,segment[i] + dist2);
			StdDraw.line((i-1)*2,segment[i-1], i*2,segment[i]);
		}
		StdDraw.setPenRadius(0.01);
		StdDraw.setPenColor(StdDraw.RED);
		for (int i = 0; i < 40; i++) {
			StdDraw.point(i*2,segment[i]);
		}
		StdDraw.setPenRadius(0.005);
		StdDraw.setPenColor(StdDraw.BLACK);

		StdDraw.line(82, 50, 100, 50);
		for (int i = 0; i < 4; i++) {
			StdDraw.line(82 + (i*6), 0, 82 + (i*6), 100);
		}
		StdDraw.setPenRadius(0.02);
		for (int i = 0; i < 4; i++) {
			if (selector == i) {
				StdDraw.setPenColor(StdDraw.BLUE);
			} else {
				StdDraw.setPenColor(StdDraw.RED);
			}
			StdDraw.point(82 + (i*6),((amp[i] / 100) * 50) + 50);
			StdDraw.text(85 + (i*6),(int)(((amp[i] / 100) * 50) + 50),""+(int)amp[i]);
		}
	}
}
