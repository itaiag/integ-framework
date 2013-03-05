package il.co.topq.integframework;

import org.testng.annotations.Test;

import com.topq.integ.assertion.Assert;
import com.topq.integ.assertion.FindTextAssertion;

public class LogicAssertionTest {

	@Test
	public void testSuccessfulAssertion() throws Exception {
		Assert.assertLogic("outer text inner text outer text", new FindTextAssertion("inner text"));
	}

	@Test(expectedExceptions = { AssertionError.class })
	public void testUnSuccessfulAssertion() throws Exception {
		Assert.assertLogic("outer text inn*er text outer text", new FindTextAssertion("inner text"));
	}

}
