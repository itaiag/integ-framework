package il.co.topq.integframework.support;


import org.openqa.selenium.support.ui.Clock;
import org.openqa.selenium.support.ui.Sleeper;

import com.google.common.base.Function;

public class FluentWait<T> extends org.openqa.selenium.support.ui.FluentWait<T> {

	private String lastFunctionTitle;

	public FluentWait(T input, Clock clock, Sleeper sleeper) {
		super(input, clock, sleeper);
	}
	
	public FluentWait(T input) {
		super(input);
	}
	
	@Override
	public <V> V until(Function<? super T, V> isTrue) {
		lastFunctionTitle = isTrue.toString();
		return super.until(isTrue);
	}
	/**
	 * return the title of the expected condition that was applied on the input.
	 */
	@Override
	public String toString() {
		return lastFunctionTitle;
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
	  @Override
	protected RuntimeException timeoutException(String message, Throwable lastException) {
	    throw new TimeoutException(message, lastException);
	  }
}
