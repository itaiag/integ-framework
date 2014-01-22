package il.co.topq.integframework.support;


import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.Sleeper;

public class FluentWait<T> extends org.openqa.selenium.support.ui.FluentWait<T> {

	public FluentWait(T input, Clock clock, Sleeper sleeper) {
		super(input, clock, sleeper);
	}
	
	public FluentWait(T input) {
		super(input);
	}
	  /**
	   * Throws a timeout exception. This method may be overridden to throw an exception that is
	   * idiomatic for a particular test infrastructure, such as an AssertionError in JUnit4.
	   *
	   * @param message The timeout message.
	   * @param lastException The last exception to be thrown and subsequently suppressed while waiting
	   *        on a function.
	   * @return Nothing will ever be returned; this return type is only specified as a convenience.
	   */
	  protected RuntimeException timeoutException(String message, Throwable lastException) {
	    throw new TimeoutException(message, lastException);
	  }
}
