package se.mockachino;

import org.junit.Test;
import se.mockachino.matchers.Matchers;

public class VarargsTest {
	static interface Varargs {
		void foo(int a, int b, String... strings);
	}

	@Test
	public void testVarargs() {
		Varargs mock = Mockachino.mock(Varargs.class);
		mock.foo(123, 456, "Hello", "World");

		Mockachino.verifyOnce().on(mock).foo(123, 456, "Hello", "World");
	}

	@Test
	public void testVarargs2() {
		Varargs mock = Mockachino.mock(Varargs.class);
		mock.foo(123, 456, "Hello", "World");

		Mockachino.verifyOnce().on(mock).foo(123, 456, "Hello", Matchers.same("World"));
	}

}