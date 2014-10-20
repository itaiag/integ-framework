package il.co.topq.integframework;

import il.co.topq.integframework.assertion.Assert;
import il.co.topq.integframework.assertion.CollectionAssertion;
import il.co.topq.integframework.assertion.FailSafeAssertionListener;
import il.co.topq.integframework.issue.KnownIssue;
import il.co.topq.integframework.issue.KnownIssueTestListener;

import java.util.List;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

@Listeners(KnownIssueTestListener.class)
public class AssertionsTests {

	@Test
	public void testAssertCollection() {
		Assert.assertLogic(Lists.newArrayList("A", "S", "D", "F"),
				new CollectionAssertion<>(Lists.newArrayList("F", "D", "A", "S")));
	}

	@Test
	@KnownIssue(messageMustMatch = "Total items not found: \\d+")
	public void testAssertCollectionFails() throws Throwable {
		FailSafeAssertionListener<List<String>> assertionListener = new FailSafeAssertionListener<>();
		List<String> actual = Lists.newArrayList("A", "S", "D", "F"), expected = Lists.newArrayList("Q", "W", "E", "A");
		CollectionAssertion<String> assertion = new CollectionAssertion<>(expected);
		Assert.assertLogic(actual, assertion, assertionListener);
		throw assertionListener.getRootSuppressedThrowable();
	}

	@Test
	@KnownIssue(messageMustMatch = "Duplicates found in actual")
	public void testAssertCollectionActualDuplications() throws Throwable {
		FailSafeAssertionListener<List<String>> assertionListener = new FailSafeAssertionListener<>();
		List<String> actual = Lists.newArrayList("A", "A", "D", "F"), expected = Lists.newArrayList("Q", "W", "E", "R");
		CollectionAssertion<String> assertion = new CollectionAssertion<>(expected);
		Assert.assertLogic(actual, assertion, assertionListener);
		throw assertionListener.getRootSuppressedThrowable();
	}

	@Test
	@KnownIssue(messageMustMatch = "Duplicates found in expected")
	public void testAssertCollectionExpectedDuplications() throws Throwable {
		FailSafeAssertionListener<List<String>> assertionListener = new FailSafeAssertionListener<>();
		List<String> actual = Lists.newArrayList("A", "S", "D", "F"), expected = Lists.newArrayList("Q", "W", "R", "R");
		CollectionAssertion<String> assertion = new CollectionAssertion<>(expected);
		Assert.assertLogic(actual, assertion, assertionListener);
		throw assertionListener.getRootSuppressedThrowable();
	}
}
