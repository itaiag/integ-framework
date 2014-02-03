package il.co.topq.integframework.assertion;

public interface AssertionListener<T> {
	public void assertionPassed(final T actual, final AbstractAssertionLogic<T> logic);
	public void assertionFailed(final T actual, final AbstractAssertionLogic<T> logic);
	public void assertionFailed(final T actual, final AbstractAssertionLogic<T> logic, Throwable t);
}