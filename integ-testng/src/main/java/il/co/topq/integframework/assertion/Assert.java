package il.co.topq.integframework.assertion;

import il.co.topq.integframework.reporting.Reporter;
import il.co.topq.integframework.reporting.Reporter.Color;

import java.util.concurrent.TimeoutException;

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
			fail(expected + " is not contained in " + actual);
		}

	}

	/**
	 * Execute the <code>logic</code> on the the <code>actual</code> object.
	 * 
	 * @param actual
	 *            Object to perform assertion on
	 * @param logic
	 *            Logic to operate on the actual object
	 * @param timeout
	 *            in milliseconds
	 * @throws TimeoutException
	 *             when time is up and the assertion is not successful
	 * @throws Exception
	 *             If exception occurced during assertion
	 * @throws AssertionError
	 *             If assertion fails
	 */
	static public <T> void assertLogic(final T actual, final AbstractAssertionLogic<T> logic, final long timeout) throws TimeoutException {
		long timeUp = System.currentTimeMillis() + timeout;
		do {
			assertLogic(actual, logic,null);
			if (!logic.status) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					throw new TimeoutException("Assertion interrupted");
				}
			}
		} while (timeUp < System.currentTimeMillis() || !logic.status);
		if (!logic.status) {
			throw new TimeoutException("Assertion failed");
		}
	}
	
	/**
	 * Execute the <code>logic</code> on the the <code>actual</code> object.
	 * 
	 * @param actual
	 *            Object to perform assertion on
	 * @param logic
	 *            Logic to operate on the actual object
	 * @throws AssertionError
	 *             If assertion fails
	 */
	static public <T> void assertLogic(final T actual,
			final AbstractAssertionLogic<T> logic) {
		assertLogic(actual, logic, null);
	}
	

	/**
	 * Execute the <code>logic</code> on the the <code>actual</code> object.
	 * 
	 * @param actual
	 *            Object to perform assertion on
	 * @param logic
	 *            Logic to operate on the actual object
	 * @param listener
	 *            an {@link AssertionListener} to fire the events of success,
	 *            failure and {@link Exception} thrown.
	 * @throws AssertionError
	 *             If assertion fails
	 */
	static public <T> void assertLogic(final T actual, final AbstractAssertionLogic<T> logic, AssertionListener<T> listener) {
		if (null == actual) {
			throw new IllegalArgumentException("Actual can't be null");
		}
		if (null == logic) {
			throw new IllegalArgumentException("logic can't be null");
		}
		if (null == listener) {
			listener = new DefaultAssertionListener<T>();
		}
		logic.setActual(actual);

		try {
			logic.doAssertion();
			if (logic.isStatus()) {
				listener.assertionPassed(actual, logic);
			} else {
				Reporter.log("Assertion failed: " + logic.getTitle(),
						logic.getMessage(), false);
				listener.assertionFailed(actual, logic);
			}
		} catch (Throwable t) {
			Reporter.log("Assertion process failed: ", t);
			listener.assertionFailed(actual, logic, t);
		}
	}

	static public <T> void assertLogicHappens(final T actual,
			final AbstractAssertionLogic<T> logic, final long timeout,
			boolean silent) throws TimeoutException {

		assertLogic(actual, logic, timeout);
		if (!silent && logic.isStatus()) {
			Reporter.log("Assertion success: " + logic.getTitle(), Color.GREEN);
		}
	}
}
