package il.co.topq.integframework.utils;

import java.text.ParseException;

public class Parsers {

	private final Object __;

	private Parsers() {
		__ = null;
		if (__ == null)
			;
	}

	public static final Parser<Long> longParser = new Parser<Long>() {
		@Override
		public Long parse(String source) throws ParseException {
			return Long.parseLong(source);
		}
	};

	public static final Parser<Integer> intParser = new Parser<Integer>() {
		@Override
		public Integer parse(String source) throws ParseException {
			return Integer.parseInt(source);
		}
	};


}
