package il.co.topq.integframework.webdriver.frames;

import java.lang.reflect.Field;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocator;

public class FramesAjaxElementLocatorFactory extends AjaxElementLocatorFactory {
	private WebDriver driver;
	private int timeOutInSeconds;

	public FramesAjaxElementLocatorFactory(WebDriver driver, int timeOutInSeconds) {
		super(driver, timeOutInSeconds);
		this.driver = driver;
		this.timeOutInSeconds = timeOutInSeconds;
	}

	@Override
	public ElementLocator createLocator(Field field) {
		return new FramesAjaxElementLocator(driver, field, timeOutInSeconds);
	}

}