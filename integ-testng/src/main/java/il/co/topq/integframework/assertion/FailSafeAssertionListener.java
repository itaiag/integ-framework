/**
 * 
 */
package il.co.topq.integframework.assertion;

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
		suppressed.add(new AssertionError(logic.message));
	}

	/* (non-Javadoc)
	 * @see il.co.topq.integframework.assertion.AssertionListener#assertionFailed(java.lang.Object, il.co.topq.integframework.assertion.AbstractAssertionLogic, java.lang.Throwable)
	 */
	@Override
	public void assertionFailed(T actual, AbstractAssertionLogic<T> logic,
			Throwable t) {
		suppressed.add(new AssertionError(logic.message, t));

	}
	
	public Iterator<Throwable> getSuppressedThrowables(){
		return new ArrayList<Throwable>(suppressed).iterator();
	}

}
