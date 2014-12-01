package il.co.topq.integframework.webdriver;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

/**
 * The page object design pattern makes it easier to access HTML elements in a
 * page.<br>
 * Each {@link WebElement} field (i.e. {@code private WebElement element}) will
 * be automatically attached to the one in the {@link WebDriver}<br>
 * The default locating mechanism is {@link ByIdOrName}, that means that in the
 * previous example, {@code element} will be attached to a
 * {@code <div name='element'>...</div>}<br>
 * In order to use other mechanism, or use other name or id (such as in id that
 * contains colon etc,) use {@link FindBy} or {@link FindBys} e.g: <br>
 * {@code public class SamplePage extends AbstractPageObject <br>
 * 
 * &nbsp;&nbsp;{@literal @}FindBys (value={
 * </br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@literal @}FindBy(id="container"),
 * </br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@literal @}
 * FindBy(linkText="start")</br>
 * &nbsp;&nbsp;}&nbsp;&nbsp;)<br>
 * 
 * &nbsp;&nbsp;private {@link WebElement} element;<br>
 * <br>
 * &nbsp;&nbsp;{@literal @}FindBy(css="div.containers")<br>
 * &nbsp;&nbsp;private {@link List}<{@link WebElement}> allDivContainers;<br>
 * <br>
 * &nbsp;&nbsp;private {@link WebElement} element; //this element is the same as
 * with {@literal @}FindBy(id="element")<br>
 * <br>
 * }<br>
 * 
 * 
 * 
 * @author Aharon
 * 
 */
public abstract class AbstractPageObject implements HasWebDriver {

	protected WebDriver driver;

	public AbstractPageObject(final WebDriver driver) {
		this.driver = driver;
		initElements();
		assertInModule();
	}

	protected AbstractPageObject() {
	}

	/**
	 * change all {@link WebElement} and {@link List}<{@link WebElement}> fields
	 * of this object to be proxys to the HTML elements in the page
	 */
	void initElements() {
		PageFactory.initElements(driver, this);
	}

	/**
	 * Asserts that the web is actually opened in the page we expect it to be.<br>
	 * this method will run after all of the elements
	 * e.g:
	 * 
	 * <pre>
	 * {@code 
	 * &#64;Override void assertInModule(){
	 *  WebDriverWait wait = new WebDriverWait(driver, 10);
	 * 	wait.until(ExpectedConditions.presenceOfElementLocated(By.id("container")));
	 * 	wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("start")));
	 * 	}
	 * }
	 * </pre>
	 */
	protected abstract void assertInModule();

	/**
	 * 
	 * @return the driver that initialized this object
	 */
	@Override
	public WebDriver getDriver() {
		return this.driver;
	}

}
