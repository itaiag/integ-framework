package il.co.topq.integframework.ivalidator.comparator;

import java.io.File;

/**
 * 
 * @author Itai_Agmon
 *
 */
public interface ImageComparatorI {

	public boolean compare(File actualImg, File expectedImg, File resultFile) throws ImageComparatorException;

	public File applyMask(File imgFile, File mask) throws ImageComparatorException;

}
