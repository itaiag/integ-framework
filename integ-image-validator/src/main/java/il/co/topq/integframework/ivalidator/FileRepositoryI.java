package il.co.topq.integframework.ivalidator;

import java.io.File;

public interface FileRepositoryI {
	
	void addFiles(File[] imgFile, String[] props);

	File[] getFiles(String[] props);
}
