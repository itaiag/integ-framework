package il.co.topq.integframework.ivalidator.resultHandler;

import java.io.File;

public class DefaultValidatorResultHandlerImpl implements ValidationResultsHandlerI {

	@Override
	public void handle(final File imgFile, String[] props, boolean result, File resultFile) {
		System.out.println(result ? "Validator result for file " + imgFile.getName() + " was successful"
				: "Validator result for file " + imgFile.getName() + " was unsuccessful");
		if (!result) {
			System.out.println("Result file:" + resultFile.getAbsolutePath());
		}

	}

}
