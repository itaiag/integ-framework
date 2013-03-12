package il.co.topq.integframework.webdriver.windows;

import il.co.topq.integframework.webdriver.utils.WebDriverUtils;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

public class ByWindowTitle extends By {

	private static final long TIMEOUT_IN_MILLI = 30000;
	private final By by;
	private final String windowTitle;

	public ByWindowTitle(By by, String windowTitle) {
		this.by = by;
		this.windowTitle = windowTitle;
	}

	@Override
	public List<WebElement> findElements(SearchContext context) {
		if (context instanceof WebDriver) {
			final WebDriver driver = (WebDriver) context;
			WebDriverUtils.switchToWindow(driver, windowTitle, TIMEOUT_IN_MILLI);
			return by.findElements(context);
		}
		throw new WebDriverException(context.toString() + " Is not a WebDriver");
	}

	

}
