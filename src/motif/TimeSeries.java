package motif;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Scanner;

import stdlib.StdDraw;
import suffixtree.GeneralizedSuffixTree;

/**
 * Time Series Motif Recognition
 * 
 * @author Kyle Moy
 *
 */
public class TimeSeries {
	
	/**
	 * Constructor
	 * 
	 * @param ts A time series as an array of double
	 */
	private TimeSeries (double[] ts) {
	}
	
	/**
	 * @param data The data to calculate on
	 * @param mean The mean of the data
	 * @return The standard deviation of the data
	 */
	private static double std(double[] data, double mean) {
		double sqdist = 0;
		for (double i : data) {
			sqdist += Math.pow(i - mean,2);
		}
		sqdist /= data.length;
		return Math.sqrt(sqdist);
	}
	
	/**
	 * @param data The data to calculate on
	 * @return The mean
	 */
	private static double mean(double[] data) {
		double mean = 0;
		for (double i : data) {
			mean += i;
		}
		return (mean / data.length);
	}
	
	/**
	 * @param d The number of dimensions to reduce to
	 * @return A time series with a reduced resolution
	 */
	public static double[] reduceDimensions(double[] ts, int d) {
		double[] reducedTimeSeries = new double[d];
		int n = ts.length;
		int s = (int) Math.ceil((double)n / (double)d);
		for (int i = 0; i < n; i++) {
			reducedTimeSeries[i/s] += ts[i];
		}
		for (int i = 0; i < d; i++) {
			if (i * s <= n) {
				reducedTimeSeries[i] /= s;
			} else {
				reducedTimeSeries[i] /= n % s;
			}
		}
		return reducedTimeSeries;
	}
	
	/**
	 * @param x
	 * @return
	 */
	private static double phi(double x) {
		return Math.exp(-x * x / 2) / Math.sqrt(2 * Math.PI);
    }
	
	/**
	 * Normalizes a value
	 * @param x The value to normalize
	 * @param mu The mean of the data
	 * @param sigma The standard deviation of the media
	 * @return The normalized value
	 */
	private static double normalize(double x, double mu, double sigma) {
		return (x - mu) / sigma;
	}

	private static double[] normalize(double[] x) {
		double mean = mean(x);
		double std = std(x, mean);
		for (int i = 0; i < x.length; i++)
			x[i] = normalize(x[i], mean, std);
		return x;
	}
    public static double Phi(double z) {
        if (z < -8.0) return 0.0;
        if (z >  8.0) return 1.0;
        double sum = 0.0, term = z;
        for (int i = 3; sum + term != sum; i += 2) {
            sum  = sum + term;
            term = term * z * z / i;
        }
        return 0.5 + sum * phi(z);
    }
    
    /**
     * A wrapper method, finds up the value for a given Z score
     * @param z Z score to find
     * @return The value associated with the Z score
     */
    public static double inverseZScore(double z) {
        return inverseZScore(z, .00000001, -8, 8);
    } 
    
    /**
     * @param z
     * @param delta
     * @param lo
     * @param hi
     * @return
     */
    private static double inverseZScore(double z, double delta, double lo, double hi) {
        double mid = lo + (hi - lo) / 2;
        if (hi - lo < delta) return mid;
        if (Phi(mid) > z) return inverseZScore(z, delta, lo, mid);
        else              return inverseZScore(z, delta, mid, hi);
    }

    public static double[] discretize(double[] ts, int a) {
    	int n = ts.length;
    	double[] ord = new double[n];
    	double[] breakpoint = new double[a];
    	
    	//Breakpoints
    	double P = 1.0 / a;
    	double incr = 1.0 / a;
    	double mu = mean(ts);
    	double sigma = std(ts,mu);
    	for (int i = 0; i < a; i++) {
    		breakpoint[i] = inverseZScore(P);
    		if (i == a-1) breakpoint[i] = Double.MAX_VALUE;
    		P += incr;
    		//System.out.println(breakpoint[i]);
    	}
    	for (int i = 0; i < n; i++) {
    		double score = normalize(ts[i], mu, sigma);
    		for (int j = 0; j < a; j++) {
    			if (score < breakpoint[j]) {
    				ord[i] = j;
    				break;
    			}
    		}
    	}
    	return ord;
    }
    public static void main(String[] args) throws Exception {
		int n = 159;
		int d = 80;
		int a = 8;
		double[] ts = new double[n];
		double[] rts;
		double[] dts;
		System.out.println("Building Time Series...");
		Scanner in = new Scanner (new File("data/unemployment"));
		for (int i = 0; i < n; i++) {
			ts[i] = in.nextDouble();
			//ts[i] = Math.sin(i/10.0);
		}

		System.out.println("Normalizing Time Series...");
		ts = normalize(ts);
		System.out.println("Dimensionality Reduction...");
		rts = reduceDimensions(ts,d);
		System.out.println("Discretization...");
		dts = discretize(rts, a);
		String series = "";
		for (double i : dts) {
			series += "" + (int)i;
			//System.out.print((int) i);
		}
		//System.out.println();
		System.out.println("Counting Suffixes...");
		Hashtable<String,Integer> ht = new Hashtable<String,Integer>();
		for (int i = 2; i < d; i++) {
			if (i % 100 == 0) System.out.println(i);
			for (int j = 0; j < d - (i + 1); j++) {
				String search = series.substring(j, j+i);
				if (ht.containsKey(search)) {
					ht.put(search, ht.get(search) + 1);
				} else {
					ht.put(search, 1);
				}
			}
		}
		System.out.println("Building Priority Queue...");
		PriorityQueue<Pair> pq = new PriorityQueue<Pair>();
		for (String s : ht.keySet()) {
			int c = ht.get(s);
			if (c > 1) {
				pq.add(new Pair(s,c));
			}
		}		
		
		System.out.println("Drawing Graph...");
		StdDraw.setCanvasSize(800,600);
		StdDraw.setXscale(0, n);
		StdDraw.setYscale(-2,2);
		StdDraw.setPenRadius(0.005);
		StdDraw.line(0, 0, n, 0);
		StdDraw.line(0, -2, 0, 2);
		StdDraw.setPenColor(StdDraw.BLUE);
		StdDraw.show(100);
		for (int i = 0; i < n; i++) {
			StdDraw.point(i, ts[i]);
		}
		StdDraw.show();
		System.out.println("Finding motifs...");
		GeneralizedSuffixTree gst = new GeneralizedSuffixTree();
		ArrayList<Pair> results = new ArrayList<Pair>();
		int ind = 0;
		Color color[] = {StdDraw.RED,StdDraw.GREEN,StdDraw.BLUE,StdDraw.YELLOW,StdDraw.CYAN,StdDraw.ORANGE};
		int col = 0;
		StdDraw.setPenRadius(0.001);
		int count = 0;
		int num = 1;
		while (!pq.isEmpty()) {
			Pair p = pq.remove();
			Collection<Integer> c = gst.search(p.key);
			if (c == null) { //This string is very unique
				gst.put(p.key, ind++);
				results.add(p);

				if (count < num) {
					count ++;
					StdDraw.setPenColor(color[col++]);
					int z = 0;
					int v = 0;
					while (z < series.length()) {
						int st = series.indexOf(p.key, z);
						if (st == -1) break;
						int en = st + p.key.length();
						z = st + 1;
						double s = (n/d) +1 ;
						double h = 2.3 - ((v % 3) * 0.05) - (col * 0.2);
						StdDraw.line(st * s, -4, st * s, h);
						StdDraw.line(en * s, -4, en * s, h);
						StdDraw.line(st * s, h, en * s, h);
						v++;
					}
				}
				//System.out.println(p.count + ": " + p.key);
			} else {
				//This string already exists in the result set as a sub string
				//Determine if it is unique by checking if it occurs more than its super string (occuring less times is not possible)
				int longest = 0;
				boolean unique = false;
				for (Pair q : results) {
					if (q.contains(p.key)) {
						if (q.count > longest) { //Only check against the longest superstring
							if (p.count > q.count) {
								longest = q.count;
								unique = true;
							} else unique = false;
						}
					}
				}
				if (unique) { //This string occurs more times than its superstring
					gst.put(p.key, ind++);
					results.add(p);

					if (count < num) {
						count ++;
						StdDraw.setPenColor(color[col++]);
						int z = 0;
						int v = 0;
						while (z < series.length()) {
							int st = series.indexOf(p.key, z);
							if (st == -1) break;
							int en = st + p.key.length();
							z = st + 1;
							double s = (n/d) +1 ;
							double h = 2.5 - ((v % 3) * 0.1) - (col * 0.35);
							StdDraw.line(st * s, -2, st * s, h);
							StdDraw.line(en * s, -2, en * s, h);
							StdDraw.line(st * s, h, en * s, h);
							v++;
						}
					}
					//System.out.println(p.count + ": " + p.key);
				}
			}
		}
		System.out.println("Complete.");
    }
    private static class Pair implements Comparable<Pair>{
    	final public String key;
    	final public int count;
    	private Pair (String k, int c) {
    		key = k;
    		count = c;
    	}
		@Override
		public int compareTo(Pair o) {
			if (this.key.length() < o.key.length()) return 1;
			else if (this.key.length() == o.key.length()) {
	    		if (this.count > o.count) return -1;
	    		if (this.count < o.count) return 1;
	    		return 0;
			} else return -1;
		}
		public boolean contains(String s) {
			return this.key.contains(s);
		}
    }
}
