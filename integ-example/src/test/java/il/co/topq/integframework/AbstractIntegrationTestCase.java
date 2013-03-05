package il.co.topq.integframework;

import il.co.topq.integframework.bdd.BddExecutor;
import il.co.topq.integframework.bdd.BddI;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

/**
 */

@ContextConfiguration(locations = { "classpath:META-INF/integration-context.xml" })
public abstract class AbstractIntegrationTestCase extends AbstractTestNGSpringContextTests{

	protected void run(BddI bdd) throws Exception{
		BddExecutor.run(bdd);
	}
	
	protected void sleep(long milliToSleep){
		try {
			Thread.sleep(milliToSleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
