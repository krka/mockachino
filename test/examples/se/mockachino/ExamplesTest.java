package se.mockachino;

import org.junit.Test;
import se.mockachino.exceptions.VerificationError;
import se.mockachino.matchers.matcher.BasicMatcher;
import se.mockachino.order.OrderingContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static se.mockachino.Mockachino.*;

public class ExamplesTest {

	@Test
	public void testExample() {
		// Create a mock
		List mock = mock(List.class);

		// Interact with it
		mock.get(123);
		mock.get(456);

		verifyOnce().on(mock).get(123);
		verifyOnce().on(mock).get(456);

		// Verify that some interactions never occured
		verifyNever().on(mock).get(124);
	}

	@Test
	public void testExampleStub() {
		// Create a mock
		List mock = mock(List.class);

		// Stub a return value
		stubReturn("Hello").on(mock).get(123);

		// Interact with it
		Object hello = mock.get(123);
		Object shouldBeNull = mock.get(456);
		assertEquals("Hello", hello);
		assertEquals(null, shouldBeNull);

		// Verify calls
		verifyOnce().on(mock).get(123);
		verifyOnce().on(mock).get(456);
	}

	@Test
	public void testExampleWithMatchers() {
		// Create a mock
		List mock = mock(List.class);

		mock.add("Hello");
		mock.add("World");

		// Verify calls
		verifyOnce().on(mock).add(eq("Hello"));
		verifyExactly(2).on(mock).add(any(String.class));
	}

	@Test
	public void testExampleStubOverride() {
		// Create a mock
		List mock = mock(List.class);

		// Stub a return value
		stubReturn("NullString").on(mock).get(anyInt());
		stubReturn("Hello").on(mock).get(123);

		// Interact with it
		assertEquals("Hello", mock.get(123));
		assertEquals("NullString", mock.get(1));
		assertEquals("NullString", mock.get(2));

		// Verify calls
		verifyExactly(3).on(mock).get(anyInt());
	}

	@Test
	public void testBadMatcherCombo() {
		// Create a mock
		List mock = mock(List.class);

		// This works
		verifyNever().on(mock).add(eq(0), any(Object.class));

		// This doesn't
		try {
			verifyNever().on(mock).add(0, any(Object.class));
			fail("This should never pass");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSimpleVerify() {
		try {
			List mock = mock(List.class);
			mock.get(1);
			mock.get(2);
			mock.get(3);

			verifyAtLeast(2).on(mock).get(3);
			fail("This should never pass");
		} catch (VerificationError e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testInOrder() {
		List mock = mock(List.class);
		mock.add("Hello");
		mock.remove("Hello");
		mock.add("World");
		mock.contains("Hello");
		mock.add("Bar");

		OrderingContext order = newOrdering();
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
		List spy = mock(List.class, Settings.spyOn(myList));

		// Show that calls to the spy go to the real object
		assertEquals(1, spy.size());
		assertEquals("Real object", spy.get(0));

		// Overwrite spy.get(0)
		stubReturn("Fake object").on(spy).get(0);
		spy.add("Real object 2");

		// Show that the overwrite worked,
		// but other calls still go through the real object
		assertEquals(2, spy.size());
		assertEquals("Fake object", spy.get(0));
		assertEquals("Real object 2", spy.get(1));

		// Verifying the calls still work of course
		verifyExactly(2).on(spy).size();
		verifyExactly(3).on(spy).get(anyInt());
		verifyExactly(1).on(spy).add(any(Object.class));
	}

	@Test
	public void testNotMatcher() {
		Comparator mock = mock(Comparator.class);
		mock.compare("Hello", "World");
		mock.compare("Foo", "Bar");
		mock.compare("Foo", null);

		verifyOnce().on(mock).compare(not(mType(List.class)), "World");
	}

	@Test
	public void testCustomMatcher() {
		Comparator mock = mock(Comparator.class);
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
		verifyOnce().on(mock).compare(match(myFooBarMatcher), match(myFooBarMatcher));
	}

}
