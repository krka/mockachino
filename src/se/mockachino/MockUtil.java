package se.mockachino;

import com.googlecode.gentyref.GenericTypeReflector;
import se.mockachino.exceptions.UsageError;
import se.mockachino.matchers.MatcherThreadHandler;
import se.mockachino.mock.MockHandler;
import se.mockachino.proxy.ProxyUtil;
import se.mockachino.spy.SpyHandler;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A mock context is a context for mocking, completely independent from any other context,
 * including the default one.
 * <p/>
 * Create new contexts when you need truly independent tests.
 */
public class MockUtil {
	private static final AtomicInteger nextSequenceNumber = new AtomicInteger();
	private static final AtomicInteger nextMockId = new AtomicInteger();

	public static Object mockType(Type type, MockSettings settings) {
		checkNull("type", type);
		checkNull("settings", settings);
		return mockType(type, settings.getFallback(), settings.isQuick(), settings.getName(), settings.getExtraInterfaces());
	}

	private static Object mockType(Type type, CallHandler fallback, boolean quick, String name, Set<Class<?>> extraInterfaces) {
		checkNull("type", type);
		checkNull("fallback", fallback);
		final Class<?> clazz = GenericTypeReflector.erase(type);
		if (name == null) {
			name = getDefaultName(clazz, fallback);
		}
		MatcherThreadHandler.assertEmpty();
		MockHandler mockHandler = new MockHandler(
				fallback, new MockData(clazz, type, extraInterfaces, name), quick, name);
		return ProxyUtil.newProxy(clazz, mockHandler, extraInterfaces);
	}

	static void checkNull(String name, Object obj) {
		if (obj == null) {
			throw new UsageError(name + " can not be null");
		}

	}

	private static <T> String getDefaultName(Class<T> clazz, CallHandler fallback) {
		String kind = "Mock";
		if (fallback instanceof SpyHandler) {
			kind = "Spy";
		}
		return kind + ":" + clazz.getSimpleName() + ":" + nextMockId();
	}

	static String nextMockId() {
		return Integer.toString(nextMockId.incrementAndGet());
	}

	/**
	 * Increments the sequence of method calls.
	 * This is not relevant for end users.
	 *
	 * @return the next sequence number.
	 */
	static int incrementSequence() {
		return nextSequenceNumber.incrementAndGet();
	}

	static int getSequence() {
		return nextSequenceNumber.get();
	}
}