package il.co.topq.integframework.assertion;

import il.co.topq.integframework.reporting.Reporter;

public class DefaultAssertionListener<T> implements AssertionListener<T>{

	@Override
	public void assertionPassed(T actual, AbstractAssertionLogic<T> logic) {
		//default - do nothing
	}

	@Override
	public void assertionFailed(T actual, AbstractAssertionLogic<T> logic) {
		Reporter.log("Assertion failed: " + logic.getTitle(), logic.getMessage(), false);
		org.testng.Assert.fail(logic.getTitle());
		
	}

	@Override
	public void assertionFailed(T actual, AbstractAssertionLogic<T> logic, Throwable t) {
		Reporter.log("Assertion process failed: ", t);
		org.testng.Assert.fail(logic.getTitle(), t);
	}
};