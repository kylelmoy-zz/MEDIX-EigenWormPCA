package skeleton;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import stdlib.StdDraw;

public class PointSetVideo implements Runnable{
	public static void main (String[] args) throws Exception {
		//SMALLEST N == 224
		//System.out.println("Smallest n: " + smallest);
		
		//Multithread! Load video while playing it
		PointSetVideo skeleton = new PointSetVideo("data/downSampled40.txt");
		PointSetVideo contour = new PointSetVideo("data/contour.txt");
		Thread t1 = new Thread(skeleton,"skeleton");
		Thread t2 = new Thread(contour,"contour");
		t1.start();
		t2.start();
		//Display video (or buffer)
		int c = 0;
		StdDraw.setCanvasSize(1024,1024);
		StdDraw.setXscale(0,1024);
		StdDraw.setYscale(1024,0);
		StdDraw.setPenRadius(0.001);
		StdDraw.show(100);
		while (true) {
			if (StdDraw.isKeyPressed(KeyEvent.VK_Q)) {
				break;
			}
			if (c >= contour.n || c >= skeleton.n) {
				if (contour.done && skeleton.done) {
					//Restart!
					c = 0;
				} else {
					//Buffer!
					StdDraw.show(100);
					continue;
				}
			}
			StdDraw.clear();
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.text(50, 50, "" + c + " / " + skeleton.n + " / " + contour.n);

			StdDraw.setPenColor(StdDraw.BLACK);
			contour.get(c).draw();
			StdDraw.setPenColor(StdDraw.RED);
			skeleton.get(c).draw();
			StdDraw.show(100);
			c++;
		}
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
