package il.co.topq.integframework.ivalidator;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestImageMagick {

	private final static String IMAGE_MAGICK_PATH = "C:\\Program Files (x86)\\ImageMagick-6.8.5-Q16";
	private ImageMagick comparator;

	private File imgFile1;
	private File imgFile2;
	private File maskFile;
	private File maskedImageFile;
	private File resultFile = new File("result.png");

	@Before
	public void setUp() throws IOException {
		imgFile1 = TestUtils.getFileFromResources("img1.png");
		imgFile2 = TestUtils.getFileFromResources("img2.png");
		maskFile = TestUtils.getFileFromResources("mask1.png");
		maskedImageFile = TestUtils.getFileFromResources("img1masked1.png");
		comparator = new ImageMagick(IMAGE_MAGICK_PATH);
	}

	@After
	public void tearDown() {
		resultFile.delete();
	}

//	@Test
	public void testAddMask() throws ImageComparatorException {
		File newFile = comparator.applyMask(imgFile1, maskFile);
		Assert.assertTrue(newFile.exists());
		Assert.assertEquals(maskedImageFile.length(), newFile.length());
	}

//	@Test
	public void testCompareSuccess() throws ImageComparatorException {
		boolean equals = comparator.compare(imgFile1, imgFile1, resultFile);
		Assert.assertTrue(equals);
	}

//	@Test
	public void testCompareFailure() throws ImageComparatorException {
		boolean equals = comparator.compare(imgFile1, imgFile2, resultFile);
		Assert.assertFalse(equals);
	}

//	@Test
	public void testCompareSuccessWithMask() throws ImageComparatorException {
		File imgMasked1 = comparator.applyMask(imgFile1, maskFile);
		File imgMasked2 = comparator.applyMask(imgFile2, maskFile);
		boolean equals = comparator.compare(imgMasked1, imgMasked2, resultFile);
		Assert.assertTrue(equals);
	}

}
