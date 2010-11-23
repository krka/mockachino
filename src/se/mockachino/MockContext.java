package se.mockachino;

import com.googlecode.gentyref.GenericTypeReflector;
import com.googlecode.gentyref.TypeToken;
import se.mockachino.annotations.Mock;
import se.mockachino.annotations.Spy;
import se.mockachino.exceptions.UsageError;
import se.mockachino.invocationhandler.CollectionsHandler;
import se.mockachino.invocationhandler.DeepMockHandler;
import se.mockachino.invocationhandler.PrimitiveInvocationHandler;
import se.mockachino.matchers.MatcherThreadHandler;
import se.mockachino.mock.MockHandler;
import se.mockachino.observer.ObserverAdder;
import se.mockachino.order.BetweenVerifyContext;
import se.mockachino.order.MockPoint;
import se.mockachino.order.OrderingContext;
import se.mockachino.proxy.ProxyUtil;
import se.mockachino.spy.SpyHandler;
import se.mockachino.verifier.VerifyRangeStart;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A mock context is a context for mocking, completely independent from any other context,
 * including the default one.
 *
 * Create new contexts when you need truly independent tests.
 *
 */
public class MockContext {
    public static final PrimitiveInvocationHandler PRIMITIVE_VALUES = new PrimitiveInvocationHandler();
    public static final CallHandler DEFAULT_VALUES = new CollectionsHandler(PRIMITIVE_VALUES);
	public final CallHandler DEEP_MOCK = new DeepMockHandler(this, DEFAULT_VALUES);

	public final MockPoint BIG_BANG = new MockPoint(this, 0);
	public final MockPoint BIG_CRUNCH = new MockPoint(this, Integer.MAX_VALUE);

	private final AtomicInteger nextSequenceNumber = new AtomicInteger();
	private final AtomicInteger nextMockId = new AtomicInteger();

	/**
	 * Creates a new mock with a default handler and default settings.
	 *
	 * @param clazz the interface or class to mock
	 * @return a mock object of the same class
	 */
	public <T> T mock(Class<T> clazz) {
		checkNull("clazz", clazz);
		MockSettings mockSettings = Settings.newSettings();
		return (T) mockType(clazz, mockSettings);
	}

	/**
	 * Creates a new mock with a default handler and default settings.
	 *
	 * @param type the type literal of the interface or class to mock
	 * @return a mock object of the same class
	 */
    public <T> T mock(TypeToken<T> type) {
		checkNull("type", type);
		MockSettings mockSettings = Settings.newSettings();
		return mock(type, mockSettings);
	}

    /**
     * Creates a new mock with specified settings.
     *
     * @param typeToken the typeToken literal of the interface or class to mock
     * @return a mock object of the same class
     */
    public <T> T mock(TypeToken<T> typeToken, MockSettings settings) {
        checkNull("typeToken", typeToken);
        checkNull("settings", settings);
        final Type type = typeToken.getType();
        return (T) mockType(type, settings);
    }

    /**
     * Creates a new mock with specified settings.
     *
     * @param clazz the interface or class to mock
     * @return a mock object of the same class
     */
    public <T> T mock(Class<T> clazz, MockSettings settings) {
        checkNull("clazz", clazz);
        return (T) mockType(clazz, settings);
    }

    public Object mockType(Type type, MockSettings settings) {
        checkNull("type", type);
        checkNull("settings", settings);
        return mockType(type, settings.getFallback(), settings.isQuick(), settings.getName(), settings.getExtraInterfaces());
    }

    /**
	 * Creates a new mock with class impl.getClass() with the impl as a default handler.
	 * Equivalent to mock(impl.getClass(), Settings.spyOn(impl));
	 *
	 * @param impl the object to spy on
	 * @return a mock object of the same class as impl
	 */
	public <T> T spy(T impl) {
		checkNull("impl", impl);
		return (T) mockType((Class<T>)impl.getClass(), Settings.spyOn(impl));
	}

	/**
	 * Creates a new mock with class impl.getClass() with the impl as a default handler,
	 * and the specified settings.
	 *
	 * Equivalent to mock(impl.getClass(), settings.spyOn(impl));
	 *
	 * @param impl the object to spy on
	 * @return a mock object of the same class as impl
	 */
	public <T> T spy(T impl, MockSettings settings) {
		checkNull("impl", impl);
		checkNull("settings", settings);
		return (T) mockType((Class<T>)impl.getClass(), settings.spyOn(impl));
	}

	private Object mockType(Type type, CallHandler fallback, boolean quick, String name, Set<Class<?>> extraInterfaces) {
		checkNull("type", type);
		checkNull("fallback", fallback);
        final Class<?> clazz = GenericTypeReflector.erase(type);
		if (name == null) {
            name = getDefaultName(clazz, fallback);
		}
		MatcherThreadHandler.assertEmpty();
		MockHandler mockHandler = new MockHandler(
				this, fallback, new MockData(this, clazz, type, extraInterfaces), quick, name);
		return ProxyUtil.newProxy(clazz, mockHandler, extraInterfaces);
	}

	private <T> String getDefaultName(Class<T> clazz, CallHandler fallback) {
		String kind = "Mock";
		if (fallback instanceof SpyHandler) {
			kind = "Spy";
		}
		return kind + ":" + clazz.getSimpleName() + ":" + nextMockId();
	}

	static void checkNull(String name, Object obj) {
		if (obj == null) {
			throw new UsageError(name + " can not be null");
		}

	}

	private String nextMockId() {
		return Integer.toString(nextMockId.incrementAndGet());
	}

	/**
	 * Creates a new ordering context which is used to verify method calls in order.
	 * Ordering contexts are completely independent of each other.
	 *
	 * Typical usage:
	 * <pre>
	 * OrderingContext context = Mockachino.newOrdering();
	 * context.verifyAtLeast(3).on(mock).method();
	 * </pre>
	 *
	 * @return the new ordering context
	 */
	public OrderingContext newOrdering() {
		MatcherThreadHandler.assertEmpty();
		return new OrderingContext(this, BIG_BANG, BIG_CRUNCH);
	}

	/**
	 * Verifies that a method call is called between min and max times, inclusive.
	 *
	 * Typical usage:
	 * <pre>
	 * Mockachino.verifyRange(1, 2).on(mock).method();
	 * </pre>
	 *
	 * @param min minimum amount of matching method calls
	 * @param max maximum amount of matching method calls
	 * @return a verifier
	 */
	public VerifyRangeStart verifyRange(int min, int max) {
		MatcherThreadHandler.assertEmpty();
		return new VerifyRangeStart(this, min, max);
	}

	/**
	 * Verifies that a method call is called an exact number of times.
	 *
	 * Typical usage:
	 * <pre>
	 * Mockachino.verifyExactly(3).on(mock).method();
	 * </pre>
	 *
	 * @param count number of times the method should be called
	 * @return a verifier
	 */
	public VerifyRangeStart verifyExactly(int count) {
		return verifyRange(count, count);
	}

	/**
	 * Verifies that a method call is never called.
	 *
	 * Typical usage:
	 * <pre>
	 * Mockachino.verifyNever().on(mock).method();
	 * </pre>
	 *
	 * @return a verifier
	 */
	public VerifyRangeStart verifyNever() {
		return verifyExactly(0);
	}

	/**
	 * Verifies that a method call is only called exactly once.
	 *
	 * Typical usage:
	 * <pre>
	 * Mockachino.verifyOnce().on(mock).method();
	 * </pre>
	 *
	 * @return a verifier
	 */
	public VerifyRangeStart verifyOnce() {
		return verifyExactly(1);
	}

	/**
	 * Verifies that a method call is called at least a specific number of times.
	 *
	 * Typical usage:
	 * <pre>
	 * Mockachino.verifyAtLeast(3).on(mock).method();
	 * </pre>
	 *
	 * @param min number of times the method should be called
	 * @return a verifier
	 */
	public VerifyRangeStart verifyAtLeast(int min) {
		return verifyRange(min, Integer.MAX_VALUE);
	}

	/**
	 * Verifies that a method call is called at most a specific number of times.
	 *
	 * Typical usage:
	 * <pre>
	 * Mockachino.verifyAtMost(3).on(mock).method();
	 * </pre>
	 *
	 * @param max number of times the method should be called
	 * @return a verifier
	 */
	public VerifyRangeStart verifyAtMost(int max) {
		return verifyRange(0, max);
	}
	
	/**
	 * Generic method for creating a proxy for a mock object.
	 *
	 * Probably not interesting for regular users of Mockachino
	 *
	 */
	public static <T> T createProxy(T mock, InvocationHandler handler) {
		MatcherThreadHandler.assertEmpty();
		MockData data = Mockachino.getData(mock);
		Class<T> iface = data.getInterface();
		return ProxyUtil.newProxy(iface, handler, (Set<Class<?>>) data.getExtraInterfaces());
	}

	/**
	 * Adds an observer to a specific method call.
	 * The observer will get a callback every time the method is called and the arguments match.
	 *
	 * Typical usage:
	 * <pre>
	 * Mockachino.observeWith(observer).on(mock).method();
	 * </pre>
	 *
	 * @param observer the observer to use
	 * @return
	 */
	public ObserverAdder observeWith(CallHandler observer) {
		checkNull("observer", observer);
		MatcherThreadHandler.assertEmpty();
		return new ObserverAdder(this, observer);
	}

    /**
	 * Increments the sequence of method calls.
	 * This is not relevant for end users.
	 * @return the next sequence number.
	 */
	public int incrementSequence() {
		return nextSequenceNumber.incrementAndGet();
	}

	/**
	 * Gets the current point on time on the call history.
	 * It can be useful to run between calls to the code under test,
	 * to later verify that calls happened during
	 * specific times.
	 *
	 * @return the mock point
	 */
	public MockPoint getCurrentPoint() {
		MatcherThreadHandler.assertEmpty();
		return new MockPoint(this, nextSequenceNumber.get());
	}

	/**
	 * Verify that calls happened between (inclusive) two points in time.
	 *
	 * Typical usage:
	 * <pre>
	 * Mockachino.between(start, end).verifyAtLeast(1).on(mock).method();
	 * </pre>
	 *
	 *
	 * @param start
	 * @param end
	 * @return a verifying context
	 */
	public BetweenVerifyContext between(MockPoint start, MockPoint end) {
		assertMockPoint(start, "start");
		assertMockPoint(end, "end");
		MatcherThreadHandler.assertEmpty();
		return new BetweenVerifyContext(this, start, end);
	}

	/**
	 * Verify that calls happened at or after a point in time.
	 *
	 * Typical usage:
	 * <pre>
	 * Mockachino.after(start).verifyAtLeast(1).on(mock).method();
	 * </pre>
	 *
	 *
	 * @param start
	 * @return a verifying context
	 */
	public BetweenVerifyContext after(MockPoint start) {
		return between(start, BIG_CRUNCH);
	}

	/**
	 * Verify that calls happened at or before a point in time.
	 *
	 * Typical usage:
	 * <pre>
	 * Mockachino.before(end).verifyAtLeast(1).on(mock).method();
	 * </pre>
	 *
	 *
	 * @param end
	 * @return a verifying context
	 */
	public BetweenVerifyContext before(MockPoint end) {
		return between(BIG_BANG, end);
	}

	private void assertMockPoint(MockPoint point, String name) {
		checkNull(name, point);
		if (point.getMockContext() != this) {
			throw new UsageError(name + " came from the wrong mock context");

		}
	}

    public void setupMocks(Object obj) {
        checkNull("obj", obj);
        try {
            setupFields(obj, obj.getClass());
        } catch (IllegalAccessException e) {
            throw new Error(e);
        }
    }

    private void setupFields(Object obj, Class<?> clazz) throws IllegalAccessException {
        if (clazz != null) {
            setupFields(obj, clazz.getDeclaredFields());
            setupFields(obj, clazz.getSuperclass());
        }
    }

    private void setupFields(Object obj, Field[] fields) throws IllegalAccessException {
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getAnnotation(Spy.class) != null) {
                Object impl = field.get(obj);
                field.set(obj, mockType(field.getType(), Settings.spyOn(impl)));
            } else if (field.getAnnotation(Mock.class) != null) {
                field.set(obj, mock(field.getType()));
            }
        }
    }

}