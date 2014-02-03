package il.co.topq.integframework.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class StringUtils {
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
}
