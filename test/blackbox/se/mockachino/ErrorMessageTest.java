package se.mockachino;

import org.junit.Test;
import se.mockachino.exceptions.VerificationError;
import se.mockachino.order.OrderingContext;

import java.util.List;

import static org.junit.Assert.fail;
import static se.mockachino.Mockachino.*;
import static se.mockachino.matchers.Matchers.anyInt;

public class ErrorMessageTest {
	@Test
	public void testSimpleVerify2() {
		try {
			List mock = mock(List.class);
			mock.add("Hello");
			mock.add(2);
			mock.add("World");

			verifyAtLeast(2).on(mock).add(anyInt());
			fail("Should not succeed");
		} catch (VerificationError e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testVerifyInOrder() {
		try {
			List mock = mock(List.class);
			mock.add("Hello");
			mock.add(2);
			mock.add("World");

			OrderingContext order = newOrdering();
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
			List mock = mock(List.class);
			mock.add("Hello");
			mock.add(2);
			mock.add("World");

			OrderingContext order = newOrdering();
			order.verify().on(mock).add("World");
			order.verify().on(mock).add("Hello2");
			fail("Should not succeed");
		} catch (VerificationError e) {
			e.printStackTrace();
		}
	}
}
