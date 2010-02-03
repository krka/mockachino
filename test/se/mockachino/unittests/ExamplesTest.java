package se.mockachino.unittests;

import org.junit.Test;
import se.mockachino.Mockachino;
import se.mockachino.matchers.Matchers;

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
}
