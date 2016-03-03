
package il.co.topq.integframework.webdriver;

import il.co.topq.integframework.webdriver.annotations.Annotations;
import il.co.topq.integframework.webdriver.annotations.ElementInFrameList;
import il.co.topq.integframework.webdriver.annotations.ElementMustExist;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

/**
 * In this abstract page object, the WebElement proxy will poll the interface on
 * a regular basis until the element is present, and also will make sure that
 * all elements with the @{@link ElementMustExist} annotation. If the element
 * exists in a Frame (or an IFrame), please provide value in
 * {@link ElementInFrameList#value()}, which is the frame-path of the element
 * 
 * @author Aharon
 * @since 1.0.7
 * 
 */
public abstract class AbstractAjaxPageObjectImpl extends AbstractAjaxPageObject {

	public static class WebDriverExpectedCondition {
		private WebDriverExpectedCondition() {
		}

		public static Predicate<WebDriver> asPredicate(final ExpectedCondition<Boolean> expectedCondition) {
			return new Predicate<WebDriver>() {
				@Override
				public boolean apply(WebDriver driver) {
					return expectedCondition.apply(driver);
				}
			};
		}
	}

	public AbstractAjaxPageObjectImpl(WebDriver driver) {
		super(driver);
	}

	public AbstractAjaxPageObjectImpl(WebDriver driver, int ajaxTimeout) {
		super(driver, ajaxTimeout);
	}

	private static WebDriverWait wait;

	/**
	 * Repeatedly applies the given predicate on this page's driver until the
	 * timeout expires or the predicate evaluates to true.<br>
	 * The timeout defined in {@link #setAjaxTimeout(int)}
	 * 
	 * @param untilPredicate
	 *            The predicate to wait on.
	 */
	public void waitUntil(Predicate<WebDriver> untilPredicate) {
		waitUntil(untilPredicate, getAjaxTimeout());
	}

	/**
	 * Repeatedly applies the given predicate on this page's driver until the
	 * timeout expires or the predicate evaluates to true.
	 * 
	 * @param untilPredicate
	 *            The predicate to wait on.
	 * @param timeout
	 *            timeout in seconds
	 */
	public void waitUntil(Predicate<WebDriver> untilPredicate, int timeout) {
		wait = new WebDriverWait(driver, getAjaxTimeout());
		wait.pollingEvery(pollingMillis, TimeUnit.MILLISECONDS);
		wait.until(untilPredicate);
	}

	/**
	 * Repeatedly invoking {@link ExpectedCondition#apply(Object)} on
	 * this page's WebDriver until one of the following
	 * occurs:
	 * <ol>
	 * <li>the function returns neither null nor false</li>
	 * <li>the timeout (set in {@link #setAjaxTimeout(int)}) expires</li>
	 * <li>the current thread is interrupted</li>
	 * </ol>
	 * 
	 * @param untilExpectedCondition
	 *            The condition to apply
	 * @param <T>
	 *            The ExpectedCondition's return type.
	 * @return The value returned by the {@link ExpectedCondition}.
	 */
	public <T> T waitUntil(ExpectedCondition<T> untilExpectedCondition) {
		return waitUntil(untilExpectedCondition, getAjaxTimeout());
	}

	/**
	 * Repeatedly invoking {@link ExpectedCondition#apply(Object)} on
	 * this page's WebDriver until one of the following
	 * occurs:
	 * <ol>
	 * <li>the function returns neither null nor false</li>
	 * <li>the timeout expires</li>
	 * <li>the current thread is interrupted</li>
	 * </ol>
	 * 
	 * @param untilExpectedCondition
	 *            The condition to apply
	 * @param timeout
	 *            timeout in seconds
	 * @param <T>
	 *            The ExpectedCondition's return type.
	 * @return The value returned by the {@link ExpectedCondition}.
	 */
	public <T> T waitUntil(ExpectedCondition<T> untilExpectedCondition, int timeout) {
		wait = new WebDriverWait(driver, timeout);
		wait.pollingEvery(pollingMillis, TimeUnit.MILLISECONDS);
		T result = wait.until(untilExpectedCondition);
		return result;
	}

	/**
	 * Wait as long as the predicate returns true, or timeout (set in
	 * {@link #setAjaxTimeout(int)}) expires
	 * 
	 * @param whilePredicate
	 *            the predicate to wait for
	 */
	public void waitWhile(Predicate<WebDriver> whilePredicate) {
		waitWhile(whilePredicate, getAjaxTimeout());
	}

	public void waitWhile(Predicate<WebDriver> whilePredicate, int timeout) {
		wait = new WebDriverWait(driver, timeout);
		wait.pollingEvery(pollingMillis, TimeUnit.MILLISECONDS);
		wait.until(Predicates.not(whilePredicate));
	}

	/**
	 * wait as long as the condition returns true.
	 * 
	 * @param whileExpectedCondition
	 *            the condition. Some of the conditions might have negate
	 *            version of themselves, which is preferred to be used with
	 *            {@link #waitUntil(ExpectedCondition)}
	 * @return The value returned by the {@link ExpectedCondition}
	 */
	public boolean waitWhile(final ExpectedCondition<Boolean> whileExpectedCondition) {
		return waitWhile(whileExpectedCondition, getAjaxTimeout());
	}

	public boolean waitWhile(final ExpectedCondition<Boolean> whileExpectedCondition, int timeout) {
		wait = new WebDriverWait(driver, timeout);
		wait.pollingEvery(pollingMillis, TimeUnit.MILLISECONDS);
		Boolean result = wait.until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply( WebDriver input) {
				return !whileExpectedCondition.apply(input);
			}
		});
		return !result;
	}

	/**
	 * Sets how often the condition should be evaluated in a waitUntil or
	 * waitWhile method - to the default value of 500 milliseconds.
	 */
	public void resetPollingMillis() {
		pollingMillis = 500;
	}

	/**
	 * Sets how often the condition should be evaluated in a waitUntil or
	 * waitWhile method.
	 * 
	 * <p>
	 * In reality, the interval may be greater as the cost of actually
	 * evaluating a condition function is not factored in. The default polling
	 * interval is 500 milliseconds.
	 * 
	 * @param pollingMillis
	 *            The timeout duration in milliseconds.
	 */
	public void setPollingMillis(int pollingMillis) {
		this.pollingMillis = pollingMillis;
	}

	private int pollingMillis = 500;

	/**
	 * Asserts that all {@link ElementMustExist}s fields are visible in this
	 * page object.
	 */
	@Override
	protected void assertInModule() {
		driver.manage().timeouts().implicitlyWait(pollingMillis, TimeUnit.MILLISECONDS);
		for (final Field currField: this.getClass().getDeclaredFields()) {
			currField.setAccessible(true);
			if (currField.isAnnotationPresent(ElementMustExist.class)) {
				By by = new Annotations(currField).buildBy();
				Reporter.log("Waiting for Element (@ElementMustExist): " + by);
				final WebDriverWait wait = new WebDriverWait(driver, getAjaxTimeout());
				wait.ignoring(WebDriverException.class);
				wait.pollingEvery(100, TimeUnit.MILLISECONDS);
				wait.until(ExpectedConditions.visibilityOfElementLocated(by));

			}
		}
	}

}
