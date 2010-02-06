package se.mockachino.verifier;

import org.junit.Test;
import se.mockachino.Mockachino;
import se.mockachino.exceptions.VerificationError;

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
}
