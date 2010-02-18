package se.mockachino;

import se.mockachino.exceptions.UsageError;
import se.mockachino.invocationhandler.DeepMockHandler;
import se.mockachino.invocationhandler.DefaultInvocationHandler;
import se.mockachino.matchers.MatcherThreadHandler;
import se.mockachino.mock.MockHandler;
import se.mockachino.observer.ObserverAdder;
import se.mockachino.order.BetweenVerifyContext;
import se.mockachino.order.MockPoint;
import se.mockachino.order.OrderingContext;
import se.mockachino.proxy.ProxyUtil;
import se.mockachino.spy.SpyHandler;
import se.mockachino.stub.AcceptAllVerifier;
import se.mockachino.stub.Stubber;
import se.mockachino.stub.exception.ThrowAnswer;
import se.mockachino.stub.returnvalue.ReturnAnswer;
import se.mockachino.stub.returnvalue.ReturnVerifier;
import se.mockachino.verifier.VerifyRangeStart;

import java.lang.reflect.InvocationHandler;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class MockContext {
	public static final CallHandler DEFAULT_VALUES = new DefaultInvocationHandler();
	public final CallHandler DEEP_MOCK = new DeepMockHandler(this, DEFAULT_VALUES);

	public final MockPoint BIG_BANG = new MockPoint(this, 0);
	public final MockPoint BIG_CRUNCH = new MockPoint(this, Integer.MAX_VALUE);

	private final AtomicInteger nextSequenceNumber = new AtomicInteger();
	private final AtomicInteger nextMockId = new AtomicInteger();

	/**
	 * Creates a new mock with a default handler
	 * @param clazz the interface or class to mock
	 * @return a mock object of the same class
	 */
	public <T> T mock(Class<T> clazz) {
		checkNull("clazz", clazz);
		MockSettings mockSettings = Settings.newSettings();
		return mock(clazz, mockSettings);
	}

	public <T> T mock(Class<T> clazz, MockSettings settings) {
		checkNull("clazz", clazz);
		checkNull("settings", settings);
		return mock(clazz, settings.getFallback(), settings.isQuick(), settings.getName(), settings.getExtraInterfaces());
	}

	public <T> T spy(T impl) {
		checkNull("impl", impl);
		return mock((Class<T>)impl.getClass(), Settings.spyOn(impl));
	}

	public <T> T spy(T impl, MockSettings settings) {
		checkNull("impl", impl);
		checkNull("settings", settings);
		return mock((Class<T>)impl.getClass(), settings.spyOn(impl));
	}

	private <T> T mock(Class<T> clazz, CallHandler fallback, boolean quick, String name, Set<Class<?>> extraInterfaces) {
		checkNull("clazz", clazz);
		checkNull("fallback", fallback);
		if (name == null) {
			name = getDefaultName(clazz, fallback);
		}
		MockHandler mockHandler = new MockHandler(
				this, fallback, new MockData(clazz, extraInterfaces), quick, name);
		T mock = ProxyUtil.newProxy(clazz, mockHandler, extraInterfaces);
		return mock;
	}

	private <T> String getDefaultName(Class<T> clazz, CallHandler fallback) {
		String kind = "Mock";
		if (fallback instanceof SpyHandler) {
			kind = "Spy";
		}
		return kind + ":" + clazz.getSimpleName() + ":" + nextMockId();
	}

	private void checkNull(String name, Object obj) {
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
	 * OrderingContext context = Mockachino.verifyOrder();
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
	public <T> T createProxy(T mock, InvocationHandler handler) {
		MatcherThreadHandler.assertEmpty();
		MockData data = getData(mock);
		Class<T> iface = data.getInterface();
		return ProxyUtil.newProxy(iface, handler, (Set<Class<?>>) data.getExtraInterfaces());
	}

	/**
	 * Stubs a method call to throw an exception
	 *
	 * Typical usage:
	 * <pre>
	 * Mockachino.stubThrow(myException).on(mock).method();
	 * </pre>
	 *
	 * @param e the exception to throw
	 * @return a stubber
	 */
	public Stubber stubThrow(Throwable e) {
		checkNull("exception", e);
		MatcherThreadHandler.assertEmpty();
		return new Stubber(this, new ThrowAnswer(e), AcceptAllVerifier.INSTANCE);
	}

	/**
	 * Stubs a method call to return a specific value
	 *
	 * Typical usage:
	 * <pre>
	 * Mockachino.stubReturn(value).on(mock).method();
	 * </pre>
	 *
	 * Note that the type of the value must match the return type of the method call.
	 * This is checked at runtime and will throw a UsageError if they don't match.
	 *
	 * @param returnValue the returnValue to return when the method is called.
	 * @return a stubber
	 */
	public Stubber stubReturn(Object returnValue) {
		MatcherThreadHandler.assertEmpty();
		return new Stubber(this, new ReturnAnswer(returnValue), new ReturnVerifier(returnValue));
	}

	/**
	 * Stubs a method call with a specific answer strategy
	 *
	 * Typical usage:
	 * <pre>
	 * Mockachino.stubAnswer(answer).on(mock).method();
	 * </pre>
	 *
	 * All matching method calls will invoke the getValue()-method on the answer-object.
	 *
	 * @param answer the answer to use
	 * @return a stubber
	 */
	public Stubber stubAnswer(CallHandler answer) {
		if (answer== null) {
			throw new UsageError("answer can not be null");
		}
		MatcherThreadHandler.assertEmpty();
		return new Stubber(this, answer, AcceptAllVerifier.INSTANCE);
	}

	public ObserverAdder observeWith(CallHandler observer) {
		checkNull("observer", observer);
		MatcherThreadHandler.assertEmpty();
		return new ObserverAdder(this, observer);
	}

	/**
	 * Get the metadata for mock.
	 *
	 * Typically not needed by end users
	 * @param mock the mock object
	 * @return the mock metadata
	 */
	public <T> MockData<T> getData(T mock) {
		checkNull("mock", mock);
		try {
			ProxyMetadata<T> metadata = (ProxyMetadata) mock;
			if (metadata.mockachino_getContext() != this) {
				throw new UsageError("argument " + mock + " belongs to a different mock context");
			}
			MockData<T> data = metadata.mockachino_getMockData();
			if (data == null) {
				throw new UsageError("argument " + mock + " is not a mock object");
			}
			return data;
		} catch (ClassCastException e) {
			throw new UsageError("argument " + mock + " is not a mock object");
		}
	}

	/**
	 * Get a list of all calls made for a mock.
	 * This may be useful for debugging.
	 *
	 * @param mock
	 * @return the list of calls.
	 */
	public Iterable<MethodCall> getCalls(Object mock) {
		return getData(mock).getCalls();
	}

	/**
	 * Increments the sequence of method calls.
	 * This is not relevant for end users.
	 * @return the next sequence number.
	 */
	public int incrementSequence() {
		return nextSequenceNumber.incrementAndGet();
	}

	public MockPoint getCurrentPoint() {
		return new MockPoint(this, nextSequenceNumber.get());
	}

	public BetweenVerifyContext between(MockPoint start, MockPoint end) {
		assertMockPoint(start, "start");
		assertMockPoint(end, "end");
		return new BetweenVerifyContext(this, start, end);
	}

	public BetweenVerifyContext after(MockPoint start) {
		return between(start, BIG_CRUNCH);
	}

	public BetweenVerifyContext before(MockPoint end) {
		return between(BIG_BANG, end);
	}

	private void assertMockPoint(MockPoint start, String name) {
		if (start == null) {
			throw new UsageError(name + " can not be null");
		}
		if (start.getMockContext() != this) {
			throw new UsageError(name + " came from the wrong mock context");

		}
	}
}