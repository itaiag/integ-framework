package il.co.topq.integframework.webdriver;

import il.co.topq.integframework.webdriver.CurrentPageKeeper.AbstractPageObjectResolver;
import il.co.topq.integframework.webdriver.eventlistener.WebDriverReportEventHandler;
import il.co.topq.integframework.webdriver.eventlistener.WebDriverScreenshotEventHandler;
import il.co.topq.integframework.webdriver.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.android.AndroidDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.iphone.IPhoneDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.testng.Reporter;

/**
 * This class implements the Selenium system object. This class is responsible
 * for communication between the client and the remote control of the Selenium
 * platform. copy to SUT : com.jsystem.webdriver.WebDriverSystemObject
 * 
 * @author Liel Ran ,Create Date - 22.12.11
 */
public class WebDriverSystemModule implements HasWebDriver {

	// can be init from the SUT file.
	// the path should be full path with "chromedriver.exe" at the end of the
	// string
	protected String chromeDriverPath = null;

	/**
	 * the type of the browser to be open. e.g. IE/Chrome//FF
	 */
	protected WebDriverType webDriver;
	/**
	 * in case of true will not open the browser when init the system object.
	 */
	protected boolean lazyInit = false;

	protected WebDriverWrapper driver;
	protected final String CHROME_DRIVER_EXE_NAME = "chromedriver.exe";
	protected String browserPath;// browser path ,e.g.// c:/...../Firefox.exe
	protected String chromeFlags = null;
	protected boolean windowMaximize = true;
	protected String chromeProfile = null;
	protected String chromeExtension = null;
	protected String firefoxExtension = null;
	protected String firefoxProfile = null;
	protected String screenShotPath;
	protected String screenShotFolderName = "Screenshots";
	protected String domain = "";
	protected String seleniumTimeOut = "30000";
	protected boolean ignoreCertificateErrors = false;

	/**
	 * User and system web driver event handler classes Init from the SUT -
	 * classes that will be loaded by Reflection API (class loader) this String
	 * should contains the full class name of the class implementation if there
	 * is more than one class the string should be this ':' char as separator
	 * (like reporter in jsystem.propreties)
	 * 
	 * 
	 * @see WebDriverScreenshotEventHandler
	 * @see WebDriverReportEventHandler
	 * @see WebDriverEventListener
	 */
	protected String webDriverEventListenerClasses = "";

	private boolean browserIsOpened = false;

	private boolean clearCookiesBeforeOpen = false;

	public WebDriverSystemModule() throws Exception {

	}

	/**
	 * Open the browser:
	 * <ol>
	 * <li>creates Selenium client using the SUT parameters</li>
	 * <li>Sets up it's timeout</li>
	 * <li>Loads the page (set by {@link #setDomain(String)})</li>
	 * <li>Maximizes the browser window</li>
	 * <li>If the changeMousePosition set to true - moves the mouse to the
	 * bottom left position</li>
	 * 
	 * <ol>
	 * 
	 * @throws Exception
	 *             when the SUT doesn't configures the driver type
	 */
	public void openBrowser() {

		if (!browserIsOpened) {

			WebDriverWrapper driverWrapper = getBrowserInstance();
			browserIsOpened = true;
			initWebDriverEventListener(driverWrapper);

			if (seleniumTimeOut != null && webDriver != null) {
				setImplicitlyWait(driverWrapper, seleniumTimeOut);
			}

			driver = driverWrapper;

			if (isWindowMaximize()) {
				driver.manage().window().maximize();
			}
			if (isClearCookiesBeforeOpen()) {
				driver.manage().deleteAllCookies();
			}
			driver.get(getDomain());

		}
	}

	/**
	 * Restarts the browser:
	 * <ol>
	 * <li>
	 * Closes the browser window, but not removing the profile.</li>
	 * <li>Open the browser again</li>
	 * 
	 * @see #openBrowser
	 */
	public void restartBrowser() {
		closeBrowser();
		openBrowser();
	}

	public void clearCookies() {
		driver.manage().deleteAllCookies();
	}

	private void initWebDriverEventListener(WebDriverWrapper driverWrapper) {
		if (webDriverEventListenerClasses != null && !webDriverEventListenerClasses.isEmpty()) {
			String[] listeners = webDriverEventListenerClasses.split(";");

			ClassLoader loader = this.getClass().getClassLoader();

			for (String eventListenerClass : listeners) {
				try {

					if (eventListenerClass != null && !eventListenerClass.isEmpty()) {
						// Trim the class string
						eventListenerClass = eventListenerClass.trim();

						// load the class by the class loader
						Class<?> loadClass = loader.loadClass(eventListenerClass);

						// create new instance of the class
						WebDriverEventListener webDriverEventListener = (WebDriverEventListener) loadClass
								.newInstance();

						// register the class as a web driver event handler.
						driverWrapper.register(webDriverEventListener);
					}

				} catch (Throwable e) {
					Reporter.log(eventListenerClass + " cannot be loaded");
				}
			}
		}

	}

	/**
	 * Creates Selenium client using SUT parameters
	 * 
	 * @return SeleniumClient
	 * @throws Exception
	 */
	protected WebDriverWrapper getBrowserInstance() {
		WebDriverWrapper webDriverInstance = null;

		if (getWebDriver() != null) {
			webDriverInstance = webDriverFactory(getWebDriver());
		} else {

			String error = "Browser instance init Exception, the webDriver type(e.g. CHROME_DRIVER) must not be null";
			error += "\n" + WebDriverType.getAllSupportedWebDriverTypesAsString();

			throw new RuntimeException(error);
		}
		return webDriverInstance;
	}

	/**
	 * will return the Registered Web Driver Event Listeners as a list
	 * 
	 * @return list of all the Registered Web Driver Event Listeners
	 */
	public List<WebDriverEventListener> getAllRegisteredWebDriverEventListeners() {
		List<WebDriverEventListener> registeredWebDriverEventListeners;
		try {
			registeredWebDriverEventListeners = driver.getAllRegisteredWebDriverEventListeners();
		} catch (Exception e) {
			registeredWebDriverEventListeners = new ArrayList<WebDriverEventListener>();
		}

		return registeredWebDriverEventListeners;
	}

	protected WebDriverWrapper webDriverFactory(WebDriverType type) {

		WebDriverWrapper driver = null;
		WebDriver webDriver = null;

		try {

			if (type == null) {
				throw new IllegalArgumentException(
						"the input browser type is null(change the value of the 'webDriver' Type in the SUT");
			}

			switch (type) {
			case CHROME_DRIVER:
				webDriver = getChromeDriver();
				break;

			case FIREFOX_DRIVER:
				webDriver = getFirefoxWebDriver();
				break;

			case INTERNET_EXPLORER_DRIVER:
				webDriver = getInternetExplorerWebDriver();
				break;

			case HTML_UNIT_DRIVER:
				webDriver = getHtmlUnitDriver();
				break;

			case OPERA_DRIVER:
				webDriver = getOperaDriver();
				break;

			case ANDROID_DRIVER:
				webDriver = getAndroidDriver();
				break;

			case IPHONE_DRIVER:
				webDriver = getIphoneDriver();
				break;

			case SAFARI_DRIVER:
				webDriver = getSafariDriver();
				break;
			case PHANTOMJS_DRIVER:
				webDriver = getPhantomJsDriver();
				break;

			default:
				throw new IllegalArgumentException("WebDriver type is unsupported");

			}
		} catch (IllegalArgumentException e) {
			Reporter.log(e.getMessage() + WebDriverType.getAllSupportedWebDriverTypesAsString(), false);
			e.printStackTrace();
			throw e;
		}

		if (webDriver == null) {
			Reporter.log("Failed to init the web driver", false);
			throw new IllegalArgumentException("Failed to init the web driver-" + type);
		} else {
			driver = new WebDriverWrapper(webDriver);
		}
		return driver;
	}

	private WebDriver getPhantomJsDriver() {
		if (StringUtils.isEmpty(browserPath)) {
			throw new IllegalArgumentException("Path to PhantomJS was not defined");
		}

		DesiredCapabilities capabilites = new DesiredCapabilities();
		capabilites.setJavascriptEnabled(true);
		capabilites.setCapability("takesScreenshot", true);

		PhantomJSDriverService service = new PhantomJSDriverService.Builder()
				.usingCommandLineArguments(new String[] { "--ignore-ssl-errors=yes" })
				.usingPhantomJSExecutable(new File(browserPath)).build();

		return new PhantomJSDriver(service, capabilites);
	}

	private WebDriver getSafariDriver() {
		WebDriver driver = null;
		try {
			driver = new SafariDriver();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return driver;
	}

	protected WebDriver getIphoneDriver() {
		WebDriver webDriver = null;
		try {
			webDriver = new IPhoneDriver();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return webDriver;
	}

	protected WebDriver getAndroidDriver() {
		return new AndroidDriver();
	}

	protected WebDriver getOperaDriver() {
		// return new OperaDriver();
		return null;// Opeara is not supported
	}

	protected WebDriver getHtmlUnitDriver() {
		return new HtmlUnitDriver();
	}

	private void copyResource(String sourcePath, File destination) {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(sourcePath);
		try {
			FileUtils.saveInputStreamToFile(is, destination);
		} catch (Exception e) {
			Reporter.log("Failed reasource" + sourcePath + " to File " + destination);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				Reporter.log("failed closing input stream for " + sourcePath);
			}
		}
	}

	protected WebDriver getInternetExplorerWebDriver() {
		WebDriver webDriver = null;
		try {

			File IEDriverServerExe = File.createTempFile("IEDriverServer", ".exe");
			copyResource("IEDriverServer.exe", IEDriverServerExe);
			System.setProperty("webdriver.ie.driver", IEDriverServerExe.getAbsolutePath());
			IEDriverServerExe.deleteOnExit();

			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
			ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			if (ignoreCertificateErrors) {
				ieCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			}
			webDriver = new InternetExplorerDriver(ieCapabilities);
		} catch (Exception e) {
			webDriver = new InternetExplorerDriver();
		}
		return webDriver;
	}

	private FirefoxProfile ffProfile = null;
	private FirefoxBinary ffBinary = null;

	protected WebDriver getFirefoxWebDriver() {
		WebDriver webDriver = null;
		try {
			if (ffProfile == null) {
				ffProfile = getFirefoxConfigurationProfile();
			}

			if (browserPath == null || browserPath.isEmpty()) {
				webDriver = new FirefoxDriver(ffProfile);
			} else {
				if (ffBinary == null) {
					System.setProperty("webdriver.firefox.bin", browserPath);
					// Build the Default path of Firefox driver in runtime
					Reporter.log("set Firefox bin Property :" + browserPath);
					File pathToFirefoxBinary = new File(browserPath);
					ffBinary = new FirefoxBinary(pathToFirefoxBinary);
					Reporter.log("the Property was set to :" + System.getProperty("webdriver.firefox.bin"));
				}
				webDriver = new FirefoxDriver(ffBinary, ffProfile);
			}
		} catch (Exception e) {
			Reporter.log("failed to set browser path =" + browserPath + ". " + e.getMessage());
		}
		return webDriver;
	}

	protected WebDriver getChromeDriver() {

		WebDriver driver = null;
		String chromeDriverPath;
		ChromeOptions options = new ChromeOptions();

		try {
			chromeDriverPath = getChromeDriverExePath();

			Reporter.log("the Chrome Driver path is =" + chromeDriverPath);
			System.setProperty("webdriver.chrome.driver", chromeDriverPath);

			ArrayList<String> switches = new ArrayList<String>();
			if (chromeProfile != null) {
				Reporter.log("open webDriver chrome with the profile(" + chromeProfile + ").");
				switches.add("--user-data-dir=" + chromeProfile);
			}

			if (chromeExtension != null) {
				Reporter.log("open webDriver chrome with the chromeExtension(" + chromeExtension + ").");
				switches.add("--load-extension=" + chromeExtension);
			}

			if (windowMaximize == true) {
				Reporter.log("open webDriver chrome with the flag of Maximized.");
				switches.add("--start-maximized");
			}

			if (ignoreCertificateErrors == true) {
				Reporter.log("open webDriver chrome with the flag of ignoreCertificateErrors.");
				switches.add("--ignore-certificate-errors");
			}

			if (switches.size() > 0) {
				options.addArguments(switches);
				driver = new ChromeDriver(options);
			} else {
				driver = new ChromeDriver();
			}
		} catch (Throwable e) {

			Reporter.log("Error in init the chorme driver");
			e.printStackTrace();
		}
		return driver;
	}

	/**
	 * Build the Default path of Chrome driver in runtime Note: the
	 * 'chromedriver.exe' must be under lib folder on the local project(the
	 * tests/src project).
	 * 
	 * @throws URISyntaxException
	 */
	private String getChromeDriverExePath() throws IOException, URISyntaxException {
		String path = "";

		try {
			if (chromeDriverPath == null) {

				// final File testSrcFolder = new
				// File(JSystemProperties.getInstance().getPreference(
				// FrameworkOptions.TESTS_SOURCE_FOLDER));
				// final File libFolder = new File(testSrcFolder.getParent()
				// +File.separator+".."+File.separator+".."+File.separator,
				// "lib");
				//

				URL location = WebDriverSystemModule.class.getProtectionDomain().getCodeSource().getLocation();

				String file = location.getFile();

				String decode = URLDecoder.decode(file);

				File classesDir = new File(decode);
				File target = new File(classesDir.getParent());
				File rootDir = new File(target.getParent());
				File chromeExe = new File(rootDir.getAbsoluteFile() + File.separator + "src" + File.separator + "main"
						+ File.separator + "resources" + File.separator + CHROME_DRIVER_EXE_NAME);

				if (chromeExe.exists()) {
					Reporter.log("found the chrome driver exe(default folder -" + chromeExe + ").", true);
					path = chromeExe.getAbsolutePath();
				} else {
					Reporter.log("didn't find the chromedriver.exe under the default folder -" + chromeExe, true);
				}

				// if (!libFolder.exists()) {
				// throw new IOException("Failed to find lib folder " +
				// libFolder.getAbsolutePath()
				// + ", chrome driver init failed");
				// }
				// path = libFolder.getAbsolutePath() + File.separator +
				// CHROME_DRIVER_EXE_NAME;
			} else {
				path = chromeDriverPath;
			}

		} catch (Exception e) {
			Reporter.log("Failed to get the chrome driver path" + e.getMessage());
		}

		return path;
	}

	private void setImplicitlyWait(WebDriverWrapper webDriverWrapper, String seleniumTimeOut) {
		try {
			long miliSecs = Long.valueOf(seleniumTimeOut);
			Reporter.log("Set WebDriver implicitlyWait seleniumTimeOut to " + seleniumTimeOut + "(Milliseconds)");
			webDriverWrapper.getDriver().manage().timeouts().implicitlyWait(miliSecs, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			Reporter.log("unable to set WebDriver implicitlyWait seleniumTimeOut,please check the SUT seleniumTimeOut parameter(Milliseconds  as String)");
		}

	}

	protected FirefoxProfile getFirefoxConfigurationProfile() {
		File extention = null;
		FirefoxProfile profile = null;

		if (firefoxProfile != null) {
			Reporter.log("open webDriver firefox with the profile(" + firefoxProfile + ").");
			File profileLib = new File(firefoxProfile);
			if (profileLib != null && !profileLib.exists() || profileLib == null) {
				throw new IllegalArgumentException("Failed to find firefox profile. is the path : " + firefoxProfile
						+ " contains the profile?");
			}
			profile = new FirefoxProfile(profileLib);
		}

		if (firefoxExtension != null) {
			Reporter.log("open webDriver firefox with Extension(" + firefoxExtension + ").");
			if (null == profile) {
				profile = new FirefoxProfile();
			}
			extention = new File(firefoxExtension);
			if (!extention.exists()) {
				throw new IllegalArgumentException("Failed to find firefox Extension. is the path : "
						+ firefoxExtension + " contains the .xpi extantion?");
			}
			try {

				profile.addExtension(extention);
			} catch (IOException e) {
				Reporter.log("faild to add Extension to the firefox profile", false);
				throw new IllegalArgumentException("Failed to add firefox c to profile. is the path : "
						+ firefoxExtension + " contains the firefoxExtension?");
			}
		}
		if (null == profile) {
			profile = new FirefoxProfile();
		}
		if (this.ignoreCertificateErrors) {
			Reporter.log("set accept Untrusted Certificates");
			profile.setAcceptUntrustedCertificates(ignoreCertificateErrors);
		}

		try {
			if (profile != null) {
				profile.setEnableNativeEvents(true);
			}
		} catch (Exception e) {
			Reporter.log("faild to enable native event on the firefox profile");
		}
		return profile;
	}

	/**
	 * Closes WebDriver Selenium client driver instance<br/>
	 * This function will also delete the temp profile.
	 * 
	 * @throws Exception
	 *             when {@code WebDriver.quit()} throws an exception
	 * @see org.openqa.selenium.WebDriver#quit()
	 */
	public void closeBrowserInstance() {

		Reporter.log("Closing selenium client");
		if (driver != null) {
			driver.quit();
			browserIsOpened = false;
		}

	}

	/**
	 * Closes the browser window, but not removing the profile.
	 * 
	 * @see org.openqa.selenium.WebDriver#close()
	 */
	public void closeBrowser() {
		if (browserIsOpened) {
			driver.close();
			pageKeeper = null;
		}
		browserIsOpened = false;
	}

	public void close() {
		Reporter.log("Closing WebDriver System Object");
		try {
			closeBrowserInstance();
		} catch (Exception e) {
			Reporter.log("error in closing WebDriver System Object,Error=" + e.getMessage());
		} finally {
			pageKeeper = null;
		}
	}

	/*
	 * feature added by Aharon @ 01/08/2012 - PageKeeper
	 */
	private CurrentPageKeeper pageKeeper = new CurrentPageKeeper();

	/**
	 * @return the pageKeeper
	 */
	public CurrentPageKeeper getPageKeeper() {
		if (pageKeeper == null) {
			pageKeeper = new CurrentPageKeeper();
		}

		if (pageKeeper.getDriver() != driver) {
			pageKeeper.setDriver(driver);
		}
		if (this instanceof AbstractPageObjectResolver) {
			pageKeeper.registerPageObjectResolver((AbstractPageObjectResolver) this);
		}

		return pageKeeper;
	}

	/**
	 * Setters/Getters
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * will return the current instance of the WebDriver if exist.
	 * 
	 * @return WebDriver
	 */
	public WebDriver getDriver() {
		return this.driver;
	}

	/**
	 * Sets the startup domain for the browser.
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getChromeDriverPath() {
		return chromeDriverPath;
	}

	public void setChromeDriverPath(String chromeServerDriverPath) {
		this.chromeDriverPath = chromeServerDriverPath;
	}

	public String getScreenShotPath() {
		return screenShotPath;
	}

	public void setScreenShotPath(String screenShotPath) {
		this.screenShotPath = screenShotPath;
	}

	public String getSeleniumTimeOut() {
		return seleniumTimeOut;
	}

	public void setSeleniumTimeOut(String timeout) {
		this.seleniumTimeOut = timeout;
	}

	public String getScreenShotFolderName() {
		return screenShotFolderName;
	}

	public void setScreenShotFolderName(String screenShotFolderName) {
		this.screenShotFolderName = screenShotFolderName;
	}

	public String getBrowserPath() {
		return browserPath;
	}

	public void setBrowserPath(String browserPath) {
		this.browserPath = browserPath;
	}

	public String getChromeFlags() {
		return chromeFlags;
	}

	public void setChromeFlags(String chromeFlags) {
		this.chromeFlags = chromeFlags;
	}

	public boolean isWindowMaximize() {
		return windowMaximize;
	}

	public void setWindowMaximize(boolean windowMaximize) {
		this.windowMaximize = windowMaximize;
	}

	public String getChromeProfile() {
		return chromeProfile;
	}

	public void setChromeProfile(String chromeProfile) {
		this.chromeProfile = chromeProfile;
	}

	public String getChromeExtension() {
		return chromeExtension;
	}

	public void setChromeExtension(String chromeExtension) {
		this.chromeExtension = chromeExtension;
	}

	public String getFirefoxExtension() {
		return firefoxExtension;
	}

	public void setFirefoxExtension(String firefoxExtension) {
		this.firefoxExtension = firefoxExtension;
	}

	public String getFirefoxProfile() {
		return firefoxProfile;
	}

	public void setFirefoxProfile(String firefoxProfile) {
		this.firefoxProfile = firefoxProfile;
	}

	public boolean isIgnoreCertificateErrors() {
		return ignoreCertificateErrors;
	}

	public void setIgnoreCertificateErrors(boolean ignoreCertificateErrors) {
		this.ignoreCertificateErrors = ignoreCertificateErrors;
	}

	public String getWebDriverEventListenerClasses() {
		return webDriverEventListenerClasses;
	}

	public void setWebDriverEventListenerClasses(String webDriverEventListenerClasses) {
		this.webDriverEventListenerClasses = webDriverEventListenerClasses;
	}

	public WebDriverType getWebDriver() {
		return webDriver;
	}

	public void setWebDriver(WebDriverType webDriver) {
		this.webDriver = webDriver;
	}

	public boolean isLazyInit() {
		return lazyInit;
	}

	public void setLazyInit(boolean lazyInit) {
		this.lazyInit = lazyInit;
	}

	/**
	 * @return True if and only if the driver set to clear cookies inside
	 *         {@link #openBrowser()} method
	 */
	public boolean isClearCookiesBeforeOpen() {
		return clearCookiesBeforeOpen;
	}

	/**
	 * The WebDriverSystemObject can delete all cookies before navigating to the
	 * domain (in {@link #openBrowser()} and {@link #init()} methods)
	 * 
	 * @param clearCookiesBeforeOpen
	 * 
	 */
	public void setClearCookiesBeforeOpen(boolean clearCookiesBeforeOpen) {
		this.clearCookiesBeforeOpen = clearCookiesBeforeOpen;
	}
}
