package il.co.topq.integframework.ivalidator;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

/**
 * 
 * @author Itai_Agmon
 * 
 */
public class FileRepositoryImpl implements FileRepositoryI {

	private final File repositoryPath;

	public FileRepositoryImpl(File repositoryPath) throws FileRepositoryException {
		super();
		if (null == repositoryPath) {
			repositoryPath = new File("repository");
		}
		this.repositoryPath = repositoryPath;
		if (!repositoryPath.exists()) {
			if (!repositoryPath.mkdirs()) {
				throw new FileRepositoryException("Failed to create repository path");
			}
		}

	}

	/**
	 * Adds file to the repository. If file already exist replaces the old file
	 * with the specified one.
	 * 
	 * @param file
	 *            File to add to the repository <br>
	 * @param folderAttributes
	 *            Folder attributes. Mandatory
	 * @param fileAttributes
	 *            file attributes. Can be null.
	 * @throws FileRepositoryException
	 *             If failed to delete existing file from repository<br>
	 *             If failed to create repository folder <br>
	 *             If failed to copy file to the repository.<br>
	 * 
	 * 
	 * 
	 * @throws IllegalArgumentException
	 *             if file is null or none exist <br>
	 *             If folder attributes are null or empty.
	 */
	@Override
	public void addFile(File file, String[] folderAttributes, String[] fileAttributes) throws FileRepositoryException {
		if (null == file || !file.exists()) {
			throw new IllegalArgumentException("File is null or none exist");
		}
		final StringBuilder path = createRepositoryFolders(folderAttributes);
		final File repositoryFile = createRepositoryFile(file, fileAttributes, path);
		if (repositoryFile.exists()) {
			if (!repositoryFile.delete()) {
				throw new FileRepositoryException("Failed to delete old file from repository");
			}
		}
		try {
			FileUtils.copyFile(file, repositoryFile);
		} catch (IOException e) {
			throw new FileRepositoryException("Failed to copy file to repository", e);
		}

	}

	/**
	 * Get all the files that has the folder attributes and begins with the
	 * specified file attributes. The order of the attributes doe's matters.
	 * 
	 * @param folderAttributes
	 *            Folder attributes. Mandatory
	 * @param fileAttributes
	 *            file attributes. Can be null.
	 * 
	 * @throws IllegalArgumentException
	 *             If folder attributes are null or empty<br>
	 * @throws FileRepositoryException
	 *             If repository folder with the specified attributes is not
	 *             exists<br>
	 * 
	 */
	@Override
	public File[] getFiles(String[] folderAttributes, String[] fileAttributes) throws FileRepositoryException {
		if (null == folderAttributes || folderAttributes.length == 0) {
			throw new IllegalArgumentException("Folder attributes can't be null or empty");
		}
		final File repoFolder = new File(generateFolderPath(folderAttributes).toString());
		if (!repoFolder.exists() || !repoFolder.isDirectory()) {
			throw new FileRepositoryException("folder with specified folder attributes is not exist");
		}
		if (null == fileAttributes || fileAttributes.length == 0) {
			return repoFolder.listFiles();
		}
		final String filePrefix = generateRepoFileNamePrefix(fileAttributes).toString();
		return repoFolder.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File file, String fileName) {
				if (fileName.startsWith(filePrefix)) {
					return true;
				}
				return false;
			}

		});

	}

	@Override
	public File[] getFiles(String[] folderAttributes) throws FileRepositoryException {
		return getFiles(folderAttributes, null);
	}

	private File createRepositoryFile(File file, String[] fileAttributes, final StringBuilder path) {
		final StringBuilder fileName = generateRepoFileNamePrefix(fileAttributes);
		fileName.append(file.getName());
		File repositoryFile = new File(path.toString(), fileName.toString());
		return repositoryFile;
	}

	private StringBuilder generateRepoFileNamePrefix(String[] fileAttributes) {
		final StringBuilder fileName = new StringBuilder();
		if (null == fileAttributes || fileAttributes.length == 0) {
			return fileName;
		}
		for (String fileAtt : fileAttributes) {
			fileName.append(fileAtt).append("-");
		}
		return fileName;
	}

	private StringBuilder createRepositoryFolders(String[] folderAttributes) throws FileRepositoryException {
		final StringBuilder path = generateFolderPath(folderAttributes);
		final File repoFolder = new File(path.toString());
		if (!repoFolder.exists() && !repoFolder.mkdirs()) {
			throw new FileRepositoryException("Failed to create folder with attributes "
					+ Arrays.toString(folderAttributes));
		}
		return path;
	}

	private StringBuilder generateFolderPath(String[] folderAttributes) throws FileRepositoryException {
		final StringBuilder path = new StringBuilder();
		if (null == folderAttributes || folderAttributes.length == 0) {
			throw new IllegalArgumentException("Folder attributes can't be null or empty");
		}
		try {
			path.append(repositoryPath.getCanonicalPath());
		} catch (IOException e) {
			throw new FileRepositoryException("Failed to retreive the canonical path of the repository");
		}
		path.append(File.separator);
		for (String folderAtt : folderAttributes) {
			path.append(folderAtt).append(File.separator);
		}
		return path;
	}

	public File getRepositoryPath() {
		return repositoryPath;
	}

}
