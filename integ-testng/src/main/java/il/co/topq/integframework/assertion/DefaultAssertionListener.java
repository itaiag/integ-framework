package il.co.topq.integframework.assertion;

public class DefaultAssertionListener<T> implements AssertionListener<T>{

	@Override
	public void assertionPassed(T actual, AbstractAssertionLogic<T> logic) {
		//default - do nothing
	}

	@Override
	public void assertionFailed(T actual, AbstractAssertionLogic<T> logic) {
		org.testng.Assert.fail(logic.message);
		
	}

	@Override
	public void assertionFailed(T actual, AbstractAssertionLogic<T> logic, Throwable t) {
		org.testng.Assert.fail(logic.message, t);
	}
};