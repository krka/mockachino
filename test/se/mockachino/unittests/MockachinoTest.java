package se.mockachino.unittests;

import org.junit.Test;
import se.mockachino.Answer;
import se.mockachino.MethodCall;
import se.mockachino.MockContext;
import se.mockachino.Mockachino;
import se.mockachino.exceptions.UsageError;
import se.mockachino.listener.MethodCallListener;
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

		// Do some stubbing on the inlined mock to match that it works.
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

			fail("Not supposed to accept bad match usage");
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
		order.verify().on(mock).add("Hello");
		order.verify().on(mock).add("World");
		order.verify().on(mock).add("Hello");
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
			order.verify().on(mock).get(i);
		}

		Mockachino.verifyExactly(100).on(mock).get(Matchers.anyInt());

	}

	@Test
	public void testNotAMock() {
		try {
			Throwable notAMock = new Throwable();
			Mockachino.verifyExactly(1).on(notAMock).printStackTrace();
			fail("Should fail");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testBadListener() {
		List mock = Mockachino.mock(List.class);
		Mockachino.listenWith(new MethodCallListener() {
			@Override
			public void listen(Object obj, MethodCall call) {
				Integer i = (Integer) obj;
				System.out.println("i:" + i);
			}
		}).on(mock).add(Matchers.any(Object.class));

		try {
			mock.add("Hello world");
			fail("Should fail");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(expected = UsageError.class)
	public void testBadUsage1() {
		List mock = Mockachino.mock(List.class);
		Mockachino.listenWith(null).on(mock);
	}

	@Test(expected = UsageError.class)
	public void testBadUsage2() {
		List mock = Mockachino.mock(List.class);
		Mockachino.stubThrow(null).on(mock);
	}

	@Test(expected = UsageError.class)
	public void testBadUsage3() {
		List mock = Mockachino.mock(List.class);
		Mockachino.stubAnswer(null).on(mock);
	}

	@Test(expected = UsageError.class)
	public void testBadUsage4() {
		Mockachino.verifyOrder().verify().on(null);
	}

	@Test(expected = UsageError.class)
	public void testBadUsage5() {
		Mockachino.mock(null);
	}

	@Test(expected = UsageError.class)
	public void testBadUsage6() {
		Mockachino.spy(List.class, null);
	}

	@Test(expected = UsageError.class)
	public void testBadUsage7() {
		Mockachino.mock(List.class, null);
	}

	@Test(expected = UsageError.class)
	public void testBadUsage8() {
		Mockachino.reset("Hello world");
	}

	@Test
	public void testReset() {
		List mock = Mockachino.mock(List.class);
		mock.add("Hello");
		mock.remove("Hello");
		mock.add("World");

		Mockachino.verifyExactly(2).on(mock).add(Matchers.any(Object.class));
		Mockachino.reset(mock);
		Mockachino.verifyExactly(0).on(mock).add(Matchers.any(Object.class));
	}

	@Test
	public void testEquals() {
		MockContext context = new MockContext();
		List mock1 = context.mock(List.class);
		List mock2 = context.mock(List.class);
		assertEquals("Mock:List:1", mock1.toString());
		assertEquals("Mock:List:2", mock2.toString());

		assertNotSame(mock1.hashCode(), mock2.hashCode());
		assertTrue(mock1.equals(mock1));
		assertFalse(mock1.equals(mock2));
		assertFalse(mock1.equals(null));
	}
}

