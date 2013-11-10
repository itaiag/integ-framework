package il.co.topq.integframework.assertion;

import il.co.topq.integframework.reporting.Reporter;
import il.co.topq.integframework.reporting.Reporter.Color;
import il.co.topq.integframework.reporting.Reporter.Style;

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
	static public <T> void assertLogic(final T actual, final AbstractAssertionLogic<T> logic, final long timeout) throws Exception {
		assertLogicHappens(actual, logic, timeout, true);
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
	static public <T> void assertLogic(final T actual, final AbstractAssertionLogic<T> logic) throws Exception {
		assertLogic(actual, logic, 0l);
	}

	static public <T> void assertLogicHappens(final T actual, final AbstractAssertionLogic<T> logic,
			final long timeout, boolean silent) throws Exception {
		if (null == actual) {
			throw new IllegalArgumentException("Actual can't be null");
		}
		if (null == logic) {
			throw new IllegalArgumentException("logic can't be null");
		}

		logic.setActual(actual);
		try {
			logic.doAssertion();
			if (logic.isStatus()) {
				if (!silent) {
					Reporter.log("Assertion success ", Style.REGULAR, Color.GREEN);
				}
			} else {
				Reporter.log("Assertion failure: " + logic.getTitle(), logic.getMessage(), Color.RED);

			}
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
				assertLogicHappens(actual, logic, timeout - 3000, silent);
			}

		}
	}
}
