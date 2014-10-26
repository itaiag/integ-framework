package il.co.topq.integframework.assertion;

import il.co.topq.integframework.reporting.Reporter;
import il.co.topq.integframework.reporting.Reporter.Color;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.common.base.Function;

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
			throw new IllegalArgumentException("expected can't be null");
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
	 * @deprecated actual value is set only once, loop has no meaning! <br>
	 *             use
	 *             {@link #assertLogic(Object, Function, AbstractAssertionLogic, long, TimeUnit, long, TimeUnit)}
	 *             instead
	 */
	@Deprecated
	static public <T> void assertLogic(final T actual, final AbstractAssertionLogic<T> logic, final long timeout)
			throws TimeoutException {
		long timeUp = System.currentTimeMillis() + timeout;
		do {
			assertLogic(actual, logic, null);
			if (!logic.status) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					throw new TimeoutException("Assertion interrupted");
				}
			}
		} while (timeUp < System.currentTimeMillis() && !logic.status);
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
	static public <T> void assertLogic(final T actual, final AbstractAssertionLogic<T> logic) {
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
				// Reporter.log("Assertion failed: " + logic.getTitle(),
				// logic.getMessage(), false);
				listener.assertionFailed(actual, logic);
			}
		} catch (Throwable t) {
			// Reporter.log("Assertion process failed: ", t);
			listener.assertionFailed(actual, logic, t);
		}
	}

	/**
	 * Execute the <code>logic</code> and repeating on the the
	 * <code>actual</code> object, as gained from the actualValueGenerator<br>
	 * stop when either one of the following occurs:
	 * <ol>
	 * <li>the actual object is null</li>
	 * <li>the thread was interrupted while sleeping</li>
	 * <li>timeout expires</li>
	 * </ol>
	 * 
	 * @param <A>
	 *            the type of actual to examine
	 * @param <R>
	 *            the type of the resource to gain the actual value from
	 * @param resource
	 *            a {@link il.co.topq.integframework.Module} or another kind of
	 *            external {@link Object}, from which the actual value is
	 *            gained.
	 * @param actualValueGenerator
	 *            a function from the resource above to the actual value- on
	 *            which to perform assertion on
	 * @param logic
	 *            Logic to operate on the actual object
	 * @param timeout
	 *            maximum time for this operation
	 * @param timeoutUnit
	 *            time unit for the parameter above
	 * @param interval
	 *            time to sleep between assertions
	 * @param intervalUnit
	 *            time unit for the parameter above
	 * @throws AssertionError
	 *             If assertion fails
	 * @return true if assertion finished due to timeout i.e. the assertion
	 *         passed on all invocations of
	 *         {@link AbstractAssertionLogic#doAssertion()}
	 */
	static public <A, R> void assertLogic(R resource, final Function<R, A> actualValueGenerator, AbstractAssertionLogic<A> logic,
			long timeout, TimeUnit timeoutUnit, long interval, TimeUnit intervalUnit) {
		assertLogic(resource, actualValueGenerator, logic, new DefaultAssertionListener<A>(), timeout, timeoutUnit, interval,
				intervalUnit);
	}

	/**
	 * Execute the <code>logic</code> and repeating on the the
	 * <code>actual</code> object, as gained from the actualValueGenerator<br>
	 * stop when either one of the following occurs:
	 * <ol>
	 * <li>the actual object is null</li>
	 * <li>the thread was interrupted while sleeping</li>
	 * <li>timeout expires</li>
	 * </ol>
	 * 
	 * @param <A>
	 *            the type of actual to examine
	 * @param <R>
	 *            the type of the resource to gain the actual value from
	 * @param resource
	 *            a {@link il.co.topq.integframework.Module} or another kind of
	 *            external {@link Object}, from which the actual value is
	 *            gained.
	 * @param actualValueGenerator
	 *            a function from the resource above to the actual value- on
	 *            which to perform assertion on
	 * @param logic
	 *            Logic to operate on the actual object
	 * @param listener
	 *            an {@link AssertionListener} for ssertion events.
	 * @param timeout
	 *            maximum time for this operation
	 * @param timeoutUnit
	 *            time unit for the parameter above
	 * @param interval
	 *            time to sleep between assertions
	 * @param intervalUnit
	 *            time unit for the parameter above
	 * @throws AssertionError
	 *             If assertion fails
	 * @return true if assertion finished due to timeout i.e. the assertion
	 *         passed on all invocations of
	 *         {@link AbstractAssertionLogic#doAssertion()}
	 */
	static public <A, R> void assertLogic(R resource, final Function<R, A> actualValueGenerator, AbstractAssertionLogic<A> logic,
			AssertionListener<A> listener, long timeout, TimeUnit timeoutUnit, long interval, TimeUnit intervalUnit) {
		long end = System.currentTimeMillis() + timeoutUnit.toMillis(timeout);
		A actual;
		do {
			actual = actualValueGenerator.apply(resource);
			if (actual == null) {
				listener.assertionFailed(actual, logic, new NullPointerException(logic.getMessage()));
			}
			Assert.assertLogic(actual, logic, listener);
			try {
				Thread.sleep(intervalUnit.toMillis(interval));
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				listener.assertionFailed(actual, logic);
			}
		} while (end < System.currentTimeMillis());
		listener.assertionPassed(actual, logic);
	}

	/**
	 * @deprecated use
	 *             {@link Assert#assertLogic(Object, Function, AbstractAssertionLogic, long, TimeUnit, long, TimeUnit) 
	 */
	@Deprecated
	static public <T> void assertLogicHappens(final T actual, final AbstractAssertionLogic<T> logic, final long timeout,
			boolean silent) throws TimeoutException {

		assertLogic(actual, logic, timeout);
		if (!silent && logic.isStatus()) {
			Reporter.log("Assertion success: " + logic.getTitle(), Color.GREEN);
		}
	}
}
