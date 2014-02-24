package il.co.topq.integframework.assertion;

public class NumberCompareAssertion extends AbstractAssertionLogic<Integer> {

	private final int expected;

	private int actualInt;

	private final CompareMethod compareMethod;

	public NumberCompareAssertion(int expected, CompareMethod compareMethod) {
		super();
		this.expected = expected;
		this.compareMethod = compareMethod;
	}

	@Override
	public void doAssertion() {
		status = true;
		switch (compareMethod) {
		case BIGGER:
			if (actualInt > expected) {
				title = "Actual number " + actualInt + " is bigger then " + expected;
			} else {
				title = "Actual number " + actualInt + " is NOT bigger then " + expected;
				status = false;
			}
			break;
		case BIGGER_OR_EQUALS:
			if (actualInt >= expected) {
				title = "Actual number " + actualInt + " is bigger or equals to " + expected;
			} else {
				title = "Actual number " + actualInt + " is NOT bigger or equals to " + expected;
				status = false;
			}

			break;
		case EQUALS:
			if (actualInt == expected) {
				title = "Actual number " + actualInt + " is equals to " + expected;
			} else {
				title = "Actual number " + actualInt + " is NOT equals to " + expected;
				status = false;
			}
			break;

		case SMALLER_OR_EQUALS:
			if (actualInt <= expected) {
				title = "Actual number " + actualInt + " is smaller or equals to " + expected;
			} else {
				title = "Actual number " + actualInt + " is NOT smaller or equals to " + expected;
				status = false;
			}
			break;
		case SMALLER:
			if (actualInt < expected) {
				title = "Actual number " + actualInt + " is smaller then " + expected;
			} else {
				title = "Actual number " + actualInt + " is NOT smaller then " + expected;
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
	public void setActual(Integer actual) {
		if (actual != null) {
			actualInt =  actual;
		}
	}

}
