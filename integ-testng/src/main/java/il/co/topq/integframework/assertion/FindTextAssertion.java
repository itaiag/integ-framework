package il.co.topq.integframework.assertion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Asserts that given text exists in an actual text
 * 
 * @author Itai Agmon
 * 
 */
public class FindTextAssertion extends AbstractAssertionLogic<String> {

	private final String expectedText;

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
		if (actual == null) {
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
				Matcher matcher = pattern.matcher(actual);
				status = matcher.find();

			} catch (Throwable t) {
				status = false;
			}

		} else {
			status = actual.contains(expectedText);
		}

		title = "Expected text [" + expectedText + "] was " + (status ? "" : "not ") + "found in actual text [" + actual + "]";

		StringBuilder messageBuilder = new StringBuilder();
		messageBuilder.append("Text to find: \n");
		messageBuilder.append(expectedText).append("\n\n");
		messageBuilder.append("Actual text\n");
		messageBuilder.append(actual);
		message = messageBuilder.toString();

	}
}
