package il.co.topq.integframework.webdriver.eventlistener;

import java.io.IOException;


import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.testng.Reporter;

/**
 * This class if for Event Handler by the Dispatcher of the
 * WebDriverEventListener Class. the
 * 
 * 
 * copy to SUT: com.jsystem.webdriver.eventlistener.WebDriverReportEventHandler
 * 
 * @author Liel Ran ,Create Date - 22.12.11
 * 
 */
public class WebDriverReportEventHandler implements WebDriverEventListener {

	private final String prefix = "[";
	private final String suffix = "]";

	public WebDriverReportEventHandler() {
		Reporter.log("Init the WebDriverReportEventHandler");
	}

	@Override
	public void afterChangeValueOf(WebElement arg0, WebDriver arg1) {
		Reporter.log("After Change Value");
	}

	@Override
	public void afterClickOn(WebElement arg0, WebDriver arg1) {
		Reporter.log("After Click");
	}

	@Override
	public void afterFindBy(By arg0, WebElement arg1, WebDriver arg2) {
		report("After FindBy", "FindBy= " + arg0);
	}

	@Override
	public void afterNavigateBack(WebDriver arg0) {
		report("After Navigate", "Navigate back to= " + arg0.getCurrentUrl());
	}

	@Override
	public void afterNavigateForward(WebDriver arg0) {
		report("After Navigate", "Navigate forward to= " + arg0.getCurrentUrl());
	}

	@Override
	public void afterNavigateTo(String arg0, WebDriver arg1) {
		report("After Navigation", "Page loaded= " + arg0);
	}

	@Override
	public void afterScript(String arg0, WebDriver arg1) {
		Reporter.log("after Script");
	}

	/**
	 * Called before {@link WebElement#clear WebElement.clear()} and
	 * {@link WebElement#sendKeys(CharSequence...)}
	 */
	@Override
	public void beforeChangeValueOf(WebElement arg0, WebDriver arg1) {
		report("Before Change Value Of");
	}

	@Override
	public void beforeClickOn(WebElement arg0, WebDriver arg1) {
		report("Before Click On");
	}

	@Override
	public void beforeFindBy(By arg0, WebElement arg1, WebDriver arg2) {
		report("Before FindBy", "FindBy= " + arg0);
	}

	@Override
	public void beforeNavigateBack(WebDriver arg0) {
		report("Before Navigate Back", "Navigate back from= " + arg0.getCurrentUrl());
	}

	@Override
	public void beforeNavigateForward(WebDriver arg0) {
		report("Before Navigate Forward", "Navigate forward from= " + arg0.getCurrentUrl());
	}

	@Override
	public void beforeNavigateTo(String arg0, WebDriver arg1) {
		report("Before Navigation", "Navigate from= " + arg1.getCurrentUrl() + ", Navigate to= " + arg0);
	}

	@Override
	public void beforeScript(String arg0, WebDriver arg1) {
		Reporter.log("Before script execution");
	}

	@Override
	public void onException(Throwable arg0, WebDriver arg1) {
		synchronized (arg0) {
			if (arg0 instanceof NoSuchElementException) {
				Reporter.log("element was not found");
			}
			else {
				report("Exception", arg0.getMessage());
				Reporter.log(arg0.getMessage());
			}
		}
	}

	private void report(String title) {
		Reporter.log(prefix + title + suffix);
	}

	private void report(String title, String text) {
		Reporter.log(prefix + title + suffix + ": " + text);
	}

}
