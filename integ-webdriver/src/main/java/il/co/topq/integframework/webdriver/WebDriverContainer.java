package il.co.topq.integframework.webdriver;

import org.openqa.selenium.WebDriver;

public interface WebDriverContainer extends HasWebDriver {
	public WebDriver getDriver();
	public void setDriver(WebDriver driver);
}
