package se.mockachino.blackbox;

import org.junit.Test;
import se.mockachino.CallHandler;
import se.mockachino.MethodCall;
import se.mockachino.Mockachino;
import se.mockachino.exceptions.UsageError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static se.mockachino.matchers.Matchers.any;

public class MethodListenerTest {
	@Test
	public void testSimple() {
		List mock = Mockachino.mock(List.class);

		final List<String> list = new ArrayList<String>();
		CallHandler listener = new CallHandler() {
			@Override
			public Object invoke(Object obj, MethodCall call) {
				list.add(call.toString());
				return null;
			}
		};
		Mockachino.observeWith(listener).on(mock).get(123);

		mock.get(123);
		mock.get(124);
		assertEquals(1, list.size());
		assertEquals("get(123)", list.get(0));
	}

	interface Foo {
		Collection<Integer> bar(String s);
	}

	@Test
	public void withNullArguments() throws Exception {
		Foo mock = Mockachino.mock(Foo.class);
		final AtomicInteger counter = new AtomicInteger();
		Mockachino.observeWith(new CallHandler() {
			@Override
			public Object invoke(Object obj, MethodCall call) throws Throwable {
				counter.incrementAndGet();
				return null;
			}
		}).on(mock).bar(any(String.class));
		assertEquals(0, counter.get());
		mock.bar("");
		assertEquals(1, counter.get());
	}

	@Test(expected = UsageError.class)
	public void invalidObserverUsage() throws Exception {
		Foo mock = Mockachino.mock(Foo.class);
		Mockachino.observeWith(new CallHandler() {
			@Override
			public Object invoke(Object obj, MethodCall call) throws Throwable {
				return null;
			}

		}).on(mock).bar(any(String.class)).add(123);
	}
}