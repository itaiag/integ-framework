package il.co.topq.integframework.assertion;

import il.co.topq.integframework.utils.Formatter;
import il.co.topq.integframework.utils.FormatterImpl;

public class ComparableAssertion<T extends Comparable<T>> extends AbstractAssertionLogic<T> {

	private final T expected;

	private String titleOfExaminedObject = "";
	private Formatter<T> formatter = new FormatterImpl<>();

	private final CompareMethod compareMethod;

	public ComparableAssertion(T expected, CompareMethod compareMethod) {
		super();
		this.expected = expected;
		this.compareMethod = compareMethod;
	}

	public ComparableAssertion(CompareMethod compareMethod, T expected) {
		this(expected, compareMethod);
	}

	public ComparableAssertion<T> examinedObjectTitled(String title) {
		this.titleOfExaminedObject = title;
		return this;

	}

	public ComparableAssertion<T> formatObjectWith(Formatter<T> formatter) {
		this.formatter = formatter;
		return this;
	}

	@Override
	public void doAssertion() {
		status = compareMethod.compare(actual, expected);
		StringBuilder titleBuilder = new StringBuilder();
		titleBuilder.append("Actual ").append(titleOfExaminedObject).append(" [").append(formatter.toString(actual))
				.append("] is ");
		if (!status) {
			titleBuilder.append("NOT ");
		}
		titleBuilder.append(compareMethod.toString()).append(" [").append(formatter.toString(expected)).append("]");
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(this.titleOfExaminedObject).append(" to be ");
		builder.append(compareMethod.toString()).append(" ").append(formatter.toString(expected));
		return builder.toString();
	}

}
