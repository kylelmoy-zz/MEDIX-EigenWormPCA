package skeleton;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import stdlib.StdDraw;

public class PointSetVideo implements Runnable{
	static PointSetVideo skeleton, contour;
	public static void main (String[] args) throws Exception {
		//SMALLEST N == 224
		//System.out.println("Smallest n: " + smallest);
		
		//Multithread! Load video while playing it
		skeleton = new PointSetVideo("data/skeleton.txt");
		//contour = new PointSetVideo("data/contour.txt");
		Thread t1 = new Thread(skeleton,"skeleton");
		//Thread t2 = new Thread(contour,"contour");
		t1.start();
		//t2.start();
		//Display video (or buffer)
		int c = 0;
		StdDraw.setCanvasSize(512,512);
		StdDraw.setXscale(-128,128);
		StdDraw.setYscale(-128,128);
		StdDraw.setPenRadius(0.001);
		StdDraw.show(100);
		while (true) {
			if (StdDraw.isKeyPressed(KeyEvent.VK_Q)) {
				break;
			}
			if (c >= skeleton.n) {
				if (skeleton.done) {
					//Restart!
					c = 0;
				} else {
					//Buffer!
					StdDraw.show(100);
					continue;
				}
			}
			if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
				if (StdDraw.isKeyPressed(KeyEvent.VK_SHIFT)) {
					c+= 10;
					draw(c);
				} else {
					draw(c++);
				}
			}
			if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
				draw(c--);
			}
			StdDraw.show(100);
		}
	}
	public static void draw(int c) {
		StdDraw.clear();
		StdDraw.setPenRadius(0.001);
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.text(50, 128, "" + c + " / " + skeleton.n);
		StdDraw.setPenColor(StdDraw.RED);
		CoordSet skel = skeleton.get(c);
		skel.sample(100); //derp
		skel.draw(skel.xMean, skel.yMean);
		//StdDraw.show(100);
	}
	
	File file;
	boolean done = false;
	ArrayList<CoordSet> pointSet = new ArrayList<CoordSet>();
	int n = 0;
	
	public PointSetVideo (String skl) {
		file = new File(skl);
	}
	public CoordSet get (int i) {
		synchronized(pointSet) {
			return pointSet.get(i);
		}
	}
	@Override
	public void run() {
		Scanner in;
		try {
			in = new Scanner (file);
			//int smallest = Integer.MAX_VALUE;
			while (in.hasNext()) {
				 String line = in.nextLine();
				 CoordSet toAdd = new CoordSet(line);
				 synchronized (pointSet) {
					 pointSet.add(toAdd);
				 }
				 //Skeleton derp = new Skeleton(line);
				 //if (derp.n < smallest) smallest = derp.n;
				 n++;
				 //if (n % 128 == 0) System.out.println(n);
				 //if (n > 1024) break;
			}
			in.close();
			done = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
