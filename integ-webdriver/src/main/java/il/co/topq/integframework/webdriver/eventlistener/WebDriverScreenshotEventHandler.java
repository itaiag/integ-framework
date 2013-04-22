package il.co.topq.integframework.webdriver.eventlistener;

import static il.co.topq.integframework.webdriver.utils.WebDriverUtils.addScreenshot;
import static il.co.topq.integframework.reporting.Reporter.log;
import il.co.topq.integframework.reporting.Reporter;
import il.co.topq.integframework.reporting.Reporter.Color;
import il.co.topq.integframework.reporting.Reporter.Style;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class WebDriverScreenshotEventHandler implements WebDriverEventListener, ITestListener {

	@Override
	public void beforeNavigateTo(String url, WebDriver driver) {
		log("About to navigate to " + url, true);

	}

	@Override
	public void afterNavigateTo(String url, WebDriver driver) {
		// addScreenshot(driver);

	}

	@Override
	public void beforeNavigateBack(WebDriver driver) {

	}

	@Override
	public void afterNavigateBack(WebDriver driver) {

	}

	@Override
	public void beforeNavigateForward(WebDriver driver) {

	}

	@Override
	public void afterNavigateForward(WebDriver driver) {

	}

	@Override
	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		log("Searching element by locator '" + by.toString() + "'\n", true);

	}

	@Override
	public void afterFindBy(By by, WebElement element, WebDriver driver) {
		// addScreenshot(driver);

	}

	@Override
	public void beforeClickOn(WebElement element, WebDriver driver) {
		log("About to click on element with tag " + element.getTagName() + "\n", true);

	}

	@Override
	public void afterClickOn(WebElement element, WebDriver driver) {
	}

	@Override
	public void beforeChangeValueOf(WebElement element, WebDriver driver) {
		log("About to change value of element with tag " + element.getTagName() + "\n", true);

	}

	@Override
	public void afterChangeValueOf(WebElement element, WebDriver driver) {

	}

	@Override
	public void beforeScript(String script, WebDriver driver) {

	}

	@Override
	public void afterScript(String script, WebDriver driver) {

	}

	@Override
	public void onException(Throwable throwable, WebDriver driver) {
		Reporter.log("Exception: " + throwable.getMessage(), Style.BOLD, Color.RED);
		addScreenshot(driver);
	}

	@Override
	public void onTestStart(ITestResult result) {
		log("----------- Starting Test: " + result.getName() + "-----------", Style.BOLD, Color.BLUE);

	}

	@Override
	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub

	}

}