package il.co.topq.integframework;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import il.co.topq.integframework.utils.Accumulator;

import org.testng.annotations.Test;

import com.google.common.collect.Sets;

public class AccumulatorTests {

	@Test
	public void testAcc() {
		Accumulator<String> accumulator = new Accumulator<>();
		accumulator.increment("A", 1);
		assertEquals(accumulator.get("A").longValue(), 1);
		accumulator.increment("A");
		assertEquals(accumulator.get("A").longValue(), 2);
		accumulator.increment("B");
		assertEquals(accumulator.get("B").longValue(), 1);
		accumulator.increment("B", 2);
		assertEquals(accumulator.get("B").longValue(), 3);
		accumulator.increment("A", 3);
		assertEquals(accumulator.get("A").longValue(), 5);
		
		accumulator.increment("C", 3);
		assertEquals(accumulator.get("C").longValue(), 3);
		assertEquals(accumulator.keySet(), Sets.newHashSet("A", "B", "C"));
		accumulator.reset("C");
		assertNull(accumulator.get("C"));
		assertEquals(accumulator.keySet(), Sets.newHashSet("A", "B"));
		assertNotNull(accumulator.get("A"));
		assertNotNull(accumulator.get("B"));
		
		accumulator.clear();
		assertNull(accumulator.get("A"));
		assertNull(accumulator.get("B"));
	}

}
