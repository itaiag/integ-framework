package il.co.topq.integframework.webdriver.frames;

import il.co.topq.integframework.webdriver.annotations.Annotations;

import java.lang.reflect.Field;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

class FramesAjaxElementLocator extends org.openqa.selenium.support.pagefactory.AjaxElementLocator {

	private By by;
	private SearchContext searchContext;

	public FramesAjaxElementLocator(WebDriver driver, Field field, int timeOutInSeconds) {
		super(driver, field, timeOutInSeconds);
		this.searchContext = driver;
		Annotations annotations = new Annotations(field);
		by = annotations.buildBy();
	}

	/**
	 * Find the element.
	 */
	@Override
	public WebElement findElement() {
		WebElement element = searchContext.findElement(by);
		return element;
	}

	/**
	 * Find the element list.
	 */
	@Override
	public List<WebElement> findElements() {
		List<WebElement> elements = searchContext.findElements(by);
		return elements;
	}

}