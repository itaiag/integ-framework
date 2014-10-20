package il.co.topq.integframework;

import static il.co.topq.integframework.assertion.Assert.assertLogic;
import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static org.testng.Assert.assertNull;
import static org.testng.collections.Lists.newArrayList;
import il.co.topq.integframework.assertion.CollectionAssertion;
import il.co.topq.integframework.assertion.FailSafeAssertionListener;

import java.util.List;

import org.testng.annotations.Test;

public class AssertionsTests {

	@Test
	public void testAssertCollection() {
		assertLogic(newArrayList("A", "S", "D", "F"), new CollectionAssertion<>(newArrayList("F", "D", "A", "S")));
	}

	@Test(expectedExceptions = AssertionError.class, expectedExceptionsMessageRegExp = "Total items not found: \\d+")
	public void testAssertCollectionFails() throws Throwable {
		FailSafeAssertionListener<List<String>> assertionListener = new FailSafeAssertionListener<>();
		assertLogic(newArrayList("A", "S", "D", "F"), new CollectionAssertion<>(newArrayList("Q", "W", "E", "A")),
				assertionListener);
		throw assertionListener.getRootSuppressedThrowable();
	}

	@Test(expectedExceptions = AssertionError.class, expectedExceptionsMessageRegExp = "Duplicates found in actual")
	public void testAssertCollectionActualDuplications() throws Throwable {
		FailSafeAssertionListener<List<String>> assertionListener = new FailSafeAssertionListener<>();
		assertLogic(newArrayList("A", "A", "D", "F"), new CollectionAssertion<>(newArrayList("Q", "W", "E", "R")),
				assertionListener);
		throw assertionListener.getRootSuppressedThrowable();
	}

	@Test(expectedExceptions = AssertionError.class, expectedExceptionsMessageRegExp = "Duplicates found in expected")
	public void testAssertCollectionExpectedDuplications() throws Throwable {
		FailSafeAssertionListener<List<String>> assertionListener = new FailSafeAssertionListener<>();
		assertLogic(newArrayList("A", "S", "D", "F"), new CollectionAssertion<>(newArrayList("Q", "W", "R", "R")),
				assertionListener);
		throw assertionListener.getRootSuppressedThrowable();
	}

	@Test(expectedExceptions = AssertionError.class, expectedExceptionsMessageRegExp = "all items should exists and nothing more")
	public void testAssertCollectionMoreActuals() throws Throwable {
		FailSafeAssertionListener<List<String>> assertionListener = new FailSafeAssertionListener<>();
		assertLogic(newArrayList("A", "S", "D", "F", "Q"),
				new CollectionAssertion<>(newArrayList("A", "S", "D", "F")).containsAllItems(), assertionListener);
		throw assertionListener.getRootSuppressedThrowable();
	}

	@Test
	public void testAssertCollectionMoreActualsOK() throws Throwable {
		FailSafeAssertionListener<List<String>> assertionListener = new FailSafeAssertionListener<>();
		assertLogic(newArrayList("A", "S", "D", "F", "Q"), new CollectionAssertion<>(newArrayList("A", "S", "D", "F")),
				assertionListener);
		assertNull(assertionListener.getRootSuppressedThrowable());
	}

	@Test
	public void testAssertCollectionComparator() throws Throwable {
		FailSafeAssertionListener<List<String>> assertionListener = new FailSafeAssertionListener<>();
		assertLogic(newArrayList("A", "S", "D", "F"),
				new CollectionAssertion<>(newArrayList("a", "s", "D", "F")).compareWith(CASE_INSENSITIVE_ORDER), assertionListener);
		assertNull(assertionListener.getRootSuppressedThrowable());
	}

	@Test(expectedExceptions = AssertionError.class, expectedExceptionsMessageRegExp = "Duplicates found in actual")
	public void testAssertCollectionComparatorDuplicates() throws Throwable {
		FailSafeAssertionListener<List<String>> assertionListener = new FailSafeAssertionListener<>();
		assertLogic(newArrayList("A", "S", "D", "F", "a"),
				new CollectionAssertion<>(newArrayList("a", "s", "D", "F")).compareWith(CASE_INSENSITIVE_ORDER), assertionListener);
		throw assertionListener.getRootSuppressedThrowable();
	}

}
