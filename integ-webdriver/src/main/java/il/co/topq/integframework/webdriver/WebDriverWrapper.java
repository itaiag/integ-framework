package il.co.topq.integframework.webdriver;

import il.co.topq.integframework.webdriver.eventlistener.WebDriverCloseAndQuitEventListener;
import il.co.topq.integframework.webdriver.eventlistener.WebDriverReportEventHandler;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestListener;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;

/**
 * 
 * @author Liel Ran ,Create Date - 22.12.11
 * 
 */
public class WebDriverWrapper extends EventFiringWebDriver implements HasWebDriver {

	private WebDriverEventListener webDriverReportEventHandler;
	private WebDriver webDriver;
	private List<WebDriverEventListener> webDriverEventListeners;

	public WebDriverWrapper(WebDriver driver, WebDriverEventListener... webDriverEventHandlers) {
		this(driver);
		webDriver = driver;
		if (webDriverEventHandlers != null) {
			for (WebDriverEventListener webDriverEventHandler : webDriverEventHandlers) {
				this.register(webDriverEventHandler);
			}
		}
	}

	public WebDriverWrapper(WebDriver driver) {
		super(driver);
		webDriver = driver;
		webDriverEventListeners = new ArrayList<WebDriverEventListener>();
	}

	/**
	 * will return all the Registered web driver event listeners
	 * 
	 * @return a list of the event handlers
	 */
	public List<WebDriverEventListener> getAllRegisteredWebDriverEventListeners() {
		return webDriverEventListeners;
	}

	@Override
	public EventFiringWebDriver register(WebDriverEventListener eventListener) {
		webDriverEventListeners.add(eventListener);
		if (eventListener instanceof WebDriverContainer) {
			((WebDriverContainer) eventListener).setDriver(webDriver);
		}
//		// We would like to get also test events.
//		if (eventListener instanceof TestListener) {
//			ListenerstManager.getInstance().addListener(eventListener);
//		}
		return super.register(eventListener);
	}

	@Override
	public EventFiringWebDriver unregister(WebDriverEventListener eventListener) {
		webDriverEventListeners.remove(eventListener);
//		if (eventListener instanceof TestListener) {
//			ListenerstManager.getInstance().removeListener(eventListener);
//		}
		return super.unregister(eventListener);
	}

	/**
	 * 
	 * @return this web driver
	 * @deprecated use {@link #getDriver()} instead
	 */
	@Deprecated
	public WebDriver getWebDriver() {
		return getDriver();
	}

	public WebDriver getDriver() {
		return this;
	}

	@Deprecated
	public void registerReportEventHandle() {
		webDriverReportEventHandler = new WebDriverReportEventHandler();
		super.register(webDriverReportEventHandler);
	}

	@Override
	public List<WebElement> findElements(By by) {
		synchronized (webDriver) {
			return super.findElements(by);
		}
	}

	@Override
	public WebElement findElement(By by) {
		synchronized (webDriver) {
			return super.findElement(by);
		}
	}

	@Override
	public <X> X getScreenshotAs(OutputType<X> target) {
		synchronized (webDriver) {
			return super.getScreenshotAs(target);
		}
	}

	@Override
	public Object executeScript(String script, Object... args) {
		synchronized (webDriver) {
			return super.executeScript(script, args);
		}
	}

	@Override
	public Object executeAsyncScript(String script, Object... args) {
		synchronized (webDriver) {
			return super.executeAsyncScript(script, args);
		}

	}

	@Override
	public void close() {
		// dispatch beforeClose()
		for (WebDriverEventListener eventListener : webDriverEventListeners) {
			if (eventListener instanceof WebDriverCloseAndQuitEventListener) {
				((WebDriverCloseAndQuitEventListener) eventListener).beforeClose();
			}
		}
		removeAllAddedEventListenersFromManager();
		try {
			super.close();
		} catch (UnreachableBrowserException e) {
			System.out.println("got UnreachableBrowserException while trying to close the browser - there is no way to handle this issue");
		}
		// dispatch afterClose()
		for (WebDriverEventListener eventListener : webDriverEventListeners) {
			if (eventListener instanceof WebDriverCloseAndQuitEventListener) {
				((WebDriverCloseAndQuitEventListener) eventListener).afterClose();
			}
		}
	}

	@Override
	public void quit() {
		// dispatch beforeQuit()
		for (WebDriverEventListener eventListener : webDriverEventListeners) {
			if (eventListener instanceof WebDriverCloseAndQuitEventListener) {
				((WebDriverCloseAndQuitEventListener) eventListener).beforeQuit();
			}
		}
		removeAllAddedEventListenersFromManager();
		super.quit();
		// dispatch afterQuit()
		for (WebDriverEventListener eventListener : webDriverEventListeners) {
			if (eventListener instanceof WebDriverCloseAndQuitEventListener) {
				((WebDriverCloseAndQuitEventListener) eventListener).afterQuit();
			}
		}
	}

	private void removeAllAddedEventListenersFromManager() {
		for (WebDriverEventListener eventListener : getAllRegisteredWebDriverEventListeners()) {
//			if (eventListener instanceof TestListener) {
//				ListenerstManager.getInstance().removeListener(eventListener);
//			}
		}
	}
}
