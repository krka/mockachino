package se.mockachino.unittests;

import org.junit.Test;
import se.mockachino.Mockachino;
import se.mockachino.exceptions.VerificationError;
import se.mockachino.matchers.Matchers;
import se.mockachino.matchers.matcher.BasicMatcher;
import se.mockachino.order.OrderingContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ExamplesTest {

	@Test
	public void testExample() {
		// Create a mock
		List mock = Mockachino.mock(List.class);

		// Interact with it
		mock.get(123);
		mock.get(456);

		Mockachino.verifyOnce().on(mock).get(123);
		Mockachino.verifyOnce().on(mock).get(456);

		// Verify that some interactions never occured
		Mockachino.verifyNever().on(mock).get(124);
	}

	@Test
	public void testExampleStub() {
		// Create a mock
		List mock = Mockachino.mock(List.class);

		// Stub a return value
		Mockachino.stubReturn("Hello").on(mock).get(123);

		// Interact with it
		Object hello = mock.get(123);
		Object shouldBeNull = mock.get(456);
		assertEquals("Hello", hello);
		assertEquals(null, shouldBeNull);

		// Verify calls
		Mockachino.verifyOnce().on(mock).get(123);
		Mockachino.verifyOnce().on(mock).get(456);
	}

	@Test
	public void testExampleWithMatchers() {
		// Create a mock
		List mock = Mockachino.mock(List.class);

		mock.add("Hello");
		mock.add("World");

		// Verify calls
		Mockachino.verifyOnce().on(mock).add(Matchers.eq("Hello"));
		Mockachino.verifyExactly(2).on(mock).add(Matchers.any(String.class));
	}

	@Test
	public void testExampleStubOverride() {
		// Create a mock
		List mock = Mockachino.mock(List.class);

		// Stub a return value
		Mockachino.stubReturn("NullString").on(mock).get(Matchers.anyInt());
		Mockachino.stubReturn("Hello").on(mock).get(123);

		// Interact with it
		assertEquals("Hello", mock.get(123));
		assertEquals("NullString", mock.get(1));
		assertEquals("NullString", mock.get(2));

		// Verify calls
		Mockachino.verifyExactly(3).on(mock).get(Matchers.anyInt());
	}

	@Test
	public void testBadMatcherCombo() {
		// Create a mock
		List mock = Mockachino.mock(List.class);

		// This works
		Mockachino.verifyNever().on(mock).add(Matchers.eq(0), Matchers.any(Object.class));

		// This doesn't
		try {
			Mockachino.verifyNever().on(mock).add(0, Matchers.any(Object.class));
			fail("This should never pass");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSimpleVerify() {
		try {
			List mock = Mockachino.mock(List.class);
			mock.get(1);
			mock.get(2);
			mock.get(3);

			Mockachino.verifyAtLeast(2).on(mock).get(3);
			fail("This should never pass");
		} catch (VerificationError e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testInOrder() {
		List mock = Mockachino.mock(List.class);
		mock.add("Hello");
		mock.remove("Hello");
		mock.add("World");
		mock.contains("Hello");
		mock.add("Bar");

		OrderingContext order = Mockachino.verifyOrder();
		order.verify().on(mock).add("Hello");
		order.verify().on(mock).add("World");
		order.verify().on(mock).contains("Hello");
		order.verify().on(mock).add("Bar");
	}

	@Test
	public void testSpy() {
		// Setup spied object
		ArrayList myList = new ArrayList();
		myList.add("Real object");

		// Create spy on object
		List spy = Mockachino.spy(List.class, myList);

		// Show that calls to the spy go to the real object
		assertEquals(1, spy.size());
		assertEquals("Real object", spy.get(0));

		// Overwrite spy.get(0)
		Mockachino.stubReturn("Fake object").on(spy).get(0);
		spy.add("Real object 2");

		// Show that the overwrite worked,
		// but other calls still go through the real object
		assertEquals(2, spy.size());
		assertEquals("Fake object", spy.get(0));
		assertEquals("Real object 2", spy.get(1));

		// Verifying the calls still work of course
		Mockachino.verifyExactly(2).on(spy).size();
		Mockachino.verifyExactly(3).on(spy).get(Matchers.anyInt());
		Mockachino.verifyExactly(1).on(spy).add(Matchers.any(Object.class));
	}

	@Test
	public void testNotMatcher() {
		Comparator mock = Mockachino.mock(Comparator.class);
		mock.compare("Hello", "World");
		mock.compare("Foo", "Bar");
		mock.compare("Foo", null);

		Mockachino.verifyOnce().on(mock).compare(Matchers.not(Matchers.typeM(List.class)), "World");
	}

	@Test
	public void testCustomMatcher() {
		Comparator mock = Mockachino.mock(Comparator.class);
		mock.compare("Hello", "World");
		mock.compare("Foo", "Bar");
		mock.compare("Foo", null);

		BasicMatcher<Object> myFooBarMatcher = new BasicMatcher<Object>() {
			@Override
			public boolean matches(Object value) {
				return "Foo".equals(value) || "Bar".equals(value);
			}

			@Override
			public Class<Object> getType() {
				return Object.class;
			}

			@Override
			protected String asString() {
				return "(Foo or Bar)";
			}
		};
		Mockachino.verifyOnce().on(mock).compare(Matchers.match(myFooBarMatcher), Matchers.match(myFooBarMatcher));
	}

}
