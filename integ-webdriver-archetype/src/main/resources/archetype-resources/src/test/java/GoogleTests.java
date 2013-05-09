package $package;


import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class GoogleTests extends AbstractIntegrationTestCase {

	@Autowired
	private Web web;

	@Test
	public void testGoogleSearch() {
		GoogleSearchPage googleSearchPage = web.getGoogleSearchPage();
		googleSearchPage.enterText("Top-Q");
		GoogleSearchResultPage resultPage = googleSearchPage.clickOnSearchBtn();

	}

}
