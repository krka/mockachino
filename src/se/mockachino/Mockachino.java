package se.mockachino;

import se.mockachino.exceptions.UsageError;
import se.mockachino.matchers.MatcherThreadHandler;
import se.mockachino.observer.ObserverAdder;
import se.mockachino.order.BetweenVerifyContext;
import se.mockachino.order.MockPoint;
import se.mockachino.order.OrderingContext;
import se.mockachino.proxy.ProxyUtil;
import se.mockachino.stub.AcceptAllVerifier;
import se.mockachino.stub.Stubber;
import se.mockachino.stub.exception.ThrowAnswer;
import se.mockachino.stub.returnvalue.ReturnAnswer;
import se.mockachino.stub.returnvalue.ReturnVerifier;
import se.mockachino.verifier.VerifyRangeStart;

/**
 * This is the main entry point of all your mocking needs.
 * All operations delegates to a singleton {@link se.mockachino.MockContext}.
 *
 * <p>
 * For most use cases, using this singleton is the most convenient approach,
 * but are also free to use a {@link se.mockachino.MockContext} object directly.
 * <p>
 * Consider statically import these classes to reduce verbosity of tests:
 * <pre>
 * import static se.mockachino.Mockachino.*;
 * import static se.mockachino.matchers.Matchers.*;
 * import static se.mockachino.Settings.*;
 * </pre>
 * 
 * <p>
 * See {@link se.mockachino.matchers.MatchersBase} for available matchers
 * and
 * see {@link se.mockachino.matchers.Matchers} for useful matcher shortcuts.
 *
 * <p>
 * See {@link Settings} for how to mock classes with various settings.
 */
public class Mockachino {
	public static final MockContext DEFAULT_CONTEXT = new MockContext();
	public static final CallHandler DEFAULT_VALUES = MockContext.DEFAULT_VALUES;
	public static final CallHandler DEEP_MOCK = DEFAULT_CONTEXT.DEEP_MOCK;

	/**
	 * Creates a new mock with a default handler and default settings.
	 *
	 * @param clazz the interface or class to mock
	 * @return a mock object of the same class
	 */
	public static <T> T mock(Class<T> clazz) {
		return DEFAULT_CONTEXT.mock(clazz);
	}

	/**
	 * Creates a new mock with specified settings.
	 *
	 * @param clazz the interface or class to mock
	 * @return a mock object of the same class
	 */
	public static <T> T mock(Class<T> clazz, MockSettings settings) {
		return DEFAULT_CONTEXT.mock(clazz, settings);
	}

	/**
	 * Creates a new mock with class impl.getClass() with the impl as a default handler.
	 * Equivalent to mock(impl.getClass(), Settings.spyOn(impl));
	 *
	 * @param impl the object to spy on
	 * @return a mock object of the same class as impl
	 */
	public static <T> T spy(T impl) {
		return DEFAULT_CONTEXT.spy(impl);
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
	public static <T> T spy(T impl, MockSettings settings) {
		return DEFAULT_CONTEXT.spy(impl, settings);
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
	public static OrderingContext newOrdering() {
		return DEFAULT_CONTEXT.newOrdering();
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
	public static VerifyRangeStart verifyRange(int min, int max) {
		return DEFAULT_CONTEXT.verifyRange(min, max);
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
	public static VerifyRangeStart verifyExactly(int count) {
		return DEFAULT_CONTEXT.verifyExactly(count);
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
	public static VerifyRangeStart verifyNever() {
		return DEFAULT_CONTEXT.verifyNever();
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
	public static VerifyRangeStart verifyOnce() {
		return DEFAULT_CONTEXT.verifyOnce();
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
	public static VerifyRangeStart verifyAtLeast(int min) {
		return DEFAULT_CONTEXT.verifyAtLeast(min);
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
	public static VerifyRangeStart verifyAtMost(int max) {
		return DEFAULT_CONTEXT.verifyAtMost(max);
	}

    /**
     * Stubs a method call to throw an exception.
     *
     * Typical usage:
     * <pre>
     * Mockachino.stubThrow(myException).on(mock).method();
     * </pre>
     *
     * @param e the exception to throw
     * @return a stubber
     */
    public static Stubber stubThrow(Throwable e) {
        MockContext.checkNull("exception", e);
        MatcherThreadHandler.assertEmpty();
        return new Stubber(new ThrowAnswer(e), AcceptAllVerifier.INSTANCE);
    }

    /**
     * Stubs a method call to return a specific value.
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
    public static Stubber stubReturn(Object returnValue) {
        MatcherThreadHandler.assertEmpty();
        return new Stubber(new ReturnAnswer(returnValue), new ReturnVerifier(returnValue));
    }

    /**
     * Stubs a method call with a specific answer strategy.
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
    public static Stubber stubAnswer(CallHandler answer) {
        MockContext.checkNull("answer", answer);
        MatcherThreadHandler.assertEmpty();
        return new Stubber(answer, AcceptAllVerifier.INSTANCE);
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
	public static ObserverAdder observeWith(CallHandler observer) {
		return DEFAULT_CONTEXT.observeWith(observer);
	}

	/**
	 * Gets the current point on time on the call history.
	 * It can be useful to run between calls to the code under test,
	 * to later verify that calls happened during
	 * specific times.
	 *
	 * @return the mock point
	 */
	public static MockPoint getCurrentPoint() {
		return DEFAULT_CONTEXT.getCurrentPoint();
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
	public static BetweenVerifyContext between(MockPoint start, MockPoint end) {
		return DEFAULT_CONTEXT.between(start, end);
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
	public static BetweenVerifyContext after(MockPoint start) {
		return DEFAULT_CONTEXT.after(start);
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
	public static BetweenVerifyContext before(MockPoint end) {
		return DEFAULT_CONTEXT.before(end);
	}

	/**
	 * Checks if a class is mockable by Mockachino.
	 * @param clazz
	 * @return true if the class is mockable, otherwise false.
	 */
	public static boolean canMock(Class clazz) {
		return ProxyUtil.canMock(clazz);
	}

    public static void setupMocks(Object obj) {
        DEFAULT_CONTEXT.setupMocks(obj);
    }

    /**
     * Get the metadata for mock.
     *
     * This can be used both for resetting mocks, and inspecting calls on the mock.
     *
     * @param mock the mock object
     * @return the mock metadata
     */
    public static <T> MockData<T> getData(T mock) {
        MockContext.checkNull("mock", mock);
        MatcherThreadHandler.assertEmpty();
        try {
            ProxyMetadata<T> metadata = (ProxyMetadata) mock;
            MockData<T> data = metadata.mockachino_getMockData();
            if (data == null) {
                throw new UsageError("argument " + mock + " is not a mock object");
            }
            return data;
        } catch (ClassCastException e) {
            throw new UsageError("argument " + mock + " is not a mock object");
        }
    }
}
