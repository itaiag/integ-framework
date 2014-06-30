package il.co.topq.integframework.assertion;

import com.google.common.base.Predicate;

public class AllItemsAre<T> extends AbstractAssertionLogic<Iterable<T>> {

	private final Predicate<T> isTrue;

	public AllItemsAre(Predicate<T> isTrue) {
		this.isTrue = isTrue;
	}

	@Override
	public void doAssertion() {
		boolean all = true;
		for (T actualItem : actual) {
			all = all && isTrue.apply(actualItem);
		}
		status = all;
	}

}
