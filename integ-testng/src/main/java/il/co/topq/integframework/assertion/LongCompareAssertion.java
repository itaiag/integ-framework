package il.co.topq.integframework.assertion;

public class LongCompareAssertion extends AbstractAssertionLogic<Long> {


	private final long expected;

	private long actualLong;

	private final CompareMethod compareMethod;

	public LongCompareAssertion(long expected, CompareMethod compareMethod) {
		super();
		this.expected = expected;
		this.compareMethod = compareMethod;
	}

	@Override
	public void doAssertion() {
		status = true;
		switch (compareMethod) {
		case BIGGER:
			if (actualLong > expected) {
				title = "Actual number " + actualLong + " is bigger then " + expected;
			} else {
				title = "Actual number " + actualLong + " is NOT bigger then " + expected;
				status = false;
			}
			break;
		case BIGGER_OR_EQUALS:
			if (actualLong >= expected) {
				title = "Actual number " + actualLong + " is bigger or equals to " + expected;
			} else {
				title = "Actual number " + actualLong + " is NOT bigger or equals to " + expected;
				status = false;
			}

			break;
		case EQUALS:
			if (actualLong == expected) {
				title = "Actual number " + actualLong + " is equals to " + expected;
			} else {
				title = "Actual number " + actualLong + " is NOT equals to " + expected;
				status = false;
			}
			break;

		case SMALLER_OR_EQUALS:
			if (actualLong <= expected) {
				title = "Actual number " + actualLong + " is smaller or equals to " + expected;
			} else {
				title = "Actual number " + actualLong + " is NOT smaller or equals to " + expected;
				status = false;
			}
			break;
		case SMALLER:
			if (actualLong < expected) {
				title = "Actual number " + actualLong + " is smaller then " + expected;
			} else {
				title = "Actual number " + actualLong + " is NOT smaller then " + expected;
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
	public void setActual(Long actual) {
		if (actual != null) {
			actualLong = actual;
		}
	}

}
