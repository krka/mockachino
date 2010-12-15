package se.mockachino.order;

import org.junit.Test;
import se.mockachino.Invocation;
import se.mockachino.Mockachino;
import se.mockachino.exceptions.VerificationError;

import java.util.List;

import static org.junit.Assert.fail;
import static se.mockachino.Mockachino.*;

public class MockPointTest {
	@Test
	public void testSimpleMockPoint() {
		List<String> mock = mock(List.class);

		mock.add("A");
		mock.add("B");
		mock.add("C");

		OrderingContext context = newOrdering();

		context.verify().on(mock).add("A");
		MockPoint first = context.afterLastCall();

		context.verify().on(mock).add("C");
		MockPoint last = context.beforeLastCall();

		between(first, last).verifyNever().on(mock).add("A");

		between(first, last).verifyOnce().on(mock).add("B");
		try {
			between(first, last).verifyNever().on(mock).add("B");
			fail("Should fail");
		} catch (VerificationError e) {
			e.printStackTrace();
		}

		between(first, last).verifyNever().on(mock).add("C");
	}

	@Test
	public void testVerificationFail() {
		MockPoint first = Mockachino.newOrdering().afterLastCall();
		MockPoint last = Mockachino.newOrdering().afterLastCall();

		List<String> mock = mock(List.class);
		mock.add("A");
		System.out.println(first);
		System.out.println(last);
		for (Invocation inv : Mockachino.getData(mock).getCalls(first, last)) {
			System.out.println(inv.getCallNumber() + ": " + inv);
		}
		try {
			between(first, last).verifyAtLeast(1).on(mock).add("A");
			fail("Should fail");
		} catch (VerificationError e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testMockPoint() {
		List mock = mock(List.class);

		mock.add("B");
		mock.add("A");
		mock.add("B");
		mock.add("C");
		mock.add("B");
		mock.add("D");

		OrderingContext context = newOrdering();

		context.verify().on(mock).add("A");
		MockPoint first = context.atLastCall();

		context.verify().on(mock).add("C");
		MockPoint last = context.atLastCall();

		Mockachino.between(first, last).verifyOnce().on(mock).add("A");
		Mockachino.between(first, last).verifyOnce().on(mock).add("B");
		Mockachino.between(first, last).verifyOnce().on(mock).add("C");
		Mockachino.between(first, last).verifyNever().on(mock).add("D");
	}

	@Test
	public void testMockPoint2() {
		List mock = mock(List.class);

		mock.add("B");
		mock.add("A");
		mock.add("B");
		mock.add("C");
		mock.add("B");
		mock.add("D");

		OrderingContext context = newOrdering();

		context.verify().on(mock).add("A");
		MockPoint first = context.afterLastCall();

		context.verify().on(mock).add("C");
		MockPoint last = context.beforeLastCall();

		Mockachino.between(first, last).verifyNever().on(mock).add("A");
		Mockachino.between(first, last).verifyOnce().on(mock).add("B");
		Mockachino.between(first, last).verifyNever().on(mock).add("C");
	}


	@Test
	public void testAtMost() {
		List mock = mock(List.class);
		mock.add(100);
		mock.add(200);
		mock.add(100);

		// Get the points
		OrderingContext ordering = newOrdering();
		MockPoint p1 = ordering.atLastCall();
		ordering.verifyAtLeast(1).on(mock).add(200);
		MockPoint p2 = ordering.beforeLastCall();

		between(p1, p2).verifyAtMost(1).on(mock).add(100);
	}
}
