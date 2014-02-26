package il.co.topq.integframework.cli.support;

import java.text.ParseException;

public interface Parser<T> {
	T parse(String source) throws ParseException;
}
