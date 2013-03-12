package il.co.topq.integframework.webdriver;


import il.co.topq.integframework.webdriver.frames.FramesAjaxElementLocatorFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

/**
 * In this abstract page object, the WebElement proxy will poll the interface on
 * a regular basis until the element is present.
 * 
 * @see AbstractPageObject
 * 
 * 
 */

public abstract class AbstractAjaxPageObject extends AbstractPageObject {

	private static int ajaxTimeout;

	public AbstractAjaxPageObject(final WebDriver driver) {
		this(driver, 30);
	}

	public AbstractAjaxPageObject(final WebDriver driver, int ajaxTimeout) {
		setAjaxTimeout(ajaxTimeout);
		this.driver = driver;
		initElements();
		assertInModule();

	}

	@Override
	void initElements() {
		//ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, ajaxTimeout);
		//there is no real difference between these lines, except that the FramesAjaxElementLocatorFactory also supports frames
		ElementLocatorFactory finder = new FramesAjaxElementLocatorFactory(driver, ajaxTimeout);
		PageFactory.initElements(finder, this);
	}

	/**
	 * @return the ajaxTimeout in seconds
	 */
	public static int getAjaxTimeout() {
		return ajaxTimeout;
	}

	/**
	 * @param ajaxTimeout
	 *            the ajax Timeout in seconds to set
	 */
	public static void setAjaxTimeout(int ajaxTimeout) {
		AbstractAjaxPageObject.ajaxTimeout = ajaxTimeout;
	}

}
