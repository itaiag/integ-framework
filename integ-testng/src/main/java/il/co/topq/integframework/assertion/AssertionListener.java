package il.co.topq.integframework.assertion;

/**
 * 
 * Listen to assertion pseodo-events:
 * <ol>
 * <li>pass</li>
 * <li>fail</li>
 * <li>exception thrown</li>
 * </ol>
 * 
 * @author Aharon Hacmon
 * 
 * @param <T>
 *            the type of actual. you may get it's value
 */
public interface AssertionListener<T> {
	public void assertionPassed(final T actual, final AbstractAssertionLogic<T> logic);

	public void assertionFailed(final T actual, final AbstractAssertionLogic<T> logic);

	public void assertionFailed(final T actual, final AbstractAssertionLogic<T> logic, Throwable t);
}