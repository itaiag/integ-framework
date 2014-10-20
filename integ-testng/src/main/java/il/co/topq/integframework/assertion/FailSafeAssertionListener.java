/**
 * 
 */
package il.co.topq.integframework.assertion;

import il.co.topq.integframework.utils.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Aharon hacmon
 *
 */
public class FailSafeAssertionListener<T> implements AssertionListener<T> {

	/* (non-Javadoc)
	 * @see il.co.topq.integframework.assertion.AssertionListener#assertionPassed(java.lang.Object, il.co.topq.integframework.assertion.AbstractAssertionLogic)
	 */
	@Override
	public void assertionPassed(T actual, AbstractAssertionLogic<T> logic) {
		// TODO Auto-generated method stub

	}

	
	protected final List<Throwable> suppressed = new ArrayList<Throwable>();
	/* (non-Javadoc)
	 * @see il.co.topq.integframework.assertion.AssertionListener#assertionFailed(java.lang.Object, il.co.topq.integframework.assertion.AbstractAssertionLogic)
	 */
	@Override
	public void assertionFailed(T actual, AbstractAssertionLogic<T> logic) {
		suppressed.add(new AssertionError(StringUtils.either(logic.message).or(logic.getTitle())));
	}

	/* (non-Javadoc)
	 * @see il.co.topq.integframework.assertion.AssertionListener#assertionFailed(java.lang.Object, il.co.topq.integframework.assertion.AbstractAssertionLogic, java.lang.Throwable)
	 */
	@Override
	public void assertionFailed(T actual, AbstractAssertionLogic<T> logic,
			Throwable t) {
		suppressed.add(new AssertionError(StringUtils.either(logic.message).or(logic.getTitle()), t));

	}
	
	public Iterator<Throwable> getSuppressedThrowables(){
		return new ArrayList<Throwable>(suppressed).iterator();
	}

	public Throwable getLastSuppressedThrowable() {
		if (suppressed.size() > 0) {
			return suppressed.get(suppressed.size() - 1);
		}
		return null;
	}

	public Throwable getRootSuppressedThrowable() {
		Throwable t;
		for (t = getLastSuppressedThrowable(); t != null && t.getCause() != null
				&& (t.getCause() != t || !(t instanceof AssertionError)); t = t
				.getCause())
			;/* DoNothing */
		return t;
	}
}
