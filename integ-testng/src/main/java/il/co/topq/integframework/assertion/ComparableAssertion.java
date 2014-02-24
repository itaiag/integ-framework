package il.co.topq.integframework.assertion;

public class ComparableAssertion<T extends Comparable<T>> extends AbstractAssertionLogic<T> {

	private final T expected;

	private T actualObject;

	private final CompareMethod compareMethod;

	public ComparableAssertion(T expected, CompareMethod compareMethod) {
		super();
		this.expected = expected;
		this.compareMethod = compareMethod;
	}

	@Override
	public void doAssertion() {
		status = true;
		switch (compareMethod) {
		case BIGGER:
			if (actualObject.compareTo(expected) > 0) {
				title = "Actual [" + actualObject + "] is bigger then " + expected;
			} else {
				title = "Actual [" + actualObject + "] is NOT bigger then " + expected;
				status = false;
			}
			break;
		case BIGGER_OR_EQUALS:
			if (actualObject.compareTo(expected) >= 0) {
				title = "Actual [" + actualObject + "] is bigger or equals to " + expected;
			} else {
				title = "Actual [" + actualObject + "] is NOT bigger or equals to " + expected;
				status = false;
			}

			break;
		case EQUALS:
			if (actualObject.compareTo(expected) == 0) {
				title = "Actual [" + actualObject + "] is equals to " + expected;
			} else {
				title = "Actual [" + actualObject + "] is NOT equals to " + expected;
				status = false;
			}
			break;

		case SMALLER_OR_EQUALS:
			if (actualObject.compareTo(expected) <= 0) {
				title = "Actual [" + actualObject + "] is smaller or equals to " + expected;
			} else {
				title = "Actual [" + actualObject + "] is NOT smaller or equals to " + expected;
				status = false;
			}
			break;
		case SMALLER:
			if (actualObject.compareTo(expected) < 0) {
				title = "Actual [" + actualObject + "] is smaller then " + expected;
			} else {
				title = "Actual [" + actualObject + "] is NOT smaller then " + expected;
				status = false;
			}
			break;

		default:
			break;
		}

	}

	
	/**
	 * Sets the actual object to perfrom assertion on
	 */
	@Override
	public void setActual(T actual) {
		if (actual != null) {
			actualObject = actual;
		}
	}

}
