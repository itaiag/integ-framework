package il.co.topq.integframework.assertion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Asserts that given text exists in an actual text
 * 
 * @author Ita Agmon
 * 
 */
public class FindTextAssertion extends AbstractAssertionLogic {

	private final String expectedText;

	private String actualText;

	private final boolean isRegex;

	/**
	 * 
	 * @param expectedText
	 *            text in find in the actual text
	 * @param isRegex
	 *            If set to treu the expcected text would be handled as regular
	 *            expression
	 */
	public FindTextAssertion(String expectedText, boolean isRegex) {
		super();
		this.expectedText = expectedText;
		this.isRegex = isRegex;
	}

	public FindTextAssertion(String expectedText) {
		this(expectedText, false);
	}

	@Override
	public void doAssertion() {
		if (actualText == null) {
			status = false;
			title = "Actual text can't be null";
			return;
		}
		if (expectedText == null) {
			status = false;
			title = "Expected text can't be null";
			return;
		}

		if (isRegex) {
			try {
				Pattern pattern = Pattern.compile(expectedText);
				Matcher matcher = pattern.matcher(actualText);
				status = matcher.find();

			} catch (Throwable t) {
				status = false;
			}

		} else {
			status = actualText.contains(expectedText);
		}
		if (status) {
			title = "Expected text was found in actual text";
		} else {
			title = "Expected text was not found in actual text";
		}
		StringBuilder messageBuilder = new StringBuilder();
		messageBuilder.append("Text to find: \n");
		messageBuilder.append(expectedText).append("\n\n");
		messageBuilder.append("Actual text\n");
		messageBuilder.append(actualText);
		message = messageBuilder.toString();

	}

	@Override
	public Class<?> getActualClass() {
		return String.class;
	}

	@Override
	public void setActual(Object actual) {
		this.actual = actual;
		if (actual != null) {
			actualText = (String) actual;
		}
	}

}
