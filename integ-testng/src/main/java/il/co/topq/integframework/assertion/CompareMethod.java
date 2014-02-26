package il.co.topq.integframework.assertion;

public enum CompareMethod {
	BIGGER {
		@Override
		public <T extends Comparable<T>> boolean compare(T o1, T o2) {
			return o1.compareTo(o2) > 0;
		}
	},
	BIGGER_OR_EQUALS {
		@Override
		public <T extends Comparable<T>> boolean compare(T o1, T o2) {
			return o1.compareTo(o2) >= 0;
		}
	},
	EQUALS {
		@Override
		public <T extends Comparable<T>> boolean compare(T o1, T o2) {
			return o1.compareTo(o2) == 0;
		}
	},
	SMALLER_OR_EQUALS {
		@Override
		public <T extends Comparable<T>> boolean compare(T o1, T o2) {
			return o1.compareTo(o2) <= 0;
		}
	},
	SMALLER {
		@Override
		public <T extends Comparable<T>> boolean compare(T o1, T o2) {
			return o1.compareTo(o2) < 0;
		}
	};
	public String toString() {
		switch (this) {
		case BIGGER:
			return "bigger then";
		case BIGGER_OR_EQUALS:
			return "bigger then or equals to";
		case EQUALS:
			return "equals to";
		case SMALLER_OR_EQUALS:
			return "smaller then or equals to";
		case SMALLER:
			return "smaller then";
		default:
			return null;
		}

	};

	public abstract <T extends Comparable<T>> boolean compare(T o1, T o2);
}