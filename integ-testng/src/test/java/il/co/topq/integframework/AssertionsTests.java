package il.co.topq.integframework;

import il.co.topq.integframework.assertion.Assert;
import il.co.topq.integframework.assertion.CollectionAssertion;
import il.co.topq.integframework.assertion.FailSafeAssertionListener;

import java.util.List;

import org.testng.annotations.Test;
import org.testng.collections.Lists;

public class AssertionsTests {

	@Test
	public void testAssertCollection() {
		Assert.assertLogic(Lists.newArrayList("A", "S", "D", "F"),
				new CollectionAssertion<>(Lists.newArrayList("F", "D", "A", "S")));
	}

	@Test(expectedExceptions = AssertionError.class, expectedExceptionsMessageRegExp = "Total items not found: \\d+")
	public void testAssertCollectionFails() throws Throwable {
		FailSafeAssertionListener<List<String>> assertionListener = new FailSafeAssertionListener<>();
		List<String> actual = Lists.newArrayList("A", "S", "D", "F"), expected = Lists.newArrayList("Q", "W", "E", "A");
		CollectionAssertion<String> assertion = new CollectionAssertion<>(expected);
		Assert.assertLogic(actual, assertion, assertionListener);
		throw assertionListener.getRootSuppressedThrowable();
	}

	@Test(expectedExceptions = AssertionError.class, expectedExceptionsMessageRegExp = "Duplicates found in actual")
	public void testAssertCollectionActualDuplications() throws Throwable {
		FailSafeAssertionListener<List<String>> assertionListener = new FailSafeAssertionListener<>();
		List<String> actual = Lists.newArrayList("A", "A", "D", "F"), expected = Lists.newArrayList("Q", "W", "E", "R");
		CollectionAssertion<String> assertion = new CollectionAssertion<>(expected);
		Assert.assertLogic(actual, assertion, assertionListener);
		throw assertionListener.getRootSuppressedThrowable();
	}

	@Test(expectedExceptions = AssertionError.class, expectedExceptionsMessageRegExp = "Duplicates found in expected")
	public void testAssertCollectionExpectedDuplications() throws Throwable {
		FailSafeAssertionListener<List<String>> assertionListener = new FailSafeAssertionListener<>();
		List<String> actual = Lists.newArrayList("A", "S", "D", "F"), expected = Lists.newArrayList("Q", "W", "R", "R");
		CollectionAssertion<String> assertion = new CollectionAssertion<>(expected);
		Assert.assertLogic(actual, assertion, assertionListener);
		throw assertionListener.getRootSuppressedThrowable();
	}
}
