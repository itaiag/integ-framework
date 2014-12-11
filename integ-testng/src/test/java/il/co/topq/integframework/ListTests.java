package il.co.topq.integframework;

import java.util.ArrayList;

import il.co.topq.integframework.assertion.Assert;
import il.co.topq.integframework.utils.list.IndexedBy;
import il.co.topq.integframework.utils.list.IndexedList;

import org.testng.annotations.Test;

public class ListTests {
	public static final class Elements implements IndexedBy<String> {
		final int a, b;
		final String s;

		public Elements(int a, int b, String s) {
			this.a = a;
			this.b = b;
			this.s = s;
		}

		@Override
		public String getIndex() {
			return s;
		}
	}

	@Test
	public void testIndexedByList() {
		IndexedList<String, Elements> list = new IndexedList<>(new ArrayList<Elements>());
		Elements e1;
		list.add(e1 = new Elements(1, 2, "A"));
		Assert.assertEquals(list.getItem("A"), e1);
	}

	@Test
	public void testIndexedByList2() {
		IndexedList<String, Elements> list = new IndexedList<>(new ArrayList<Elements>());
		Elements e1, e2;
		list.add(e1 = new Elements(1, 2, "A"));
		list.add(e2 = new Elements(2, 3, "a"));
		Assert.assertEquals(list.getItem("A"), e1);
		Assert.assertEquals(list.getItem("a"), e2);
		
	}
}
