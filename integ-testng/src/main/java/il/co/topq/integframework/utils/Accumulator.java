package il.co.topq.integframework.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Collect items and their values (can be counter)
 * 
 * @author Aharon Hacmon
 * 
 * @param <T>
 *            the type of items to collect values on using a {@link HashMap} to
 *            match two items
 */
public class Accumulator<T> {
	protected Map<T, Long> acc;

	/**
	 * increment the counter of t by one
	 * 
	 * @param t
	 */
	public void increment(T t) {
		increment(t, 1);
	}

	/**
	 * increment the counter of t by the given value
	 * 
	 * @param val
	 * 
	 */
	public void increment(T t, long val) {
		lazyInit();
		Long old;
		if ((old = acc.put(t, val)) != null) {
			acc.put(t, old + val);
		}
	}

	public Long get(T t) {
		lazyInit();
		return acc.get(t);
	}

	public void reset(T t) {
		lazyInit();
		acc.remove(t);
	}
	public void clear() {
		acc = null;
	}

	private void lazyInit() {
		if (acc == null) {
			acc = new HashMap<T, Long>();
		}
	}

	public Set<T> keySet() {
		return acc.keySet();
	}
}
