package il.co.topq.integframework.assertion;

/**
 * Abstract class which intend to inherit by assertion logic classes.
 * 
 * @author Itai Agmon
 * 
 */
public abstract class AbstractAssertionLogic<T> implements IAssertionLogic<T> {

	/**
	 * The actual object to perform assertion on
	 */
	protected T actual;

	/**
	 * The status of the assertion
	 */
	protected boolean status;

	/**
	 * Title that will be printed in the report
	 */
	protected String title;

	/**
	 * Additionl information to report on the assertion process
	 */
	protected String message;

	

	/**
	 * Sets the actual object to perfrom assertion on
	 */
	@Override
	public void setActual(T actual) {
		this.actual = actual;
	}

	public String getTitle() {
		return title;
	}

	public String getMessage() {
		return message;
	}

	public boolean isStatus() {
		return status;
	}

}
