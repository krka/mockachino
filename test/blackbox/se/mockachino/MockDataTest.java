package se.mockachino;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static se.mockachino.Mockachino.stubAnswer;

public class MockDataTest {
	static class Prot {
		protected void foo() {
			System.out.println("Hello world");
		}
	}

	@Test
	public void testProtected() {
		final AtomicInteger count = new AtomicInteger();
		Prot mock = Mockachino.mock(Prot.class);
		stubAnswer(new CallHandler() {
			@Override
			public Object invoke(Object obj, MethodCall call) throws Throwable {
				count.incrementAndGet();
				return null;
			}
		}).on(mock).foo();
		mock.foo();
		assertEquals(1, count.get());

	}
}
