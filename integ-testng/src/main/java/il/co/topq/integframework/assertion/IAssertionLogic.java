package il.co.topq.integframework.assertion;

/**
 * Intefrace for creating assertion logic
 * 
 * @author Itai Agmon
 * 
 */
public interface IAssertionLogic {

	/**
	 * Perform assertion
	 */
	void doAssertion();

	/**
	 * 
	 * @return The class on which the logic can be perform on
	 */
	Class<?> getActualClass();

	/**
	 * Set the actual object to perfrom logic on
	 * 
	 * @param actual
	 */
	void setActual(Object actual);

}
