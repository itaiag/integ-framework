package il.co.topq.integframework.ivalidator;

import java.io.File;

public interface ImageComparatorI {

	public boolean compare(File actualImg, File expectedImg, File resultFile) throws ImageComparatorException;

	public File applyMask(File imgFile, File mask) throws ImageComparatorException;

}
