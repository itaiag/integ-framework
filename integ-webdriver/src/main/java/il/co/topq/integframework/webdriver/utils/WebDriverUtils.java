package il.co.topq.integframework.webdriver.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

public class WebDriverUtils {

	private static boolean switchToWindowWithTitle(WebDriver driver, String[] windowHandles, String windowTitle) {
		for (String windowHandle : windowHandles) {
			try {
				driver.switchTo().window(windowHandle);
			} catch (NoSuchWindowException e) {
				Reporter.log("Ooops. Recieved 'NoSuchWindowException' while searching for window with title "
						+ windowTitle + ". Recovering", true);
				// Recovery in case we are in window that is not exist
				continue;
			}
			if (driver.getTitle().contains(windowTitle.trim())) {
				return true;
			}
		}
		return false;

	}
	
	public static void rightClickOnElement(WebDriver driver,WebElement webElement){
		Actions actions = new Actions(driver);
		Action action = actions.contextClick(webElement).build();
		action.perform();

	}

	public static void switchToWindow(WebDriver driver, String windowTitle) {
		switchToWindow(driver, windowTitle, 30000);
	}

	public static WebElement waitForElement(WebDriver driver, By by) {
		return waitForElement(driver, by, 30);
	}

	public static WebElement waitForElement(WebDriver driver, By by, int timeout) {
		WebElement dynamicElement = (new WebDriverWait(driver, timeout)).until(ExpectedConditions
				.presenceOfElementLocated(by));
		return dynamicElement;
	}

	public static void switchToWindow(WebDriver driver, String windowTitle, long timeoutInMillis) {
		if (driver.getTitle().contains(windowTitle.trim())) {
			return;
		}
		try {
			String[] windowHandles = driver.getWindowHandles().toArray(new String[] {});
			long start = System.currentTimeMillis();
			while ((System.currentTimeMillis() - start) < timeoutInMillis) {
				if (switchToWindowWithTitle(driver, windowHandles, windowTitle)) {
					Reporter.log("Switched to window with title '" + windowTitle + "'\n", true);
					return;
				}
				try {
					Reporter.log("Window with title '" + windowTitle + "' was not found. Waiting \n", true);
					Thread.sleep(500);
					windowHandles = driver.getWindowHandles().toArray(new String[] {});
				} catch (InterruptedException e) {
				}
			}

		} catch (NoSuchWindowException e) {
			Reporter.log("Recieved 'NoSuchWindowException' while trying to switch to '" + windowTitle + "' !!!!", true);
			throw new IllegalStateException("Failed to switch to window with title '" + windowTitle + "'");
		}
		throw new IllegalStateException("Failed to switch to window with title '" + windowTitle + "'");

	}

}