package skeleton;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;


public class skelTestCase {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String out = "data/skeletonTest.dat";
		FileOutputStream fos = new FileOutputStream(out);
		DataOutputStream dos = new DataOutputStream(fos);
		int c = 0;
		int n = 10;
		for (int i = 0; i < 10; i++) {
			dos.writeInt(n);
			for (int j = 0; j < n; j++) {
				dos.writeInt(j % 2);
				dos.writeInt(j % 2);
			}
		}
		System.out.println("done");
		dos.close();
		fos.close();
	}
}
