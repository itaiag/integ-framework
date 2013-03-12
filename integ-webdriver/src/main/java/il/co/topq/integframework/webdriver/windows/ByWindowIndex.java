package il.co.topq.integframework.webdriver.windows;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

public class ByWindowIndex extends By {

	private static final long TIMEOUT_IN_MILLI = 30000;
	private final By by;
	private final int windowIndex;

	public ByWindowIndex(By by, int windowIndex) {
		this.by = by;
		this.windowIndex = windowIndex;
	}

	@Override
	public List<WebElement> findElements(SearchContext context) {
		if (context instanceof WebDriver) {
			final WebDriver driver = (WebDriver) context;
			String[] windowHandles = driver.getWindowHandles().toArray(new String[] {});
			long start = System.currentTimeMillis();

			// Waiting for windows to open
			while (windowIndex >= windowHandles.length && (System.currentTimeMillis() - start) < TIMEOUT_IN_MILLI) {
				try {
					Thread.sleep(500);
					windowHandles = driver.getWindowHandles().toArray(new String[] {});
				} catch (InterruptedException e) {
				}
			}
			Reporter.log("Found " + windowHandles.length + " windows\n", true);
			if (windowIndex < windowHandles.length) {
				Reporter.log("Switching to window with index " + windowIndex, true);
				driver.switchTo().window(windowHandles[windowIndex]);
			} else {
				Reporter.log("Window index " + windowIndex + " is not exists", true);
			}
			Reporter.log("Switched to window with title '" + driver.getTitle() + "'\n", true);
			return by.findElements(context);
		}
		throw new WebDriverException(context.toString() + " Is not a WebDriver");
	}

}
