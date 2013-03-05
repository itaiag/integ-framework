package il.co.topq.integframework;

import il.co.topq.integframework.assertion.Assert;
import il.co.topq.integframework.assertion.FindTextAssertion;

import org.testng.annotations.Test;


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
