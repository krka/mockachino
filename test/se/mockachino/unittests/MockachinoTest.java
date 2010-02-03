package se.mockachino.unittests;

import org.junit.Test;
import se.mockachino.Answer;
import se.mockachino.MethodCall;
import se.mockachino.Mockachino;
import se.mockachino.matchers.Matchers;
import se.mockachino.order.InOrder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class MockachinoTest {

	@Test
	public void testSimpleTest() {
		List mock = Mockachino.mock(List.class);

		Mockachino.stubReturn(123).on(mock).size();
		Mockachino.stubReturn(100).on(mock).indexOf("Hello");
		Mockachino.stubReturn(200).on(mock).indexOf("World");
		Mockachino.stubReturn(300).on(mock).indexOf("Test");

		assertEquals(123, mock.size());
		assertEquals(100, mock.indexOf("Hello"));
		assertEquals(200, mock.indexOf("World"));
		assertEquals(300, mock.indexOf("Test"));

		Mockachino.spy(List.class, new ArrayList());

		foo(mock);

		Mockachino.verifyOnce().on(mock).add(1, Matchers.regexp("Hell.*"));
		Mockachino.verifyNever().on(mock).add(1, Matchers.regexp("Helll.*"));
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
		Mockachino.verifyOnce().on(mock).add("Hello");
		Mockachino.verifyOnce().on(mock).add("World");
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

		Mockachino.verifyExactly(1).on(mock).add("Hello");

		InOrder order = Mockachino.verifyOrder();
		order.verify(mock).add("Hello");
		order.verify(mock).add("World");

		InOrder order2 = Mockachino.verifyOrder();
		order2.verify(mock).add("Hello");
		order2.verify(mock).remove(Matchers.type(Object.class));

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
		try {
			List mock = Mockachino.mock(List.class);
			Mockachino.stubThrow(new ArrayIndexOutOfBoundsException("Hello")).on(mock).add(Matchers.type(Object.class));
			mock.add("");
			fail("Should have thrown an exception");
		} catch (Exception e) {
			assertEquals("Hello", e.getMessage());
		}
	}

	@Test
	public void testSpy2() {
		List list = new LinkedList();
		List spy = Mockachino.spy(List.class, list);

		Mockachino.stubReturn("foo").on(spy).get(0);

		assertEquals("foo", spy.get(0));
	}

	@Test
	public void testInlineMock() {
		List mockA = Mockachino.mock(List.class);
		List mockB = Mockachino.mock(List.class);

		Mockachino.stubReturn(mockB).on(mockA).get(Matchers.anyInt());

		assertSame(mockB, mockA.get(0));
	}

	@Test
	public void testInlineMock2() {
		List<List> mock = Mockachino.mock(List.class);

		Mockachino.stubReturn(Mockachino.mock(List.class)).on(mock).get(Matchers.anyInt());

		List inlinedMock = mock.get(0);

		// Do some stubbing on the inlined mock to verify that it works.
		Mockachino.stubReturn("Foo").on(inlinedMock).get(123);
		assertEquals("Foo", inlinedMock.get(123));
		assertEquals(null, inlinedMock.get(124));
	}

	@Test
	public void testSpyString() {
		String s = "Hello world";
		CharSequence mock = Mockachino.spy(CharSequence.class, s);

		Mockachino.stubReturn("Jello").on(mock).subSequence(0, 5);

		assertEquals("Hell", mock.subSequence(0, 4));
		assertEquals("Jello", mock.subSequence(0, 5));

		Mockachino.verifyExactly(1).on(mock).subSequence(0, 4);
		Mockachino.verifyExactly(1).on(mock).subSequence(Matchers.eq(0), Matchers.eq(4));
	}

	@Test
	public void testSpyMock() {
		List mock = Mockachino.mock(List.class);
		List spy = Mockachino.spy(List.class, mock);

		Mockachino.stubReturn("Hello").on(mock).get(123);
		Mockachino.stubReturn("World").on(spy).get(123);
		Mockachino.stubReturn("Foo").on(mock).get(456);

		Mockachino.verifyNever().on(mock).get(Matchers.type(int.class));
		Mockachino.verifyNever().on(spy).get(Matchers.type(int.class));

		assertEquals("Hello", mock.get(123));
		Mockachino.verifyOnce().on(mock).get(123);
		Mockachino.verifyNever().on(spy).get(Matchers.anyInt());

		assertEquals("Foo", spy.get(456));
		Mockachino.verifyOnce().on(mock).get(456);
		Mockachino.verifyOnce().on(spy).get(456);

		assertEquals("World", spy.get(123));
		Mockachino.verifyOnce().on(spy).get(123);
	}

	@Test
	public void testBadMatcher() {
		try {
			List mock = Mockachino.mock(List.class);
			Matchers.anyInt();
			Mockachino.stubReturn("Hello").on(mock).get(0);

			fail("Not supposed to accept bad matcher usage");
		} catch (Exception e) {

		}
	}

	@Test
	public void testOverwriteStub() {
		List mock = Mockachino.mock(List.class);
		Mockachino.stubReturn("A").on(mock).get(0);
		Mockachino.stubReturn("B").on(mock).get(0);
		assertEquals("B", mock.get(0));

	}

	private static interface Foo {
		String bar(String s);
		int bar(int i);
	}

	@Test
	public void testMatcherType() {
		Foo mock = Mockachino.mock(Foo.class);
		Mockachino.stubReturn("World").on(mock).bar("Hello");
		Mockachino.stubReturn(123).on(mock).bar(Matchers.anyInt());

		assertEquals("World", mock.bar("Hello"));
		assertEquals(123, mock.bar(789));

		Mockachino.verifyOnce().on(mock).bar("Hello");
		Mockachino.verifyOnce().on(mock).bar(Matchers.type(String.class));
		Mockachino.verifyOnce().on(mock).bar(Matchers.eq("Hello"));
		Mockachino.verifyOnce().on(mock).bar(Matchers.same("Hello"));
		Mockachino.verifyOnce().on(mock).bar((String) Matchers.notSame(null));
		Mockachino.verifyOnce().on(mock).bar(Matchers.<String>notNull());

		Mockachino.verifyOnce().on(mock).bar(789);
		Mockachino.verifyOnce().on(mock).bar(Matchers.type(Integer.class));
		Mockachino.verifyOnce().on(mock).bar(Matchers.eq("Hello"));
		Mockachino.verifyOnce().on(mock).bar(Matchers.same("Hello"));
		Mockachino.verifyOnce().on(mock).bar((String) Matchers.notSame(null));
		Mockachino.verifyOnce().on(mock).bar(Matchers.<String>notNull());

		assertEquals(null, mock.bar("A"));
	}

	@Test
	public void testVerifyInOrderDuplicate() {
		List mock = Mockachino.mock(List.class);
		mock.add("Hello");
		mock.add("World");
		mock.add("Hello");

		InOrder order = Mockachino.verifyOrder();
		order.verify(mock).add("Hello");
		order.verify(mock).add("World");
		order.verify(mock).add("Hello");
	}

	@Test
	public void testAnswer() {
		List mock = Mockachino.mock(List.class);
		Mockachino.stubAnswer(new Answer() {
			@Override
			public Object getValue(MethodCall call) {
				Object obj = call.getArguments()[0];
				return obj.toString() + obj;
			}
		}).on(mock).get(Matchers.anyInt());

		for (int i = 0; i < 100; i++) {
			String s = "" + i;
			String s2 = s + s;
			assertEquals(s2, mock.get(i));
		}
		InOrder order = Mockachino.verifyOrder();
		for (int i = 0; i < 100; i++) {
			order.verify(mock).get(i);
		}

		Mockachino.verifyExactly(100).on(mock).get(Matchers.anyInt());

	}
}
