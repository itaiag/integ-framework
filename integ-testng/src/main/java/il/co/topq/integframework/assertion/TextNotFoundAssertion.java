package il.co.topq.integframework.assertion;

import il.co.topq.integframework.utils.StringUtils;


/**
 * Asserts that given text does not exists in an actual text
 * 
 * @author Aharon Hacmon
 * 
 */
public class TextNotFoundAssertion extends AbstractAssertionLogic<String> {

	private final String expectedText;

	private String actualText;

	private final boolean isRegex;

	/**
	 * 
	 * @param expectedText
	 *            text to find in the actual text
	 * @param isRegex
	 *            If set to true the expected text would be handled as regular
	 *            expression
	 */
	public TextNotFoundAssertion(String expectedText, boolean isRegex) {
		super();
		this.expectedText = expectedText;
		this.isRegex = isRegex;
	}

	public TextNotFoundAssertion(String expectedText) {
		this(expectedText, false);
	}

	@Override
	public void doAssertion() {
		if (StringUtils.isEmpty(actualText) && StringUtils.isEmpty(expectedText)){
			title="Both expected and actual strings are empty";
			status = true;
		} else {
			FindTextAssertion findTextAssertion = new FindTextAssertion(expectedText, isRegex);
			findTextAssertion.setActual(actualText);
			findTextAssertion.doAssertion();
			status = !findTextAssertion.status;
			title = findTextAssertion.title.replace("can't be", "is");
		}
		
	}
	
	@Override
	public void setActual(String actual) {
		this.actual = actual;
		if (actual != null) {
			actualText = (String) actual;
		}
	}
}
