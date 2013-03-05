package il.co.topq.integframework.assertion;

import il.co.topq.integframework.reporting.Reporter;
import il.co.topq.integframework.reporting.Reporter.Color;

/**
 * Class for comparing between actual and expected states
 * 
 * @author Itai Agmon
 * 
 */
public class Assert extends org.testng.Assert {

	static public void assertStringContains(final String actual, final String expected) {
		if (null == actual) {
			throw new IllegalArgumentException("Actual can't be null");
		}
		if (null == expected) {
			throw new IllegalArgumentException("logic can't be null");
		}
		if (!actual.contains(expected)) {
			throw new AssertionError(expected + " is not contained in " + actual);
		}

	}

	/**
	 * Execute the <code>logic</code> on the the <code>actual</code> object.
	 * 
	 * @param actual
	 *            Object to perform assertion on
	 * @param logic
	 *            Logic to operate on the actual object
	 * @throws Exception
	 *             If exception occurced during assertion
	 * @throws AssertionError
	 *             If assertion fails
	 */
	static public void assertLogic(final Object actual, final AbstractAssertionLogic logic) throws Exception {
		assertLogicHappens(actual, logic, 0);
	}

	static public void assertLogicHappens(final Object actual, final AbstractAssertionLogic logic, final long timeout)
			throws Exception {
		if (null == actual) {
			throw new IllegalArgumentException("Actual can't be null");
		}
		if (null == logic) {
			throw new IllegalArgumentException("logic can't be null");
		}
		if (!logic.getActualClass().isAssignableFrom(actual.getClass())) {
			Reporter.log("Actual type " + actual.getClass().getSimpleName() + " is not applicable for assertion logic");
			throw new IllegalStateException("Actual type " + actual.getClass().getSimpleName()
					+ " is not applicable for assertion logic");
		}
		logic.setActual(actual);
		try {
			logic.doAssertion();
			Reporter.log(logic.isStatus() ? "Assertion success: " : "Assertion failure: " + logic.getTitle(),
					logic.getMessage(), logic.isStatus() ? Color.GREEN : Color.RED);
			if (!logic.isStatus()) {
				throw new AssertionError(logic.getTitle());
			}
		} catch (Throwable t) {
			if (timeout <= 0) {
				Reporter.log("Assertion process failed due to " + t.getMessage());
				throw new AssertionError("Assertion process failed ");
			} else {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException interruptedException) {

				}
				assertLogicHappens(actual, logic, timeout - 3000);
			}
		}
	}

}
