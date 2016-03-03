package il.co.topq.integframework.assertion;

import com.google.common.base.Predicate;

public class PredicateAssertionLogic<T> extends AbstractAssertionLogic<T> {

	private final Predicate<T> isTrue;

	public PredicateAssertionLogic(Predicate<T> isTrue) {
		this.isTrue = isTrue;
	}

	@Override
	public void doAssertion() {
		message = isTrue.toString();
		status = isTrue.apply(actual);
	}

}
