package il.co.topq.integframework;

import il.co.topq.integframework.db.DatabaseSystemModule;
import junit.framework.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.topq.integ.reporting.Reporter;

public class DatabaseSystemModuleTests extends AbstractIntegrationTestCase {

	@Autowired
	private DatabaseSystemModule db;

	@Test
	public void testNumberOfRows() {
		Reporter.log("About to test database");
		Assert.assertEquals(3, db.countRowsInTable("QUEUE.NAME"));
	}

}
