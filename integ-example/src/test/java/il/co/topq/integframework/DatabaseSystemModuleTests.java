package il.co.topq.integframework;

import il.co.topq.integframework.db.DatabaseSystemModule;
import il.co.topq.integframework.reporting.Reporter;
import junit.framework.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;


public class DatabaseSystemModuleTests extends AbstractIntegrationTestCase {

	@Autowired
	private DatabaseSystemModule db;

	@Test
	public void testNumberOfRows() {
		Reporter.log("About to test database");
		Assert.assertEquals(3, db.countRowsInTable("QUEUE.NAME"));
	}

}
