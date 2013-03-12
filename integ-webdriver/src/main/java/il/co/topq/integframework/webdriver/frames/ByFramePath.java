package il.co.topq.integframework.webdriver.frames;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

public class ByFramePath extends By {

	public ByFramePath(By by, String... framelist) {
		if (framelist.length > 0) {
			this.framelist = framelist;
		}
		else {
			throw new IllegalArgumentException("framelist must not be empty");
		}
		this.by = by;
	}

	private final String[] framelist;
	private final By by;
	private static String lastFrameEntered;
	public static boolean lazyMode = false;

	@Override
	public List<WebElement> findElements(SearchContext context) {
		if (context instanceof WebDriver) {
			WebDriver driver = (WebDriver) context;
			if (!lazyMode || (lazyMode && !framelist[framelist.length - 1].equals(lastFrameEntered))) {
				driver.switchTo().defaultContent();
				for (String currFrame: framelist) {
					driver.switchTo().frame(currFrame);
					lastFrameEntered = currFrame;
				}
			}
			return by.findElements(context);
		}
		throw new WebDriverException(context.toString() + " Is not a WebDriver");
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder(this.getClass().getSimpleName());
		stringBuilder.append("({/");
		for (String currframe: framelist) {
			stringBuilder.append("/frame:").append(currframe);
		}
		stringBuilder.append("/").append(by.toString());
		stringBuilder.append("})");
		return stringBuilder.toString();
	}

}