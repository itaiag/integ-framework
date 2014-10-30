package il.co.topq.integframework.utils;

import java.util.Comparator;

public class FormattingComparatorDecorator<T> extends FormatterImpl<T> implements FormattingComparator<T> {
	private final Comparator<T> comparator;

	public FormattingComparatorDecorator(Comparator<T> comparator) {
		this.comparator = comparator;
	}

	@Override
	public int compare(T o1, T o2) {
		// delegate
		return this.comparator.compare(o1, o2);
	}
}
