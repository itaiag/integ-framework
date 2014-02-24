package il.co.topq.integframework.cli.support;

import il.co.topq.integframework.assertion.AbstractAssertionLogic;
import il.co.topq.integframework.assertion.Assert;
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
					execution.execute();
				} catch (Exception e) {
					Assert.fail(toString(), e);
				}
				Assert.assertLogic(execution.getResult(), logic);
				return execution.getResult();
			}

			@Override
			public String toString() {
				return logic.getTitle();
			}
		};

	}

	public static CliExecutionExpectedCondition<String> executionResponseReturn(final String mustHaveResponse) {
		return new CliExecutionExpectedCondition<String>() {
			@Override
			public String apply(CliCommandExecution execution) {
				try {
					execution.mustHaveResponse(mustHaveResponse).execute();
				} catch (Exception e) {
					Assert.fail(toString(), e);
				}
				return execution.getResult();
			}

			@Override
			public String toString() {
				return "execution must have response of " + mustHaveResponse;
			}
		};

	}


}
