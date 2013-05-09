package $package;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import il.co.topq.integframework.webdriver.AbstractAjaxPageObjectImpl;

public class GoogleSearchPage extends AbstractAjaxPageObjectImpl {

	@FindBy(name = "q")
	private WebElement searchBox;

	@FindBy(name = "btnK")
	private WebElement searchBtn;

	public GoogleSearchPage(WebDriver driver) {
		super(driver);
	}

	public void enterText(String searchText) {
		searchBox.sendKeys(searchText);
	}

	public GoogleSearchResultPage clickOnSearchBtn() {
		searchBtn.click();
		return new GoogleSearchResultPage(driver);
	}

}
