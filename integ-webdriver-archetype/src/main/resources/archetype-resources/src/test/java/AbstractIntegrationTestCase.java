package $package;

import il.co.topq.integframework.webdriver.WebDriverSystemModule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;



/**
 */

@ContextConfiguration(locations = { "classpath:META-INF/integration-context.xml" })
public abstract class AbstractIntegrationTestCase extends AbstractTestNGSpringContextTests{

	@Autowired
	protected WebDriverSystemModule web;
	
	protected void sleep(long milliToSleep){
		try {
			Thread.sleep(milliToSleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
