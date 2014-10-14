package il.co.topq.integframework;

import static il.co.topq.integframework.assertion.CompareMethod.BIGGER_OR_EQUALS;
import static il.co.topq.integframework.assertion.CompareMethod.SMALLER;
import il.co.topq.integframework.assertion.Assert;
import il.co.topq.integframework.assertion.ComparableAssertion;
import il.co.topq.integframework.assertion.PredicateAssertionLogic;
import il.co.topq.integframework.utils.RandomUtils;

import java.util.List;
import java.util.Random;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

import com.google.common.base.Predicates;

public class RandomTests {
	private Random random = new Random(System.currentTimeMillis());

	@DataProvider(name = "intranges", parallel = true)
	public Object[][] intranges() {
		return new Integer[][] { new Integer[] { 0, 100 }, new Integer[] { -100, 0 }, new Integer[] { -100, 100 },
				new Integer[] { -100, -50 }, new Integer[] { 50, 100 }, new Integer[] { Integer.MIN_VALUE, Integer.MAX_VALUE },
				new Integer[] { 0, Integer.MAX_VALUE }, new Integer[] { Integer.MIN_VALUE, 0 } };

	}

	@Test(suiteName = "random", invocationCount = 100, dataProvider = "intranges", threadPoolSize = 10)
	public void testRandomInts(int min, int max) {
		int r = RandomUtils.getRandomInt(min, max, random);
		Assert.assertLogic(r, new ComparableAssertion<>(SMALLER, max));
		Assert.assertLogic(r, new ComparableAssertion<>(BIGGER_OR_EQUALS, min));

	}

	@DataProvider(name = "longranges", parallel = true)
	public Object[][] longranges() {
		return new Long[][] { new Long[] { 0l, 1000l }, new Long[] { -1000l, 0l }, new Long[] { -1000l, -500l },
				new Long[] { 500l, 1000l }, new Long[] { (long) Integer.MIN_VALUE, (long) Integer.MAX_VALUE },
				new Long[] { 3 + 4 * (long) Integer.MIN_VALUE, 3 + 4 * (long) Integer.MAX_VALUE },
				new Long[] { Long.MIN_VALUE, Long.MAX_VALUE }, new Long[] { 0l, Long.MAX_VALUE }, new Long[] { Long.MIN_VALUE, 0l } };

	}

	@Test(suiteName = "random", invocationCount = 100, dataProvider = "longranges", threadPoolSize = 10)
	public void testRandomLongs(long min, long max) {
		long r = RandomUtils.getRandomLong(min, max, random);
		Assert.assertLogic(r, new ComparableAssertion<>(SMALLER, max));
		Assert.assertLogic(r, new ComparableAssertion<>(BIGGER_OR_EQUALS, min));

	}

	@Test(invocationCount = 50, threadPoolSize = 5)
	public void testRandomItem() {
		List<String> letters = Lists.newArrayList("a", "b", "C", "D", "1", "2", "3", "4");
		String r = RandomUtils.getRandomItemFrom(letters);
		Assert.assertLogic(r, new PredicateAssertionLogic<>(Predicates.in(letters)));
	}

	@Test(expectedExceptions = { IllegalArgumentException.class }, expectedExceptionsMessageRegExp = "-?\\d+ is smaller then -?\\d+")
	public void testInvalidRange() {
		RandomUtils.getRandomInt(200, -200, random);
	}

	@Test(expectedExceptions = { IllegalArgumentException.class }, expectedExceptionsMessageRegExp = "-?\\d+ is smaller then -?\\d+")
	public void testInvalidRangeLong() {
		RandomUtils.getRandomLong(200, -200, random);
	}

}
