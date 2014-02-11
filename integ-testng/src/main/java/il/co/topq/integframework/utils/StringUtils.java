package il.co.topq.integframework.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class StringUtils {
	@SuppressWarnings("unused")
	private final boolean __;

	private StringUtils() {
		__ = true;
	}

	public static boolean isEmpty(String str) {
		return (str == null) ? true : str.isEmpty();
	}

	public static String getStackTrace(Throwable t) {
		if (t != null) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			t.printStackTrace(printWriter);
			return stringWriter.toString();
		}
		return "";
	}

	public static String getPrefix(String s, String delim) {
		return s.substring(0, s.lastIndexOf(delim));
	}

	/**
	 * get the suffix of a string after a specified delimiter
	 * 
	 * @param s
	 *            the string to get the suffix from
	 * @param delim
	 *            the delimiter to find in it
	 * 
	 * @return the last part of the string after the delimiter.<br>
	 * @throws IndexOutOfBoundsException
	 *             when the delimiter does not exist in the string
	 */
	public static String getSuffix(String s, String delim) {
		return s.substring(s.lastIndexOf(delim)).substring(delim.length());
	}

	/**
	 * get the suffix of a string after a specified delimiter
	 * 
	 * @param s
	 *            the string to get the suffix from
	 * @param delim
	 *            the delimiter to find in it
	 * @param orAll
	 *            flag for returning entire string if the delimiter does not
	 *            exist in the string
	 * @return the last part of the string after the delimiter.<br>
	 *         If the delimiter does not exist in the string:
	 *         <ul>
	 *         <li>if orAll is true: return the entire string</li>
	 *         <li>if orAll is false: throws {@link IndexOutOfBoundsException}</li>
	 *         </ul>
	 */
	public static String getSuffix(String s, String delim, boolean orAll) {
		return orAll ? s.substring(1 + s.lastIndexOf(delim)) : getSuffix(s, delim);
	}

	/**
	 * get the suffix of the source string, after finding the longest substring
	 * of prefix.
	 * 
	 * @param src
	 *            the source to find the prefix in
	 * @param prefix
	 *            the prefix to find.
	 * @return the first suffix after the largest substring of prefix.
	 */
	public static String getFirstSubStringSuffix(String src, String prefix) {
		return getFirstSubStringSuffix(src, prefix, null);
	}

	/**
	 * get the suffix of the source string, after finding the longest substring
	 * of prefix.
	 * 
	 * @param src
	 *            the source to find the prefix in
	 * @param prefix
	 *            the prefix to find.
	 * @param prefixLeftovers
	 *            a buffer to put the rest of the prefix. ignored if set to null
	 * @return the first suffix after the largest substring of prefix.
	 */

	public static String getFirstSubStringSuffix(String src, String prefix, StringBuffer prefixLeftovers) {
		int srcIndex = src.indexOf(prefix);
		String subPrefix = prefix;
		while (srcIndex < 0 && subPrefix.length() > 0) {
			subPrefix = subPrefix.substring(0, subPrefix.length() - 1);
			srcIndex = src.indexOf(subPrefix);
		}
		if (prefixLeftovers != null && prefixLeftovers.length() == 0 && subPrefix.length() < prefix.length()) {
			prefixLeftovers.append(getSuffix(prefix, subPrefix));
		}
		return getSuffix(src, subPrefix);
	}
}
