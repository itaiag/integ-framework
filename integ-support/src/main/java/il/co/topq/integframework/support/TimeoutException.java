package il.co.topq.integframework.support;

public class TimeoutException extends RuntimeException {
	  /**
	 * 
	 */
	private static final long serialVersionUID = -7491009459179861015L;

	public TimeoutException() {
	  }

	  public TimeoutException(String message) {
	    super(message);
	  }

	  public TimeoutException(Throwable cause) {
	    super(cause);
	  }

	  public TimeoutException(String message, Throwable cause) {
	    super(message, cause);
	  }
}
