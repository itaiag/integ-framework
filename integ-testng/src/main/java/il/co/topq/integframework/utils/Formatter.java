package il.co.topq.integframework.utils;

/**
 * A formatter of T returns a string representation of the T object.
 * 
 * @author Aharon
 * 
 * @param <T>
 *            the type of object to be formatted
 */
public interface Formatter<T> {
	public String toString(T t);
}
