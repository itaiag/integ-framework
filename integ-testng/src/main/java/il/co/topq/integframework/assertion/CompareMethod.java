package il.co.topq.integframework.assertion;

public enum CompareMethod {
	BIGGER, BIGGER_OR_EQUALS, EQUALS, SMALLER_OR_EQUALS, SMALLER;
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
}