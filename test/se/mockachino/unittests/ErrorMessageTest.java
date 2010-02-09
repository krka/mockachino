package se.mockachino.unittests;

import org.junit.Test;
import se.mockachino.Mockachino;
import se.mockachino.exceptions.VerificationError;
import se.mockachino.matchers.Matchers;
import se.mockachino.order.OrderingContext;

import java.util.List;

import static org.junit.Assert.fail;

public class ErrorMessageTest {
	@Test
	public void testSimpleVerify2() {
		try {
			List mock = Mockachino.mock(List.class);
			mock.add("Hello");
			mock.add(2);
			mock.add("World");

			Mockachino.verifyAtLeast(2).on(mock).add(Matchers.anyInt());
			fail("Should not succeed");
		} catch (VerificationError e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testVerifyInOrder() {
		try {
			List mock = Mockachino.mock(List.class);
			mock.add("Hello");
			mock.add(2);
			mock.add("World");

			OrderingContext order = Mockachino.verifyOrder();
			order.verify().on(mock).add("World");
			order.verify().on(mock).add("Hello");
			fail("Should not succeed");
		} catch (VerificationError e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testVerifyInOrder2() {
		try {
			List mock = Mockachino.mock(List.class);
			mock.add("Hello");
			mock.add(2);
			mock.add("World");

			OrderingContext order = Mockachino.verifyOrder();
			order.verify().on(mock).add("World");
			order.verify().on(mock).add("Hello2");
			fail("Should not succeed");
		} catch (VerificationError e) {
			e.printStackTrace();
		}
	}

	// TODO: test and improve verifyInOrder error message
	// TODO: test and improve "only interfaces can be mocked" error
	// TODO: test and improve argument is not a mocked object
	// TODO: test and improve illegal match usage error
}
