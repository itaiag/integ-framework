package il.co.topq.integframework.utils;

import java.text.ParseException;

/**
 * A parser of T parses a string representation of a T object and returns it.
 * 
 * @author Aharon
 * 
 * @param <T>
 *            the type of object to be parsed
 */
public interface Parser<T> {
	T parse(String source) throws ParseException;
}
