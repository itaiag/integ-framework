package il.co.topq.integframework.ivalidator;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

public class TestFileRepositoryImpl {
	private FileRepositoryI repo;
	private File repositoryPath;

	@Before
	public void setUp() throws FileRepositoryException, IOException {
		repo = new FileRepositoryImpl(null);
		repositoryPath = ((FileRepositoryImpl) repo).getRepositoryPath();
		if (repositoryPath.exists()) {
			if (!FileUtils.deleteQuietly(repositoryPath)) {
				System.out.println("Failed to delete repository folder");
			}
		}
	}

	@Test
	public void testAddFile() throws IOException, FileRepositoryException {
		File file = createFileWithContent();
		final File repoFile = new File(((FileRepositoryImpl) repo).getRepositoryPath().getAbsolutePath()
				+ File.separator + "foatt1" + File.separator + "foatt2" + File.separator + "foatt3",
				"fiatt1-fiatt2-fiatt3-" + file.getName());

		repo.addFile(file, new String[] { "foatt1", "foatt2", "foatt3" }, new String[] { "fiatt1", "fiatt2", "fiatt3" });
		Assert.assertTrue(repoFile.exists());
		Assert.assertEquals(FileUtils.readFileToString(repoFile), FileUtils.readFileToString(file));
		file.delete();

	}

	private File createFileWithContent() throws IOException {
		File file = File.createTempFile("myFile", ".txt");
		String content = "File content " + System.currentTimeMillis();
		FileUtils.write(file, content);
		return file;
	}

	@Test
	public void testGetFile() throws Exception {
		File file = createFileWithContent();
		repo.addFile(file, new String[] { "foatt1", "foatt2", "foatt3" }, new String[] { "fiatt1", "fiatt2", "fiatt3" });
		File[] repoFiles = repo.getFiles(new String[] { "foatt1", "foatt2", "foatt3" }, new String[] { "fiatt1",
				"fiatt2", "fiatt3" });
		Assert.assertNotNull(repoFiles);
		Assert.assertEquals(1, repoFiles.length);
		Assert.assertEquals(FileUtils.readFileToString(repoFiles[0]), FileUtils.readFileToString(file));
		file.delete();

	}

	@Test
	public void testAddExistingFile() throws FileRepositoryException, IOException {
		File file = createFileWithContent();
		repo.addFile(file, new String[] { "foatt1", "foatt2", "foatt3" }, new String[] { "fiatt1", "fiatt2", "fiatt3" });
		File[] repoFiles = repo.getFiles(new String[] { "foatt1", "foatt2", "foatt3" }, new String[] { "fiatt1",
				"fiatt2", "fiatt3" });
		Assert.assertNotNull(repoFiles);
		Assert.assertEquals(1, repoFiles.length);
		Assert.assertEquals(FileUtils.readFileToString(repoFiles[0]), FileUtils.readFileToString(file));

		// The content will be different now.
		FileUtils.write(file, "New content");
		repo.addFile(file, new String[] { "foatt1", "foatt2", "foatt3" }, new String[] { "fiatt1", "fiatt2", "fiatt3" });
		repoFiles = repo.getFiles(new String[] { "foatt1", "foatt2", "foatt3" }, new String[] { "fiatt1", "fiatt2",
				"fiatt3" });
		Assert.assertNotNull(repoFiles);
		Assert.assertEquals(1, repoFiles.length);
		Assert.assertEquals(FileUtils.readFileToString(repoFiles[0]), FileUtils.readFileToString(file));
		file.delete();
	}

	@Test
	public void addMultipleFile() throws Exception {
		for (int i = 0; i < 10; i++) {
			File file = createFileWithContent();
			repo.addFile(createFileWithContent(), new String[] { "foatt1", "foatt2", "foatt3" }, new String[] {
					"fiatt1", "fiatt2", "fiatt3" });
			file.delete();
		}
		Assert.assertEquals(
				10,
				repo.getFiles(new String[] { "foatt1", "foatt2", "foatt3" }, new String[] { "fiatt1", "fiatt2",
						"fiatt3" }).length);

	}

	@Test
	public void readFileWithoutFileAttributes() throws Exception {
		File file = createFileWithContent();
		repo.addFile(file, new String[] { "foatt1", "foatt2", "foatt3" }, new String[] { "fiatt1", "fiatt2", "fiatt3" });
		File[] repoFiles = repo.getFiles(new String[] { "foatt1", "foatt2", "foatt3" });
		Assert.assertNotNull(repoFiles);
		Assert.assertEquals(1, repoFiles.length);
		Assert.assertEquals(FileUtils.readFileToString(repoFiles[0]), FileUtils.readFileToString(file));
		file.delete();

	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddNoneExistFile() throws Exception {
		File file = createFileWithContent();
		file.delete();
		try {
			repo.addFile(file, new String[] { "foatt1", "foatt2", "foatt3" }, new String[] { "fiatt1", "fiatt2",
					"fiatt3" });

		} finally {
			file.delete();
		}

	}

	@Test(expected = IllegalArgumentException.class)
	public void testExceptionWhenNoFolderAttributes() throws Exception {
		File file = createFileWithContent();
		try {
			repo.addFile(file, null, null);
		} finally {
			file.delete();
		}

	}

	@Test
	public void testAddFileWithNoAttributes() throws Exception {
		File file = createFileWithContent();
		try {
			repo.addFile(file, new String[] { "att0" }, null);
			File[] repoFiles = repo.getFiles(new String[] { "att0" });
			Assert.assertNotNull(repoFiles);
			Assert.assertEquals(1, repoFiles.length);
			Assert.assertEquals(FileUtils.readFileToString(repoFiles[0]), FileUtils.readFileToString(file));

			
			
		} finally {
			file.delete();
		}
	}

	@Test(expected = FileRepositoryException.class)
	public void TryToRetreiveFromNoneExistFolder() throws FileRepositoryException {
		repo.getFiles(new String[] { "noneExist0", "NoneExist1" });
	}
}
