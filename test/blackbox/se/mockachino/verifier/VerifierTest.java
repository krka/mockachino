package se.mockachino.verifier;

import org.junit.Test;
import se.mockachino.CallHandler;
import se.mockachino.MethodCall;
import se.mockachino.Mockachino;
import se.mockachino.exceptions.VerificationError;

import java.util.ArrayList;
import java.util.List;

public class VerifierTest {
	@Test
	public void testSimple() {
		List mock = Mockachino.mock(List.class);
		mock.add("Foo");
		Mockachino.verifyAtLeast(0).on(mock).add("Foo");
		Mockachino.verifyAtLeast(1).on(mock).add("Foo");
		Mockachino.verifyExactly(1).on(mock).add("Foo");
		Mockachino.verifyRange(1, 1).on(mock).add("Foo");
	}

	@Test(expected = VerificationError.class)
	public void testTooFew() {
		List mock = Mockachino.mock(List.class);
		mock.add("Foo");
		Mockachino.verifyAtLeast(2).on(mock).add("Foo");
	}

	@Test(expected = VerificationError.class)
	public void testTooFew2() {
		List mock = Mockachino.mock(List.class);
		mock.add("Foo");
		Mockachino.verifyExactly(2).on(mock).add("Foo");
	}

	@Test(expected = VerificationError.class)
	public void testTooMany() {
		List mock = Mockachino.mock(List.class);
		mock.add("Foo");
		mock.add("Foo");
		mock.add("Foo");
		Mockachino.verifyAtMost(2).on(mock).add("Foo");

	}

	@Test(expected = VerificationError.class)
	public void testTooMany2() {
		List mock = Mockachino.mock(List.class);
		mock.add("Foo");
		mock.add("Foo");
		mock.add("Foo");
		Mockachino.verifyExactly(2).on(mock).add("Foo");

	}

	@Test
	public void testPrettyStack() {
		final List mock1 = Mockachino.mock(ArrayList.class);
		final List mock2 = Mockachino.mock(ArrayList.class);
		Mockachino.stubAnswer(new CallHandler() {
			@Override
			public Object invoke(Object obj, MethodCall call) throws Throwable {
				return mock1.add("Foo");
			}
		}).on(mock2).remove(1);

		mock2.remove(1);
		Mockachino.verifyExactly(0).on(mock1).add("Bar");
		Mockachino.verifyExactly(1).on(mock1).add("Foo");
	}
}
