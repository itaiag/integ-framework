package $package;

import il.co.topq.integframework.webdriver.WebDriverSystemModule;
import javax.annotation.PostConstruct;

public class Web extends WebDriverSystemModule {

	public Web() throws Exception {
		super();
	}

	@PostConstruct
	public void init() {
		openBrowser();
		driver.get(domain);
	}

	public GoogleSearchPage getGoogleSearchPage() {
		return new GoogleSearchPage(driver);
	}

}
