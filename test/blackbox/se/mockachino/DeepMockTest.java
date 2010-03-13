package se.mockachino;

import org.junit.Test;
import se.mockachino.order.OrderingContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static se.mockachino.Mockachino.DEEP_MOCK;
import static se.mockachino.Mockachino.mock;
import static se.mockachino.Mockachino.newOrdering;
import static se.mockachino.Mockachino.verifyExactly;

public class DeepMockTest {

	interface Foo {
		Foo getFoo(int value);
	}

	@Test
	public void testStubbing() {
		Foo mock = mock(Foo.class, Settings.fallback(DEEP_MOCK));

		System.out.println("mock: " + mock);
		Foo mock2 = mock.getFoo(0);
		System.out.println("mock.getFoo(): " + mock2);

		assertNotNull(mock2);
		Foo mock3 = mock.getFoo(0);
		System.out.println("mock.getFoo(): " + mock3);
		assertTrue(mock2 == mock3);

		Foo mock4 = mock2.getFoo(0);
		System.out.println("mock.getFoo().getFoo(): " + mock4);
		assertNotNull(mock4);

		assertFalse(mock3 == mock4);

		Foo mock5 = mock3.getFoo(0);
		System.out.println("mock.getFoo().getFoo(): " + mock5);
		assertNotNull(mock5);

		assertTrue(mock4 == mock5);
	}

	@Test
	public void testVerify() {
		Foo mock = mock(Foo.class, Settings.fallback(DEEP_MOCK));
		mock.getFoo(1).getFoo(2).getFoo(3);

		verifyExactly(1).on(mock).getFoo(1);
		verifyExactly(0).on(mock).getFoo(2);
		verifyExactly(0).on(mock).getFoo(3);

		verifyExactly(0).on(mock.getFoo(1)).getFoo(1);
		verifyExactly(1).on(mock.getFoo(1)).getFoo(2);
		verifyExactly(0).on(mock.getFoo(1)).getFoo(3);

		verifyExactly(0).on(mock.getFoo(1).getFoo(2)).getFoo(1);
		verifyExactly(0).on(mock.getFoo(1).getFoo(2)).getFoo(2);
		verifyExactly(1).on(mock.getFoo(1).getFoo(2)).getFoo(3);
	}

	@Test
	public void testVerifyInOrder() {
		Foo mock = mock(Foo.class, Settings.fallback(DEEP_MOCK));
		mock.getFoo(1).getFoo(2).getFoo(3);

		OrderingContext orderingContext = newOrdering();
		orderingContext.verifyAtLeast(1).on(mock).getFoo(1);
		orderingContext.verifyAtLeast(1).on(mock.getFoo(1)).getFoo(2);
		orderingContext.verifyAtLeast(1).on(mock.getFoo(1).getFoo(2)).getFoo(3);
	}
}
