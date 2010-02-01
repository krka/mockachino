package se.mockachino;

import org.junit.Test;
import se.mockachino.exceptions.VerificationError;
import se.mockachino.matchers.Matchers;

import java.util.List;

import static org.junit.Assert.fail;

public class ErrorMessageTest {
	@Test
	public void testSimpleVerify() {
		try {
			List mock = Mockachino.mock(List.class);
			mock.get(1);
			mock.get(2);
			mock.get(3);

			Mockachino.verifyAtLeast(mock, 2).get(3);
			fail("Should not succeed");
		} catch (VerificationError e) {
		}
	}

	@Test
	public void testSimpleVerify2() {
		try {
			List mock = Mockachino.mock(List.class);
			mock.add("Hello");
			mock.add(2);
			mock.add("World");

			Mockachino.verifyAtLeast(mock, 2).add(Matchers.anyInt());
			fail("Should not succeed");
		} catch (VerificationError e) {
		}
	}

	// TODO: test and improve verifyInOrder error message
	// TODO: test and improve "only interfaces can be mocked" error
	// TODO: test and improve argument is not a mocked object
	// TODO: test and improve illegal matcher usage error
}
