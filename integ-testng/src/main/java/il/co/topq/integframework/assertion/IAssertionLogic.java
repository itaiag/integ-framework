package il.co.topq.integframework.assertion;

/**
 * Intefrace for creating assertion logic
 * 
 * @author Itai Agmon
 * 
 */
public interface IAssertionLogic<T> {

	/**
	 * Perform assertion
	 */
	void doAssertion();

	/**
	 * Set the actual object to perfrom logic on
	 * 
	 * @param actual
	 */
//	void setActual(Object actual);
	
	void setActual(T actual);

}
