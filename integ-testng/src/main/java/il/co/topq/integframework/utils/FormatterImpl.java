package il.co.topq.integframework.utils;

public class FormatterImpl<T> implements Formatter<T> {

	@Override
	public String toString(T t) {
		return t.toString();
	}

}
