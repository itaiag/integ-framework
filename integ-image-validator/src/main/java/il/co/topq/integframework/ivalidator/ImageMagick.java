package il.co.topq.integframework.ivalidator;

import il.co.topq.integframework.cli.process.ProcessHandler;

import java.io.File;
import java.io.IOException;

public class ImageMagick implements ImageComparatorI {

	private final String imageMagickPath;
	/**
	 * Apply mask to image file. <br>
	 * 1. Imagemagick path<br>
	 * 2. Original image.<br>
	 * 3. The map image<br>
	 * 4. Output image<br>
	 */
	private final static String APPLY_MASK = "\"%s\"/convert  \"%s\"  \"%s\" -compose DstOut -composite  \"%s\"";

	/**
	 * Compare <br>
	 * 1. Imagemagick path<br>
	 * 2. First image file<br>
	 * 3. Second image file<br>
	 * 4. Result file
	 * 
	 */
	private final static String COMPARE1 = "\"%s\"/compare -compose src \"%s\" \"%s\" \"%s\"";
	
	private final static String COMPARE2 = "\"%s\"/composite \"%s\" \"%s\" -compose difference \"%s\"";
	
	/**
	 * Compare <br>
	 * 1. Imagemagick path<br>
	 * 2. First image file<br>
	 * 3. Second image file<br>
	 * 4. Result file
	 * 
	 */
	private final static String COMPARE = "\"%s\"/compare -verbose -metric mae \"%s\" \"%s\" \"%s\"";

	public ImageMagick(String imageMagickPath) throws IOException {
		super();
		this.imageMagickPath = imageMagickPath;
	}

	@Override
	public boolean compare(File actualImg, File expectedImg, File resultFile) throws ImageComparatorException {
		if (!resultFile.exists()) {
			try {
				if (!resultFile.createNewFile()) {
					throw new ImageComparatorException("Failed to create result file ");
				}
			} catch (IOException e) {
				throw new ImageComparatorException("Failed to create result file due to IOException " + e.getMessage(),
						e);
			}
		}
		int errorLevel = runCommand(String.format(COMPARE, imageMagickPath, actualImg.getAbsolutePath(), expectedImg.getAbsolutePath(),
				resultFile.getAbsolutePath()), false);
		return errorLevel == 0;
	}

	private int runCommand(String commandLine, boolean assertErrorLevel) throws ImageComparatorException {
		final ProcessHandler pe = new ProcessHandler(new NoErrorValidationExecutor());
		try {
			int errorLevel = pe.execute(new File("."), commandLine, true, 5000);
			if (assertErrorLevel && errorLevel != 0) {
				throw new ImageComparatorException("Failed to execute imagemagick command");
			}
			return errorLevel;

		} catch (IOException e) {
			throw new ImageComparatorException("Failed to execute imagemagick command due to IOException "
					+ e.getMessage(), e);
		}

	}

	@Override
	public File applyMask(File imgFile, File mask) throws ImageComparatorException {
		File maskedImage = null;
		try {
			maskedImage = File.createTempFile(imgFile.getName(), ".png");
		} catch (IOException e) {
			throw new ImageComparatorException("failed to create temp file", e);
		}
		runCommand(String.format(APPLY_MASK, imageMagickPath, imgFile.getAbsolutePath(), mask.getAbsolutePath(),
				maskedImage.getAbsolutePath()), true);
		return maskedImage;
	}

	public String getImageMagickPath() {
		return imageMagickPath;
	}

}
