import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

import stdlib.StdDraw;

public class EigenwormCreator {
	static int[] segment = new int[40];
	static int selector = 0;
	static Scanner sin = new Scanner(System.in);
	public static void main(String[] args) throws Exception {
		StdDraw.setCanvasSize(600,400);
		StdDraw.setYscale(0, 100);
		StdDraw.setXscale(0, 80);
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
				String fname = sin.nextLine() + ".txt";
				File file = new File(fname);
				PrintWriter fout = new PrintWriter(new FileWriter(file));
				for (int i = 0; i < 40; i++) {
					fout.println(segment[i]);
				}
				fout.flush();
				fout.close();
				System.out.println("File written");
			}

			if (StdDraw.isKeyPressed(KeyEvent.VK_L)) {
				System.out.println("Enter file name to load from:");
				String fname = sin.nextLine() + ".txt";
				File file = new File(fname);
				Scanner in = new Scanner(file);
				for (int i = 0; i < 40; i++) {
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
			StdDraw.show(100);
		}
		sin.close();
	}

	public static void moveValue(int dir) {
		if (StdDraw.isKeyPressed(KeyEvent.VK_SHIFT)) {
			dir *= 5;
		}
		segment[selector] += dir;
		if (segment[selector] < 0) {
			segment[selector] = 0;
		}
		if (segment[selector] > 100) {
			segment[selector] = 100;
		}
		draw();
	}
	public static void moveSelector(int dir) {
		selector += dir;
		if (selector < 0) {
			selector = 0;
		}
		if (selector > 39) {
			selector = 39;
		}
		draw();
	}
	public static void draw() {
		StdDraw.clear();
		StdDraw.setPenRadius(0.005);
		StdDraw.setPenColor(StdDraw.GREEN);
		StdDraw.line(0, 50, 80, 50);
		StdDraw.setPenColor(StdDraw.BLACK);

		for (int i = 1; i < 40; i++) {
			//double dist1 = (1-((double)(Math.abs((i-1)-20))/20)) * 30.0;
			//double dist2 = (1-((double)(Math.abs(i-20))/20)) * 30.0;
			double dist1 = 13 - Math.pow((i-19.5)-1, 2) / (30);
			double dist2 = 13 - Math.pow((i-19.5), 2) / (30);
			//System.out.println(dist1);
			StdDraw.line((i-1)*2,segment[i-1] - dist1, i*2,segment[i] - dist2);
			StdDraw.line((i-1)*2,segment[i-1] + dist1, i*2,segment[i] + dist2);
		}
		for (int i = 0; i < 40; i++) {
			if (selector == i) {
				StdDraw.setPenRadius(0.015);
				StdDraw.setPenColor(StdDraw.BLUE);
			} else {
				StdDraw.setPenRadius(0.01);
				StdDraw.setPenColor(StdDraw.RED);
			}
			StdDraw.point(i*2,segment[i]);
		}
	}
}
