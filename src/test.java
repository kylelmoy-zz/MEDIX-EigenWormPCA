import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;


public class test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		RandomAccessFile f = new RandomAccessFile(new File("I:/derp"), "rw");
		f.seek(0);
		f.write(71);
		f.close();
	}

}
