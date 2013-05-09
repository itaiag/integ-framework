package il.co.topq.integframework.ivalidator;

import java.io.File;

import javax.annotation.PostConstruct;

public class ImageValidator {

	private boolean createRepository = false;

	private ImageComparatorI comparator;

	private FileRepositoryI repository;

	@PostConstruct
	public void init() {
		if (null == comparator) {
			throw new IllegalArgumentException("Please specify image comparator");
		}
		if (null == repository) {
			throw new IllegalArgumentException("Please specify file repository");
		}

	}

	public void validate(File imgFile, String[] props) throws ImageComparatorException {
		if (createRepository) {
			repository.addFiles(new File[]{imgFile}, props);
			return;
		}
		File[] repoFiles = repository.getFiles(props);
		//TODO: Validate number of files returned
		File expectedFile = repoFiles[0];
		File maskFile = repoFiles[1];
		
		expectedFile = comparator.applyMask(expectedFile, maskFile);
		File actualFile = comparator.applyMask(imgFile, maskFile);
		File resultFile = new File("resultFile.png");
		comparator.compare(actualFile, expectedFile, resultFile);
	}

	public boolean isCreateRepository() {
		return createRepository;
	}

	public void setCreateRepository(boolean createRepository) {
		this.createRepository = createRepository;
	}

	public ImageComparatorI getComparator() {
		return comparator;
	}

	public void setComparator(ImageComparatorI comparator) {
		this.comparator = comparator;
	}

}
