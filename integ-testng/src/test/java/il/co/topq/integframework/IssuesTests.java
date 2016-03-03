package il.co.topq.integframework;

import il.co.topq.integframework.issue.IssueType;
import il.co.topq.integframework.issue.KnownIssue;
import il.co.topq.integframework.issue.KnownIssueTestListener;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(KnownIssueTestListener.class)
public class IssuesTests {

	@Test
	public void pass() {
		Assert.assertTrue(true, "test should pass");
	}

	@KnownIssue()
	@Test
	public void knownIssue() {
		Assert.assertTrue(false, "test should pass, failure is a known issue");
	}

	@KnownIssue(throwableType = IOException.class)
	@Test
	public void knownIssueThrowable() throws IOException {
		throw new IOException("test should pass, exception is a known issue");
	}

	@KnownIssue(throwableType = IOException.class, dueTo = 1500000000000l)
	@Test(enabled = false)
	// test result depends on time, enable to locally test, change due date to
	// "bug due date"
	public void knownIssueThrowableDue() throws IOException {
		throw new IOException("test should pass, exception is a known issue");
	}

	@KnownIssue(throwableType = IOException.class, dueTo = 1300000000000l)
	@Test(enabled = false)
	// test result depends on time, enable to locally test
	public void knownIssueThrowableDueFails() throws IOException {
		throw new IOException("test should pass, exception is a known issue, should have been fixed by now!!!");
	}

	@KnownIssue(type = IssueType.FAIL)
	@Test(expectedExceptions = IOException.class, enabled = false)
	public void knownIssueNegativeTest() throws IOException {
		throw new IOException("test should fail, exception is a known issue - not warning!!!");
	}

}
