package se.mockachino.concurrencytests;

import org.junit.Test;
import se.mockachino.MethodCall;
import se.mockachino.Mockachino;
import se.mockachino.matchers.Matchers;
import se.mockachino.order.InOrder;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ThreadsafetyTest {
	private static interface TestInterface {
		int foo(String threadName, int count);
	}

	@Test
	public void testParallellMocks() throws InterruptedException {
		final int numThreads = 100;
		final long TIMEOUT = 20000;

		Thread[] threads = new Thread[numThreads];
		for (int i = 0; i < numThreads; i++) {
			final String name = "Thread:" + i;
			Thread t = new Thread(name) {
				@Override
				public void run() {
					long t1 = System.currentTimeMillis();
					int count = 0;
					while (System.currentTimeMillis() - t1 < TIMEOUT) {
						TestInterface mock = Mockachino.mock(TestInterface.class);
						count++;
						Mockachino.stubReturn(count).on(mock).foo(name, 123);
						Mockachino.verifyNever().on(mock).foo(name, 123);
						assertEquals(count, mock.foo(name, 123));
						Mockachino.verifyOnce().on(mock).foo(name, 123);
					}
					System.out.println(name + " did " + count + " iterations");
				}
			};
			threads[i] = t;
			t.start();
		}

		for (int i = 0; i < numThreads; i++) {
			threads[i].join();
		}
	}

	@Test
	public void testSharedMock() throws InterruptedException {
		final int numThreads = 100;
		final long TIMEOUT = 20000;

		final TestInterface mock = Mockachino.mock(TestInterface.class);

		Thread[] threads = new Thread[numThreads];
		final int[] counts = new int[numThreads];
		for (int i = 0; i < numThreads; i++) {
			final String name = "Thread:" + i;
			final int finalI = i;
			Thread t = new Thread(name) {
				@Override
				public void run() {
					long t1 = System.currentTimeMillis();
					int count = 0;
					while (System.currentTimeMillis() - t1 < TIMEOUT) {
						count++;
						Mockachino.stubReturn(count).on(mock).foo(name, count);
						Mockachino.verifyNever().on(mock).foo(name, count);
						assertEquals(count, mock.foo(name, count));
						Mockachino.verifyOnce().on(mock).foo(name, count);
					}
					counts[finalI] = count;
					System.out.println(name + " did " + count + " iterations");
				}
			};
			threads[i] = t;
			t.start();
		}

		for (int i = 0; i < numThreads; i++) {
			threads[i].join();
		}
		for (int i = 0; i < numThreads; i++) {
			final String name = "Thread:" + i;
			int count = counts[i];
			InOrder order = Mockachino.verifyOrder();
			for (int j = 0; j < count; j++) {
				order.verify().on(mock).foo(name, 1 + j);
			}
			Mockachino.verifyExactly(count).on(mock).foo(name, Matchers.anyInt());
		}
	}
}
