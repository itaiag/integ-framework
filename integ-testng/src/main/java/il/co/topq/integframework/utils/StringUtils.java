package il.co.topq.integframework.utils;

public abstract class StringUtils {
	private final boolean __;
	private StringUtils(){
		__=true;
	}
	public static boolean isEmpty(String str) {
		return (str==null)?true:str.isEmpty();
	}
}
