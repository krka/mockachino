package se.mockachino.blackbox;

import org.junit.Test;
import se.mockachino.MockData;
import se.mockachino.Mockachino;

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
		List mock = Mockachino.mock(List.class);

		String mockName = mock.toString();
		assertTrue(mockName, mockName.matches("^Mock:List:(\\d+)$"));
		Mockachino.stubReturn("MyName").on(mock).toString();
		assertEquals("MyName", mock.toString());
		MockData<Object> data = Mockachino.getData((Object) mock);
		data.resetStubs();
		Mockachino.stubReturn(System.identityHashCode(mock)).on((Object) mock).hashCode();
		Mockachino.stubReturn(true).on((Object) mock).equals(same((Object) mock));
		assertEquals(mockName, mock.toString());
	}

  @Test
 	public void equalsShouldWorkOnSpies() throws Exception {
 		Foo foo = new Foo("foo");
 		Foo fooSpy1 = spy(foo);
 		Foo fooSpy2 = spy(foo);

    assertEquals(fooSpy1, fooSpy2);

 	}

 	private static class Foo {
 		private String name;

 		private Foo(String name) {
 			this.name = name;
 		}

     @Override
     public boolean equals(Object o) {
       if (this == o) return true;
       if (!(o instanceof Foo)) return false;

       Foo foo = (Foo) o;

       if (!name.equals(foo.getName())) return false;

       return true;
     }

     @Override
     public int hashCode() {
       return name.hashCode();
     }

     public String getName() {
       return name;
     }
   }
}
