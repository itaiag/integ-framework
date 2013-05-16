package il.co.topq.integframework.ivalidator;

import java.io.File;

public interface ValidationResultsHandlerI {

	void handle(final File imgFile, String[] props,boolean result, File resultFile);

}
