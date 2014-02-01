package se.mockachino.blackbox;

import org.junit.Test;
import se.mockachino.Mockachino;
import se.mockachino.alias.Alias;
import se.mockachino.alias.SimpleAlias;
import se.mockachino.matchers.Matchers;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AliasTest {
	@Test
	public void testEmpty() {
		List mock = Mockachino.mock(List.class);

		SimpleAlias a = Mockachino.newAlias();
		a.bind(mock).add("Hello");

		assertEquals(0, a.count());
	}

	@Test
	public void testCount() {
		List mock = Mockachino.mock(List.class);

		mock.add("Hello");
		mock.add("Helly");
		SimpleAlias a = Mockachino.newAlias();
		a.bind(mock).add(Matchers.regexp("Hell.*"));

		assertEquals(2, a.count());
	}

	@Test
	public void testJoin1() {
		List mock = Mockachino.mock(List.class);
		mock.add("Hello");
		assertEquals(1, getCountHello(mock));
	}

	@Test
	public void testJoin2() {
		List mock = Mockachino.mock(List.class);
		mock.add("Hello2");
		assertEquals(0, getCountHello(mock));
	}

	@Test
	public void testJoin3() {
		List mock = Mockachino.mock(List.class);
		mock.remove("Hello");
		assertEquals(1, getCountHello(mock));
	}


	private int getCountHello(List mock) {
		SimpleAlias a = Mockachino.newAlias();
		a.bind(mock).add("Hello");

		SimpleAlias b = Mockachino.newAlias();
		b.bind(mock).remove("Hello");

		Alias orAlias = a.union(b);
		return orAlias.count();
	}

	@Test
	public void testJoinDuplicate() {
		List mock = Mockachino.mock(List.class);

		mock.add("Hello");

		SimpleAlias a = Mockachino.newAlias();
		a.bind(mock).add("Hello");

		SimpleAlias b = Mockachino.newAlias();
		b.bind(mock).add("Hello");

		Alias orAlias = a.union(b);
		assertEquals(1, orAlias.count());
	}

	@Test
	public void testUnion() {
		List mock = Mockachino.mock(List.class);

		mock.add("Hello");
		mock.remove("Hello");

		SimpleAlias a = Mockachino.newAlias();
		a.bind(mock).add("Hello");

		SimpleAlias b = Mockachino.newAlias();
		b.bind(mock).remove("Hello");

		Alias orAlias = a.union(b);
		assertEquals(2, orAlias.count());
	}

	@Test
	public void testIntersection() {
		List mock = Mockachino.mock(List.class);

		mock.add("Hello");
		mock.add("Jello");

		SimpleAlias a = Mockachino.newAlias();
		a.bind(mock).add("Hello");

		SimpleAlias b = Mockachino.newAlias();
		b.bind(mock).add(Matchers.any(String.class));

		assertEquals(1, a.count());
		assertEquals(2, b.count());
		assertEquals(1, a.intersection(b).count());

		a.verifyExactly(1);
		b.verifyExactly(2);
		a.intersection(b).verifyExactly(1);
	}
}
