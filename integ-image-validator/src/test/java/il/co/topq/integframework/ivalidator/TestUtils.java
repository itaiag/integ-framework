package il.co.topq.integframework.ivalidator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

public class TestUtils {
	
	public static File getFileFromResources(String fileName) throws IOException {
		InputStream inputStream = new Object().getClass().getResourceAsStream("/" + fileName);
		File file = new File(fileName);
		OutputStream outputStream = new FileOutputStream(file);
		IOUtils.copy(inputStream, outputStream);
		return file;
	}
	
}
