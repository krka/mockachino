package se.mockachino;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static se.mockachino.Mockachino.*;
import static se.mockachino.matchers.Matchers.same;
import static se.mockachino.matchers.Matchers.type;

public class EqualsHashcodeTest {
	@Test
	public void testMockEquals() {
		List mock = mock(List.class);
		stubReturn(true).on(mock).equals("Hello");

		assertTrue(mock.equals("Hello"));
		assertFalse(mock.equals("World"));

		verifyExactly(1).on(mock).equals("Hello");
		verifyExactly(1).on(mock).equals("World");
		verifyExactly(2).on(mock).equals(type(String.class));
	}

	@Test
	public void testMockEqualsWithClass() {
		ArrayList mock = mock(ArrayList.class);
		stubReturn(true).on(mock).equals("Hello");

		assertTrue(mock.equals("Hello"));
		assertFalse(mock.equals("World"));

		verifyExactly(1).on(mock).equals("Hello");
		verifyExactly(1).on(mock).equals("World");
		verifyExactly(2).on(mock).equals(type(String.class));
	}

	@Test
	public void testMockHashcode() {
		List mock = mock(List.class);
		stubReturn(123).on(mock).hashCode();

		assertEquals(mock.hashCode(), 123);

		verifyExactly(1).on(mock).hashCode();
	}

	@Test
	public void testMockHashcodeWithClass() {
		ArrayList mock = mock(ArrayList.class);
		stubReturn(true).on(mock).equals("Hello");

		assertTrue(mock.equals("Hello"));
		assertFalse(mock.equals("World"));

		verifyExactly(1).on(mock).equals("Hello");
		verifyExactly(1).on(mock).equals("World");
		verifyExactly(2).on(mock).equals(type(String.class));
	}

	@Test
	public void testTostring() {
		MockContext context = new MockContext();
		List mock = context.mock(List.class);

		assertEquals("Mock:List:1", mock.toString());
		Mockachino.stubReturn("MyName").on(mock).toString();
		assertEquals("MyName", mock.toString());
		MockData<Object> data = Mockachino.getData((Object) mock);
		data.resetStubs();
		Mockachino.stubReturn(System.identityHashCode(mock)).on((Object) mock).hashCode();
		Mockachino.stubReturn(true).on((Object) mock).equals(same((Object) mock));
		assertEquals("Mock:List:1", mock.toString());
	}

}
