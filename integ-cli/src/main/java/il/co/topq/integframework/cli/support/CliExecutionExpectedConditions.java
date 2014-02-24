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

}
