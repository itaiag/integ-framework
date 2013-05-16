package il.co.topq.integframework.ivalidator;

import java.io.File;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

public class TestImageValidator {

	private ImageValidator validator;
	private File img1;
	private File img2;
	private File mask1;
	private File repositoryPath = new File("repository");
	private String[] props = new String[] { "prop1", "prop2", "prop3" };

	@Before
	public void setUp() throws Exception {
		validator = new ImageValidator(new FileRepositoryImpl(repositoryPath), new ImageMagick(
				CommonResources.IMAGE_MAGICK_PATH));
		img1 = TestUtils.getFileFromResources("img1.png");
		img2 = TestUtils.getFileFromResources("img2.png");
		mask1 = TestUtils.getFileFromResources("mask1.png");
		if (repositoryPath.exists()) {
			if (!FileUtils.deleteQuietly(repositoryPath)) {
				System.out.println("Failed to delete repository folder");
			}
		}
	}

	@Test
	public void testCreateRepository() throws Exception {
		validator.setCreateRepository(true);
		validator.validate(img1, props);
		File repoFile = new File(repositoryPath, props[0] + File.separator + props[1] + File.separator + props[2]
				+ File.separator + img1.getName());
		Assert.assertTrue(repoFile.exists());
	}

	@Test
	public void testValidationSuccessWithMask() throws Exception {
		// Crating repository
		validator.setCreateRepository(true);
		validator.validate(img1, props);
		validator.validate(mask1, props);

		// Validating
		validator.setCreateRepository(false);
		Assert.assertTrue(validator.validate(img2, props));
	}
	
	@Test
	public void testValidationSuccessWithoutMask() throws Exception{
		// Crating repository
		validator.setCreateRepository(true);
		validator.validate(img1, props);

		// Validating
		validator.setCreateRepository(false);
		Assert.assertTrue(validator.validate(img1, props));
		
	}
	
	@Test
	public void testValidationFailureWithoutMask() throws Exception{
		// Crating repository
		validator.setCreateRepository(true);
		validator.validate(img1, props);

		// Validating
		validator.setCreateRepository(false);
		Assert.assertFalse(validator.validate(img2, props));
		
	}

}
