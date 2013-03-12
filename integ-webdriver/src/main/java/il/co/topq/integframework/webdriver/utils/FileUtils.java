package il.co.topq.integframework.webdriver.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileUtils {
	/**
	 * 
	 */
	public static void saveInputStreamToFile(InputStream in, File file) throws Exception {
		if (file.getParentFile() != null) {
			file.getParentFile().mkdirs();
		}
		FileOutputStream fos = new FileOutputStream(file);
		try {
			byte[] buf = new byte[4000];
			int c;
			while (true) {
				c = in.read(buf);
				if (c == -1) {
					break;
				}
				fos.write(buf, 0, c);
			}
		} finally {
			fos.close();
		}
	}
}
