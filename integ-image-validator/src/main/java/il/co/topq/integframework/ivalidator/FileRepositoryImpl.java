package il.co.topq.integframework.ivalidator;

import java.io.File;

public class FileRepositoryImpl implements FileRepositoryI {

	private final File repositoryPath;

	public FileRepositoryImpl(File repositoryPath) {
		super();
		this.repositoryPath = repositoryPath;
	}

	@Override
	public void addFiles(File imgFile[], String[] props) {

	}

	@Override
	public File[] getFiles(String[] props) {
		return null;
	}

}
