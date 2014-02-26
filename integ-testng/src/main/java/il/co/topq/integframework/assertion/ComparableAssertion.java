package il.co.topq.integframework.assertion;

public class ComparableAssertion<T extends Comparable<T>> extends AbstractAssertionLogic<T> {

	private final T expected;

	private final CompareMethod compareMethod;

	public ComparableAssertion(T expected, CompareMethod compareMethod) {
		super();
		this.expected = expected;
		this.compareMethod = compareMethod;
	}

	public ComparableAssertion(CompareMethod compareMethod, T expected) {
		this(expected, compareMethod);
	}
	@Override
	public void doAssertion() {
		status = compareMethod.compare(actual, expected);
		StringBuilder titleBuilder = new StringBuilder();
		titleBuilder.append("Actual [").append(actual).append("] is ");
		if (!status) {
			titleBuilder.append("NOT ");
		}
		titleBuilder.append(compareMethod.toString()).append(" [").append(expected).append("]");
	}

}
