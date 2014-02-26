package il.co.topq.integframework.cli.support;

import il.co.topq.integframework.assertion.AbstractAssertionLogic;
import il.co.topq.integframework.assertion.Assert;
import il.co.topq.integframework.assertion.FailSafeAssertionListener;
import il.co.topq.integframework.assertion.FindTextAssertion;
import il.co.topq.integframework.cli.process.CliCommandExecution;

public abstract class CliExecutionExpectedConditions {
	final Object __;

	private CliExecutionExpectedConditions() {
		__ = null; // util class, do not init!
	}

	public static CliExecutionExpectedCondition<String> executionLogicHappens(final AbstractAssertionLogic<String> logic) {
		return new CliExecutionExpectedCondition<String>() {
			@Override
			public String apply(CliCommandExecution execution) {
				try {
					FailSafeAssertionListener<String> failSafeListener = new FailSafeAssertionListener<String>();
					execution.execute();
					Assert.assertLogic(execution.getResult(), logic, failSafeListener);
					if (failSafeListener.getSuppressedThrowables().hasNext()) {
						Assert.fail("execution failed", failSafeListener.getSuppressedThrowables().next());
					}
					return execution.getResult();
				} catch (Exception e) {
					Assert.fail(toString(), e);
				}
				return null;
			}

			@Override
			public String toString() {
				return logic.getTitle();
			}
		};

	}

	public static CliExecutionExpectedCondition<String> executionResponseReturn(final String expectedResponse) {
		return executionLogicHappens(new FindTextAssertion(expectedResponse) {
			@Override
			public String toString() {
				return "execution response must contain " + expectedResponse;
			}
		});
		// final FindTextAssertion findTextAssertion = new
		// FindTextAssertion(mustHaveResponse);
		// final FailSafeAssertionListener<String> failSafeListener = new
		// FailSafeAssertionListener<String>();
		// return new CliExecutionExpectedCondition<String>() {
		// @Override
		// public String apply(CliCommandExecution execution) {
		// String result;
		// try {
		// execution.execute();
		// Assert.assertLogic(result = execution.getResult(), findTextAssertion,
		// failSafeListener);
		// return result;
		// } catch (Exception e) {
		// Assert.fail(toString(), e);
		// }
		// return null;
		// }
		//
		// @Override
		// public String toString() {
		// return "execution must have response with " + mustHaveResponse;
		// }
		// };

	}

	public static CliExecutionExpectedCondition<String> executionResponseReturnExactly(final String expectedResponse) {
		return executionLogicHappens(new AbstractAssertionLogic<String>() {

			@Override
			public void doAssertion() {
				this.status = this.actual.equals(expectedResponse);
			}

			@Override
			public String toString() {
				return "execution response must be " + expectedResponse;
			}
		});
	}

	/**
	 * this condition will return the return value of cli execution, after
	 * parsed by a parser and analyzed by the assertion logic
	 * 
	 * @param T
	 *            the type of the value the execution response. e.g. execution
	 *            of <code>wc -l <i>file</i></code> returns java.lang.Long
	 *            meaning the amount of lines in the file
	 * @param parser
	 *            an object that gets the string from the execution and
	 *            translates it to a T.<br>
	 *            a parser should be aware of all fields in the result.
	 * @param logic
	 *            the logic and sometimes the expected value to examine.
	 * @return the parsed value
	 */
	public static <T> CliExecutionExpectedCondition<T> executionLogicHappens(final AbstractAssertionLogic<T> logic,
			final Parser<T> parser) {
		return new CliExecutionExpectedCondition<T>() {

			@Override
			public T apply(CliCommandExecution execution) {
				try {
					FailSafeAssertionListener<T> failSafeListener = new FailSafeAssertionListener<T>();
					execution.execute();
					T actual = parser.parse(execution.getResult());
					Assert.assertLogic(actual, logic, failSafeListener);
					if (failSafeListener.getSuppressedThrowables().hasNext()) {
						Assert.fail("execution failed", failSafeListener.getSuppressedThrowables().next());
					}
					return actual;
				} catch (Exception e) {
					Assert.fail(toString(), e);
				}
				return null;
			}
		};

	}
}