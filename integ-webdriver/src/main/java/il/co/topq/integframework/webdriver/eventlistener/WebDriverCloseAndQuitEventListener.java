package il.co.topq.integframework.webdriver.eventlistener;

import org.openqa.selenium.support.events.WebDriverEventListener;

public interface WebDriverCloseAndQuitEventListener extends WebDriverEventListener {
	/**
	 * Called before {@link org.openqa.selenium.WebDriver#quit()}.
	 */
	public void beforeQuit();
	
	/**
	 * Called after {@link org.openqa.selenium.WebDriver#quit()}.
	 */
	public void afterQuit();

	/**
	 * Called before {@link org.openqa.selenium.WebDriver#close()}.
	 */

	public void beforeClose();

	/**
	 * Called after {@link org.openqa.selenium.WebDriver#close()}.<br>
	 */
	public void afterClose();
}
