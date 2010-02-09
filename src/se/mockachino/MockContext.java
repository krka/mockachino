package se.mockachino;

import se.mockachino.exceptions.UsageError;
import se.mockachino.invocationhandler.DefaultInvocationHandler;
import se.mockachino.listener.ListenerAdder;
import se.mockachino.listener.MethodCallListener;
import se.mockachino.matchers.MatcherThreadHandler;
import se.mockachino.mock.MockHandler;
import se.mockachino.order.OrderingContext;
import se.mockachino.proxy.ProxyUtil;
import se.mockachino.stub.StubAnswer;
import se.mockachino.stub.StubReturn;
import se.mockachino.stub.StubThrow;
import se.mockachino.verifier.VerifyRangeStart;

import java.lang.reflect.InvocationHandler;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MockContext {
	private static final InvocationHandler DEFAULT_INVOCATION_HANDLER = new DefaultInvocationHandler();

	private final Map<Object, MockData> mockData = new ConcurrentHashMap<Object, MockData>();
	private final AtomicInteger nextSequenceNumber = new AtomicInteger();
	private final AtomicInteger nextMockId = new AtomicInteger();

	/**
	 * Creates a new mock with a default handler
	 * @param clazz the interface or class to mock
	 * @return a mock object of the same class
	 */
	public <T> T mock(Class<T> clazz) {
		assertClass(clazz);
		return mock(clazz, DEFAULT_INVOCATION_HANDLER);
	}

	/**
	 * Creates a new mock with a custom handler.
	 * @param clazz the class of the returned object
	 * @param handler the handler that is called for each mock method invocation
	 * @returna mock object of the same class
	 */
	public <T> T mock(Class<T> clazz, InvocationHandler handler) {
		assertClass(clazz);
		assertHandler(handler);
		return spy(clazz, ProxyUtil.newProxy(clazz, handler), "Mock");
	}

	/**
	 * Creates a mock that spies on a specific object.
	 * Unless overridden by stubbing, spied on object will be called for all invocations
	 * @param clazz the class to spy with, must be a superclass of impl
	 * @param impl the object to spy on.
	 * @return the mock object
	 */
	public <T> T spy(Class<T> clazz, T impl) {
		assertClass(clazz);
		assertImpl(impl);
		return spy(clazz, impl, "Spy");
	}

	/**
	 * Creates a mock that spies on a specific object.
	 * Unless overridden by stubbing, spied on object will be called for all invocations
	 * @param impl the object to spy on
	 * @return a mock object of the same class as impl
	 */
	public <T> T spy(T impl) {
		assertImpl(impl);
		return spy((Class<T>) impl.getClass(), impl);
	}

	private <T> T spy(Class<T> clazz, T impl, String kind) {
		assertClass(clazz);
		T mock = ProxyUtil.newProxy(clazz, new MockHandler(this, impl, kind, clazz.getSimpleName(), nextMockId()));
		mockData.put(mock, new MockData(clazz));
		return mock;
	}

	private <T> void assertClass(Class<T> clazz) {
		if (clazz == null) {
			throw new UsageError("class can not be null");
		}
	}

	private <T> void assertImpl(T impl) {
		if (impl == null) {
			throw new UsageError("impl can not be null");
		}
	}

	private <T> void assertHandler(InvocationHandler handler) {
		if (handler == null) {
			throw new UsageError("handler can not be null");
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
	public OrderingContext verifyOrder() {
		MatcherThreadHandler.assertEmpty();
		return new OrderingContext(this);
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
		return ProxyUtil.newProxy(iface, handler);
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
	public StubThrow stubThrow(Throwable e) {
		if (e == null) {
			throw new UsageError("exception can not be null");
		}
		MatcherThreadHandler.assertEmpty();
		return new StubThrow(this, e);
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
	public StubReturn stubReturn(Object returnValue) {
		MatcherThreadHandler.assertEmpty();
		return new StubReturn(this, returnValue);
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
	public StubAnswer stubAnswer(Answer answer) {
		if (answer== null) {
			throw new UsageError("answer can not be null");
		}
		MatcherThreadHandler.assertEmpty();
		return new StubAnswer(this, answer);
	}

	/**
	 * Add a listener on a specific method. Every time the method is called on the mock,
	 * this listener will get a callback.
	 *
	 * Typical usage:
	 * <pre>
	 * Mockachino.listenWith(myListener).on(mock).method();
	 * </pre>

	 * @param listener the listener
	 * @return a listener adder
	 */
	public ListenerAdder listenWith(MethodCallListener listener) {
		if (listener == null) {
			throw new UsageError("Listener can not be null");
		}
		MatcherThreadHandler.assertEmpty();
		return new ListenerAdder(this, listener);
	}

	/**
	 * Get the metadata for mock.
	 *
	 * Typically not needed by end users
	 * @param mock the mock object
	 * @return the mock metadata
	 */
	public <T> MockData<T> getData(T mock) {
		if (mock == null) {
			throw new UsageError("Did not expect null value");
		}
		MockData data = mockData.get(mock);
		if (data == null) {
			throw new UsageError(
							"Argument " + mock + " is not a mocked object.");
		}
		return data;
	}

	/**
	 * Get a list of all calls made for a mock.
	 * This may be useful for debugging.
	 *
	 * @param mock
	 * @return the list of calls.
	 */
	public List<MethodCall> getCalls(Object mock) {
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

	/**
	 * Resets calls, stubs and listeners for a mock
	 * @param mock
	 */
	public void reset(Object mock) {
		resetCalls(mock);
		resetStubs(mock);
		resetListeners(mock);
	}

	/**
	 * Resets calls, stubs and listeners for mocks
	 * @param mocks
	 */
	public void reset(Object mock, Object... mocks) {
		reset(mock);
		for (Object mock2 : mocks) {
			reset(mock2);
		}
	}

	/**
	 * Resets list of listeners for a mock
	 * @param mock
	 */
	public void resetListeners(Object mock) {
		MockData<Object> data = getData(mock);
		data.resetListeners();
	}

	/**
	 * Resets list of listeners for mocks
	 * @param mocks
	 */
	public void resetListeners(Object mock, Object... mocks) {
		resetListeners(mock);
		for (Object mock2 : mocks) {
			resetListeners(mock2);
		}
	}

	/**
	 * Resets list of calls for a mock
	 * @param mock
	 */
	public void resetCalls(Object mock) {
		MockData<Object> data = getData(mock);
		data.resetCalls();
	}

	/**
	 * Resets list of calls for mocks
	 * @param mocks
	 */
	public void resetCalls(Object mock, Object[] mocks) {
		resetCalls(mock);
		for (Object mock2 : mocks) {
			resetCalls(mock2);
		}
	}

	/**
	 * Resets list of stubs for a mock
	 * @param mock
	 */
	public void resetStubs(Object mock) {
		MockData<Object> data = getData(mock);
		data.resetStubs();
	}

	/**
	 * Resets list of stubs for mocks
	 * @param mocks
	 */
	public void resetStubs(Object mock, Object[] mocks) {
		resetStubs(mock);
		for (Object mock2 : mocks) {
			resetStubs(mock2);
		}
	}

}