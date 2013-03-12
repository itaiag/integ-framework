package il.co.topq.integframework.webdriver.eventlistener;
//package il.co.topq.integframework.webdriver.eventlistener;
//
//import java.io.File;
//
//import jsystem.framework.report.ListenerstManager;
//import jsystem.framework.report.Reporter;
//import jsystem.utils.FileUtils;
//import jsystem.utils.StringUtils;
//import junit.framework.AssertionFailedError;
//import junit.framework.Test;
//import junit.framework.TestListener;
//
//import il.co.topq.integframework.webdriver.WebDriverContainer;
//import org.openqa.selenium.By;
//import org.openqa.selenium.OutputType;
//import org.openqa.selenium.TakesScreenshot;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.events.WebDriverEventListener;
//
///**
// * This class if for Event Handler by the Dispatcher of the
// * WebDriverEventListener Class.
// * 
// * copy to SUT :
// * il.co.topq.integframework.webdriver.eventlistener.WebDriverScreenshotEventHandler
// * 
// * @author Liel Ran ,Create Date - 22.12.11
// */
//public class WebDriverScreenshotEventHandler implements WebDriverEventListener, TestListener, WebDriverContainer {
//
//	private WebDriver webDriver;
//	private String screenshotFolderName = "Screenshots";
//	private final String prefix = "Screenshot";
//	private String path;
//	protected boolean loaded = false;
//	protected String screenShotPath = "log" + File.separator + "current";
//	protected String screenShotFolderName = "Screenshots";
//
//	public WebDriverScreenshotEventHandler() {
//		//We would like to get also test events.
//		//ListenerstManager.getInstance().addListener(this); moved to WebDriverWrapper#register()
//		init();
//	}
//
//	private void init() {
//		try {
//			// default is one folder of screenshot in the log folder. but can be
//			// config from the SUT file
//			String fileDir = screenShotPath + File.separator + screenShotFolderName;// Create
//			// dir
//			File dir = new File(fileDir);
//
//			if (dir.exists()) {
//				loaded = true;
//			}
//			else if ((dir).mkdir()) {
//				reporter.report("Create Directory: " + fileDir + " created");
//				loaded = true;
//			}
//			else if (!(dir.exists())) {
//				reporter.report("Failed to create Directory: " + fileDir, Reporter.WARNING);
//				loaded = false;
//			}
//			path = fileDir;
//		} catch (Exception e) {
//			reporter.report("Failed to set screenshot working folder, set screenshot to false", Reporter.WARNING);
//			loaded = false;
//		}
//	}
//
//	public void afterChangeValueOf(WebElement arg0, WebDriver arg1) {
//		takeScreenshot(arg1, "After ChangeValueOf Screenshot");
//	}
//
//	public void afterClickOn(WebElement arg0, WebDriver arg1) {
//		takeScreenshot(arg1, "After ClickOn Screenshot");
//	}
//
//	public void afterFindBy(By arg0, WebElement arg1, WebDriver arg2) {
//	}
//
//	public void afterNavigateBack(WebDriver arg0) {
//		takeScreenshot(arg0, "After NavigateBack Screenshot");
//	}
//
//	public void afterNavigateForward(WebDriver arg0) {
//		takeScreenshot(arg0, "After NavigateForward Screenshot");
//	}
//
//	public void afterNavigateTo(String arg0, WebDriver arg1) {
//		takeScreenshot(arg1, "After NavigateTo Screenshot");
//	}
//
//	public void afterScript(String arg0, WebDriver arg1) {
//		takeScreenshot(arg1, "After Script execution Screenshot");
//	}
//
//	/**
//	 * Called before {@link WebElement#clear()} and
//	 * {@link WebElement#sendKeys(CharSequence...)}
//	 */
//	public void beforeChangeValueOf(WebElement arg0, WebDriver arg1) {
//		takeScreenshot(arg1, "Before ChangeValueOf Screenshot");
//	}
//
//	public void beforeClickOn(WebElement arg0, WebDriver arg1) {
//		try {
//			File scrFile = ((TakesScreenshot) arg1).getScreenshotAs(OutputType.FILE);
//			scrFile = copyFileToScreenshotFolder(scrFile);
//			addToReport(scrFile, "Before ClickOn Screenshot");
//		} catch (Exception e) {
//
//		}
//	}
//
//	public void beforeFindBy(By arg0, WebElement arg1, WebDriver arg2) {
//	}
//
//	public void beforeNavigateBack(WebDriver arg0) {
//		takeScreenshot(arg0, "Before NavigateBack Screenshot");
//	}
//
//	public void beforeNavigateForward(WebDriver arg0) {
//		takeScreenshot(arg0, "Before NavigateForward Screenshot");
//	}
//
//	public void beforeNavigateTo(String arg0, WebDriver arg1) {
//		takeScreenshot(arg1, "Before NavigateTo Screenshot");
//	}
//
//	public void beforeScript(String arg0, WebDriver arg1) {
//	}
//
//	public void onException(Throwable arg0, WebDriver arg1) {
//	}
//
//	private File copyFileToScreenshotFolder(File file) {
//		File dest = null;
//		if (file != null) {
//			try {
//				dest = new File(path + File.separator + prefix + System.currentTimeMillis() + ".png");
//				FileUtils.copyFile(file, dest);
//				file.delete();
//			} catch (Exception e) {
//			}
//		}
//
//		return dest;
//	}
//
//	/**
//	 * this function will take the screenshot
//	 * 
//	 * @param driver
//	 *            - the active webdriver (Htmlunit will not work)
//	 * @param title
//	 *            - the title in the HTML report file.
//	 * @return true in case of success else return false;
//	 */
//	public boolean takeScreenshot(WebDriver driver, String title) {
//
//		if (driver == null) {
//			driver = getDriver();
//		}
//		synchronized (driver) {
//			boolean screenshot = false;
//			try {
//				if (loaded) {
//					File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//					scrFile = copyFileToScreenshotFolder(scrFile);
//					addToReport(scrFile, title);
//					screenshot = true;
//				}
//			} catch (Throwable e) {
//				reporter.report("Error taking Screenshot", e.getMessage() + StringUtils.getStackTrace(e), Reporter.PASS);
//			}
//			return screenshot;
//		}
//	}
//
//	private void addToReport(File file, String title) {
//
//		String fileName = file.getName();
//		String linkPath = ".." + File.separator + screenshotFolderName + File.separator + fileName;
//		reporter.addLink(title + " (fileName=" + fileName + ").", linkPath);
//
//	}
//
//	public void addError(Test test, Throwable t) {
//		if (!(takeScreenshot(getDriver(), "Error"))) {
//			reporter.report("Error", t);
//		}
//	}
//
//	public void addFailure(Test test, AssertionFailedError t) {
//		if (!takeScreenshot(getDriver(), "Failure")) {
//			reporter.report("Failure", t);
//		}
//	}
//
//	public void endTest(Test test) {
//
//	}
//
//	public void startTest(Test test) {
//
//	}
//
//	public WebDriver getDriver() {
//		return webDriver;
//	}
//
//	public void setDriver(WebDriver driver) {
//		this.webDriver = driver;
//	}
//}
