package il.co.topq.integframework.webdriver.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
	/**
	 * save InputStream to File
	 * @param in the input stream to read drom
	 * @param file the file to write to
	 * @throws IOException when something bad happens
	 */
	public static void saveInputStreamToFile(InputStream in, File file) throws IOException {
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
