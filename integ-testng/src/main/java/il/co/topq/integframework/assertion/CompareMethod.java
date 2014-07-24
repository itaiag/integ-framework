package il.co.topq.integframework.assertion;

import com.google.common.base.Predicate;

public enum CompareMethod {

	BIGGER {
		@Override
		public <T extends Comparable<T>> boolean compare(T o1, T o2) {
			return o1.compareTo(o2) > 0;
		}
	},
	AFTER {
		@Override
		public <T extends Comparable<T>> boolean compare(T o1, T o2) {
			return BIGGER.compare(o1, o2);
		}
	},
	BIGGER_OR_EQUALS {
		@Override
		public <T extends Comparable<T>> boolean compare(T o1, T o2) {
			return o1.compareTo(o2) >= 0;
		}
	},
	AFTER_OR_WHEN {
		@Override
		public <T extends Comparable<T>> boolean compare(T o1, T o2) {
			return BIGGER_OR_EQUALS.compare(o1, o2);
		}
	},
	EQUALS {
		@Override
		public <T extends Comparable<T>> boolean compare(T o1, T o2) {
			return o1.compareTo(o2) == 0;
		}
	},
	WHEN {
		@Override
		public <T extends Comparable<T>> boolean compare(T o1, T o2) {
			return EQUALS.compare(o1, o2);
		}

	},
	SMALLER_OR_EQUALS {
		@Override
		public <T extends Comparable<T>> boolean compare(T o1, T o2) {
			return o1.compareTo(o2) <= 0;
		}
	},
	BEFORE_OR_WHEN {
		@Override
		public <T extends Comparable<T>> boolean compare(T o1, T o2) {
			return SMALLER_OR_EQUALS.compare(o1, o2);
		}
	},
	SMALLER {
		@Override
		public <T extends Comparable<T>> boolean compare(T o1, T o2) {
			return o1.compareTo(o2) < 0;
		}
	},
	BEFORE {
		@Override
		public <T extends Comparable<T>> boolean compare(T o1, T o2) {
			return SMALLER.compare(o1, o2);
		}

	};
	public String toString() {
		switch (this) {
		case BIGGER:
			return "bigger then";
		case AFTER:
			return "after";
		case BIGGER_OR_EQUALS:
			return "bigger then or equals to";
		case AFTER_OR_WHEN:
			return "after or when";
		case EQUALS:
			return "equals to";
		case WHEN:
			return "when";
		case SMALLER_OR_EQUALS:
			return "smaller then or equals to";
		case BEFORE_OR_WHEN:
			return "before or when";
		case SMALLER:
			return "smaller then";
		case BEFORE:
			return "before";
		default:
			return null;
		}
	};

	public abstract <T extends Comparable<T>> boolean compare(T o1, T o2);

	public final <T extends Comparable<T>> Predicate<T> to(final T that) {
		return than(that);
	}

	public final <T extends Comparable<T>> Predicate<T> than(final T that) {
		final CompareMethod me = this;
		return new Predicate<T>() {

			@Override
			public boolean apply(T t) {
				return me.compare(t, that);
			}
		};
	}

	public static final <T extends Comparable<T>> boolean is(T one, CompareMethod compare, T another) {
		return compare.compare(one, another);
	}
}