package il.co.topq.integframework.ivalidator;

import java.io.File;

import javax.annotation.PostConstruct;

/**
 * 
 * @author Itai_Agmon
 * 
 */
public class ImageValidator {

	private final FileRepositoryI repository;

	private final ImageComparatorI comparator;

	private ValidationResultsHandlerI resultHandler;

	/**
	 * If set to true, do not validate images just add them to the repository
	 */
	private boolean createRepository;

	public ImageValidator(FileRepositoryI repository, ImageComparatorI comparator, boolean createRepository) {
		super();
		this.repository = repository;
		this.comparator = comparator;
		this.createRepository = createRepository;
		resultHandler = new DefaultValidatorResultHandlerImpl();
	}

	public ImageValidator(FileRepositoryI repository, ImageComparatorI comparator) {
		this(repository, comparator, false);
	}

	@PostConstruct
	public void init() {
		if (null == comparator) {
			throw new IllegalArgumentException("Please specify image comparator");
		}
		if (null == repository) {
			throw new IllegalArgumentException("Please specify file repository");
		}

	}

	/**
	 * 
	 * @param imgFile
	 * @param props
	 * @throws ImageComparatorException
	 */
	public boolean validate(final File imgFile, String[] props) throws ImageComparatorException {
		if (createRepository) {
			try {
				repository.addFile(imgFile, props, null);
			} catch (FileRepositoryException e) {
				throw new ImageComparatorException("Failed to add file to the repository", e);
			}
			return true;
		}
		File[] repoFiles = null;
		try {
			repoFiles = repository.getFiles(props);
		} catch (FileRepositoryException e) {
			throw new ImageComparatorException("Failed to get repository file", e);
		}
		File expectedFile = repoFiles[0];
		File maskFile = null;
		if (repoFiles.length > 1) {
			maskFile = repoFiles[1];
		}
		File actualFile = imgFile;
		if (maskFile != null) {
			expectedFile = comparator.applyMask(expectedFile, maskFile);
			actualFile = comparator.applyMask(imgFile, maskFile);
		}

		File resultFile = new File("resultFile.png");
		boolean result = comparator.compare(actualFile, expectedFile, resultFile);
		if (resultHandler != null) {
			resultHandler.handle(imgFile, props, result, resultFile);
		}
		return result;
	}

	public boolean isCreateRepository() {
		return createRepository;
	}

	public void setCreateRepository(boolean createRepository) {
		this.createRepository = createRepository;
	}

	public void setResultHandler(ValidationResultsHandlerI resultHandler) {
		this.resultHandler = resultHandler;
	}

}
