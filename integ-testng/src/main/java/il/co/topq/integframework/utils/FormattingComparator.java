package il.co.topq.integframework.utils;

import java.util.Comparator;
@Deprecated
/**
 * A hybrid interface for Formatter and Comparator
 * @author Aharon
 *please do not use this class. it will be removed in few weeks.
 * @param <T>
 * the type to compare and to format
 */
public interface FormattingComparator<T> extends Formatter<T>, Comparator<T> {

}
