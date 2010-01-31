package se.mockachino;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import se.mockachino.matchers.Matchers;
import se.mockachino.order.InOrder;
import se.mockachino.Mockachino;

public class MockachinoTest {

	@Test
	public void testSimpleTest() {
		List mock = Mockachino.mock(List.class);

		Mockachino.stubReturn(mock, 123).size();
		Mockachino.stubReturn(mock, 100).indexOf("Hello");
		Mockachino.stubReturn(mock, 200).indexOf("World");
		Mockachino.stubReturn(mock, 300).indexOf("Test");

		assertEquals(123, mock.size());
		assertEquals(100, mock.indexOf("Hello"));
		assertEquals(200, mock.indexOf("World"));
		assertEquals(300, mock.indexOf("Test"));

		Mockachino.spy(List.class, new ArrayList());

		foo(mock);

		Mockachino.verify(mock).add(1, Matchers.regexp("Hell.*"));
		Mockachino.verifyNever(mock).add(1, Matchers.regexp("Helll.*"));
	}

	private void foo(List mock) {
		mock.add(1, "Hello");
		mock.add(2, "World");
	}

	@Test
	public void testSpy() {
		List mock = Mockachino.spy(List.class, new ArrayList());

		mock.add("Hello");
		mock.add("World");

		assertEquals(2, mock.size());
		Mockachino.verify(mock).add("Hello");
		Mockachino.verify(mock).add("World");
		assertEquals(true, mock.remove("Hello"));
		assertEquals(false, mock.remove("Foo"));
		assertEquals(1, mock.size());
	}

	@Test
	public void testInOrder() {
		List mock = Mockachino.mock(List.class);
		mock.add("Hello");
		mock.remove("Hello");
		mock.add("World");

		Mockachino.verifyExactly(mock, 1).add("Hello");

		InOrder order = Mockachino.verifyOrder();
		order.verify(mock).add("Hello");
		order.verify(mock).add("World");

		InOrder order2 = Mockachino.verifyOrder();
		order2.verify(mock).add("Hello");
		order2.verify(mock).remove(Matchers.any());

	}

	@Test
	public void testInOrderFail() {
		List mock = Mockachino.mock(List.class);
		mock.add("Hello");
		mock.remove("Hello");
		mock.add("World");

		try {
			InOrder order = Mockachino.verifyOrder();
			order.verify(mock).add("World");
			order.verify(mock).add("Hello");
			fail("Expected out of order calls to fail");
		} catch (Exception e) {

		}
	}

	@Test
	public void testException() {
		List mock = Mockachino.mock(List.class);
		Mockachino.stubThrow(mock, new ArrayIndexOutOfBoundsException("Hello")).add(Matchers.any());

		//mock.add(null);
	}

	@Test
	public void testSpy2() {
		List list = new LinkedList();
		List spy = Mockachino.spy(List.class, list);

		Mockachino.stubReturn(spy, "foo").get(0);

		assertEquals("foo", spy.get(0));
	}

	@Test
	public void testInlineMock() {
		List mockA = Mockachino.mock(List.class);
		List mockB = Mockachino.mock(List.class);

		Mockachino.stubReturn(mockA, mockB).get(Matchers.anyInt());

		assertTrue(mockB == mockA.get(0));
	}

	@Test
	public void testSpyString() {
		String s = "Hello world";
		CharSequence mock = Mockachino.spy(CharSequence.class, s);

		Mockachino.stubReturn(mock, "Jello").subSequence(0, 5);

		assertEquals("Hell", mock.subSequence(0, 4));
		assertEquals("Jello", mock.subSequence(0, 5));

		Mockachino.verifyExactly(mock, 1).subSequence(0, 4);
		Mockachino.verifyExactly(mock, 1).subSequence(0, Matchers.eq(4));
	}

	@Test
	public void testSpyMock() {
		List mock = Mockachino.mock(List.class);
		List spy = Mockachino.spy(List.class, mock);

		Mockachino.stubReturn(mock, "Hello").get(123);
		Mockachino.stubReturn(spy, "World").get(123);
		Mockachino.stubReturn(mock, "Foo").get(456);

		Mockachino.verifyNever(mock).get(Matchers.anyInt());
		Mockachino.verifyNever(spy).get(Matchers.anyInt());

		assertEquals("Hello", mock.get(123));
		Mockachino.verifyExactly(mock, 1).get(123);
		Mockachino.verifyNever(spy).get(Matchers.anyInt());

		assertEquals("Foo", spy.get(456));
		Mockachino.verifyExactly(mock, 1).get(456);
		Mockachino.verifyExactly(spy, 1).get(456);

		assertEquals("World", spy.get(123));
		Mockachino.verifyExactly(spy, 1).get(123);
	}

	@Test
	public void testBadMatcher() {
		try {
			List mock = Mockachino.mock(List.class);
			Matchers.anyInt();
			Mockachino.stubReturn(mock, "Hello").get(0);

			fail("Not supposed to accept bad matcher usage");
		} catch (Exception e) {

		}
	}

	@Test
	public void testOverwriteStub() {
		List mock = Mockachino.mock(List.class);
		Mockachino.stubReturn(mock, "A").get(0);
		Mockachino.stubReturn(mock, "B").get(0);
		assertEquals("B", mock.get(0));

	}


}
