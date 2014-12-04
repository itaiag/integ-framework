package il.co.topq.integframework;

import com.google.common.base.Function;

public enum NamingFunctions implements Function<Named, String> {
	getName {
		@Override
		public String apply(Named input) {
			return input.getName();
		}
	};
}
