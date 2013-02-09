package se.mockachino;

import com.googlecode.gentyref.TypeToken;
import se.mockachino.alias.SimpleAlias;
import se.mockachino.annotations.Mock;
import se.mockachino.annotations.Spy;
import se.mockachino.assertion.AssertThrows;
import se.mockachino.exceptions.UsageError;
import se.mockachino.exceptions.VerificationError;
import se.mockachino.invocationhandler.CollectionsHandler;
import se.mockachino.invocationhandler.DeepMockHandler;
import se.mockachino.invocationhandler.PrimitiveInvocationHandler;
import se.mockachino.matchers.MatcherThreadHandler;
import se.mockachino.matchers.matcher.EqualityMatcher;
import se.mockachino.matchers.matcher.Matcher;
import se.mockachino.mock.WhenStubber;
import se.mockachino.observer.ObserverAdder;
import se.mockachino.order.BetweenVerifyContext;
import se.mockachino.order.MockPoint;
import se.mockachino.order.OrderingContext;
import se.mockachino.proxy.ProxyUtil;
import se.mockachino.stub.Stubber;
import se.mockachino.stub.exception.ThrowAnswer;
import se.mockachino.stub.returnvalue.ReturnAnswer;
import se.mockachino.stub.returnvalue.SequentialAnswers;
import se.mockachino.verifier.VerifyRangeStart;

import java.lang.reflect.*;

/**
 * This is the main entry point of all your mocking needs.
 * All operations delegates to a singleton {@link MockUtil}.
 * <p/>
 * <p/>
 * For most use cases, using this singleton is the most convenient approach,
 * but are also free to use a {@link MockUtil} object directly.
 * <p/>
 * Consider statically import these classes to reduce verbosity of tests:
 * <pre>
 * import static se.mockachino.Mockachino.*;
 * import static se.mockachino.matchers.Matchers.*;
 * import static se.mockachino.Settings.*;
 * </pre>
 * <p/>
 * <p/>
 * See {@link se.mockachino.matchers.MatchersBase} for available matchers
 * and
 * see {@link se.mockachino.matchers.Matchers} for useful matcher shortcuts.
 * <p/>
 * <p/>
 * See {@link Settings} for how to mock classes with various settings.
 */
public class Mockachino {
	public static final PrimitiveInvocationHandler PRIMITIVE_VALUES = new PrimitiveInvocationHandler();
	public static final CallHandler DEFAULT_VALUES = new CollectionsHandler(PRIMITIVE_VALUES);
    public static final CallHandler DEEP_MOCK = new DeepMockHandler(DEFAULT_VALUES);
	public static final MockPoint BIG_BANG = new MockPoint(0);
	public static final MockPoint BIG_CRUNCH = new MockPoint(Integer.MAX_VALUE);

    // Warning for static mutable state!

	/**
	 * Creates a new mock with a default handler and default settings.
	 *
	 * @param clazz the interface or class to mock
	 * @return a mock object of the same class
	 */
	public static <T> T mock(Class<T> clazz) {
		MockUtil.checkNull("clazz", clazz);
		MockSettings mockSettings = Settings.newSettings();
		return (T) MockUtil.mockType(clazz, mockSettings);
	}

	/**
	 * Creates a new mock with a default handler and default settings.
	 *
	 * @param type the type literal of the interface or class to mock
	 * @return a mock object of the same class
	 */
	public static <T> T mock(TypeToken<T> type) {
		MockUtil.checkNull("type", type);
		MockSettings mockSettings = Settings.newSettings();
		MockUtil.checkNull("typeToken", type);
		MockUtil.checkNull("settings", mockSettings);
		final Type type1 = type.getType();
		return (T) MockUtil.mockType(type1, mockSettings);
	}


	/**
	 * Creates a new mock with specified settings.
	 *
	 * @param clazz the interface or class to mock
	 * @return a mock object of the same class
	 */
	public static <T> T mock(Class<T> clazz, MockSettings settings) {
		MockUtil.checkNull("clazz", clazz);
		return (T) MockUtil.mockType(clazz, settings);
	}

	/**
	 * Creates a new mock with specified settings.
	 *
	 * @param type the type literal of the interface or class to mock
	 * @return a mock object of the same class
	 */
	public static <T> T mock(TypeToken<T> type, MockSettings settings) {
		MockUtil.checkNull("typeToken", type);
		MockUtil.checkNull("settings", settings);
		final Type type1 = type.getType();
		return (T) MockUtil.mockType(type1, settings);
	}

	/**
	 * Creates a new mock with class impl.getClass() with the impl as a default handler.
	 * Equivalent to mock(impl.getClass(), Settings.spyOn(impl));
	 *
	 * @param impl the object to spy on
	 * @return a mock object of the same class as impl
	 */
	public static <T> T spy(T impl) {
		MockUtil.checkNull("impl", impl);
		return (T) MockUtil.mockType((Class<T>) impl.getClass(), Settings.spyOn(impl));
	}

	/**
	 * Creates a new mock with class impl.getClass() with the impl as a default handler,
	 * and the specified settings.
	 * <p/>
	 * Equivalent to mock(impl.getClass(), settings.spyOn(impl));
	 *
	 * @param impl the object to spy on
	 * @return a mock object of the same class as impl
	 */
	public static <T> T spy(T impl, MockSettings settings) {
		MockUtil.checkNull("impl", impl);
		MockUtil.checkNull("settings", settings);
		return (T) MockUtil.mockType((Class<T>) impl.getClass(), settings.spyOn(impl));
	}


	/**
	 * Creates a new ordering context which is used to verify method calls in order.
	 * Ordering contexts are completely independent of each other.
	 * <p/>
	 * Typical usage:
	 * <pre>
	 * OrderingContext context = Mockachino.newOrdering();
	 * context.verifyAtLeast(3).on(mock).method();
	 * </pre>
	 *
	 * @return the new ordering context
	 */
	public static OrderingContext newOrdering() {
		MatcherThreadHandler.assertEmpty();
		return new OrderingContext(BIG_BANG, BIG_CRUNCH);
	}

	/**
	 * Verifies that a method call is called between min and max times, inclusive.
	 * <p/>
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
		MatcherThreadHandler.assertEmpty();
		return new VerifyRangeStart(min, max);
	}

	/**
	 * Verifies that a method call is called an exact number of times.
	 * <p/>
	 * Typical usage:
	 * <pre>
	 * Mockachino.verifyExactly(3).on(mock).method();
	 * </pre>
	 *
	 * @param count number of times the method should be called
	 * @return a verifier
	 */
	public static VerifyRangeStart verifyExactly(int count) {
		MatcherThreadHandler.assertEmpty();
		return new VerifyRangeStart(count, count);
	}

	/**
	 * Verifies that a method call is never called.
	 * <p/>
	 * Typical usage:
	 * <pre>
	 * Mockachino.verifyNever().on(mock).method();
	 * </pre>
	 *
	 * @return a verifier
	 */
	public static VerifyRangeStart verifyNever() {
		MatcherThreadHandler.assertEmpty();
		return new VerifyRangeStart(0, 0);
	}

	/**
	 * Verifies that a method call is only called exactly once.
	 * <p/>
	 * Typical usage:
	 * <pre>
	 * Mockachino.verifyOnce().on(mock).method();
	 * </pre>
	 *
	 * @return a verifier
	 */
	public static VerifyRangeStart verifyOnce() {
		MatcherThreadHandler.assertEmpty();
		return new VerifyRangeStart(1, 1);
	}

	/**
	 * Verifies that a method call is called at least a specific number of times.
	 * <p/>
	 * Typical usage:
	 * <pre>
	 * Mockachino.verifyAtLeast(3).on(mock).method();
	 * </pre>
	 *
	 * @param min number of times the method should be called
	 * @return a verifier
	 */
	public static VerifyRangeStart verifyAtLeast(int min) {
		MatcherThreadHandler.assertEmpty();
		return new VerifyRangeStart(min, Integer.MAX_VALUE);
	}

	/**
	 * Verifies that a method call is called at most a specific number of times.
	 * <p/>
	 * Typical usage:
	 * <pre>
	 * Mockachino.verifyAtMost(3).on(mock).method();
	 * </pre>
	 *
	 * @param max number of times the method should be called
	 * @return a verifier
	 */
	public static VerifyRangeStart verifyAtMost(int max) {
		MatcherThreadHandler.assertEmpty();
		return new VerifyRangeStart(0, max);
	}

	/**
	 * Stubs a method call to throw an exception.
	 * <p/>
	 * Typical usage:
	 * <pre>
	 * Mockachino.stubThrow(myException).on(mock).method();
	 * </pre>
	 *
	 * @param e the exception to throw
	 * @return a stubber
	 */
	public static <T> Stubber<T> stubThrow(Throwable e) {
		MockUtil.checkNull("exception", e);
		MatcherThreadHandler.assertEmpty();
		return new Stubber<T>(new SequentialAnswers<T>(new ThrowAnswer<T>(e)));
	}

	/**
	 * Stubs a method call to return a specific value.
	 * <p/>
	 * Typical usage:
	 * <pre>
	 * Mockachino.stubReturn(value).on(mock).method();
	 * </pre>
	 * <p/>
	 * Note that the type of the value must match the return type of the method call.
	 * This is checked at runtime and will throw a UsageError if they don't match.
	 *
	 * @param returnValue the returnValue to return when the method is called.
	 * @return a stubber
	 */
	public static <T> Stubber<T> stubReturn(T returnValue) {
		MatcherThreadHandler.assertEmpty();
		return new Stubber<T>(new SequentialAnswers<T>(new ReturnAnswer<T>(returnValue)));
	}

	public static <T> Stubber<T> stubReturn(T returnValue, T... returnValues) {
		MatcherThreadHandler.assertEmpty();
        Stubber<T> stubber = new Stubber<T>(new SequentialAnswers<T>(new ReturnAnswer<T>(returnValue)));
        if (returnValues != null) {
            for (T value : returnValues) {
                stubber.extend(new ReturnAnswer<T>(value));
            }
        }
        return stubber;
    }

	/**
	 * Stubs a method call with a specific answer strategy.
	 * <p/>
	 * Typical usage:
	 * <pre>
	 * Mockachino.stubAnswer(answer).on(mock).method();
	 * </pre>
	 * <p/>
	 * All matching method calls will invoke the getValue()-method on the answer-object.
	 *
	 * @param answer the answer to use
	 * @return a stubber
	 */
	public static <T> Stubber<T> stubAnswer(CallHandler<T> answer) {
		MockUtil.checkNull("answer", answer);
		MatcherThreadHandler.assertEmpty();
		return new Stubber(new SequentialAnswers(new VerifyableCallHandlerWrapper(answer)));
	}


	/**
	 * Adds an observer to a specific method call.
	 * The observer will get a callback every time the method is called and the arguments match.
	 * <p/>
	 * Typical usage:
	 * <pre>
	 * Mockachino.observeWith(observer).on(mock).method();
	 * </pre>
	 *
	 * @param observer the observer to use
	 * @return
	 */
	public static <T> ObserverAdder<T> observeWith(CallHandler<T> observer) {
		MockUtil.checkNull("observer", observer);
		MatcherThreadHandler.assertEmpty();
		return new ObserverAdder<T>(observer);
	}

	/**
	 * Gets the current point on time on the call history.
	 * It can be useful to run between calls to the code under test,
	 * to l
	 * ater verify that calls happened during
	 * specific times.
	 *
	 * @return the mock point
	 */
	public static MockPoint getCurrentPoint() {
		MatcherThreadHandler.assertEmpty();
		return new MockPoint(MockUtil.getSequence());
	}


	/**
	 * Verify that calls happened between (inclusive) two points in time.
	 * <p/>
	 * Typical usage:
	 * <pre>
	 * Mockachino.between(start, end).verifyAtLeast(1).on(mock).method();
	 * </pre>
	 *
	 * @param start
	 * @param end
	 * @return a verifying context
	 */
	public static BetweenVerifyContext between(MockPoint start, MockPoint end) {
		assertMockPoint(start, "start");
		assertMockPoint(end, "end");
		MatcherThreadHandler.assertEmpty();
		return new BetweenVerifyContext(start, end);
	}

	/**
	 * Verify that calls happened at or after a point in time.
	 * <p/>
	 * Typical usage:
	 * <pre>
	 * Mockachino.after(start).verifyAtLeast(1).on(mock).method();
	 * </pre>
	 *
	 * @param start
	 * @return a verifying context
	 */
	public static BetweenVerifyContext after(MockPoint start) {
		assertMockPoint(start, "start");
		assertMockPoint(BIG_CRUNCH, "end");
		MatcherThreadHandler.assertEmpty();
		return new BetweenVerifyContext(start, BIG_CRUNCH);
	}

	/**
	 * Verify that calls happened at or before a point in time.
	 * <p/>
	 * Typical usage:
	 * <pre>
	 * Mockachino.before(end).verifyAtLeast(1).on(mock).method();
	 * </pre>
	 *
	 * @param end
	 * @return a verifying context
	 */
	public static BetweenVerifyContext before(MockPoint end) {
		assertMockPoint(BIG_BANG, "start");
		assertMockPoint(end, "end");
		MatcherThreadHandler.assertEmpty();
		return new BetweenVerifyContext(BIG_BANG, end);
	}

	/**
	 * Checks if a class is mockable by Mockachino.
	 *
	 * @param clazz
	 * @return true if the class is mockable, otherwise false.
	 */
	public static boolean canMock(Class clazz) {
		return ProxyUtil.canMock(clazz);
	}

	public static void setupMocks(Object obj) {
		MockUtil.checkNull("obj", obj);
		try {
			setupFields(obj, obj.getClass());
		} catch (IllegalAccessException e) {
			throw new Error(e);
		}
	}

	/**
	 * Get the metadata for mock.
	 * <p/>
	 * This can be used both for resetting mocks, and inspecting calls on the mock.
	 *
	 * @param mock the mock object
	 * @return the mock metadata
	 */
	public static <T> MockData<T> getData(T mock) {
		MockUtil.checkNull("mock", mock);
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

	public static SimpleAlias newAlias() {
		return new SimpleAlias();
	}

	public static <T> WhenStubber<T> when(T mockInvocation) {
		return new WhenStubber<T>();
	}

	public static Object mockType(Type type, MockSettings mockSettings) {
		return MockUtil.mockType(type, mockSettings);
	}

	private static void assertMockPoint(MockPoint point, String name) {
		MockUtil.checkNull(name, point);
	}

	private static void setupFields(Object obj, Class<?> clazz) throws IllegalAccessException {
		if (clazz != null) {
			setupFields(obj, clazz.getDeclaredFields());
			setupFields(obj, clazz.getSuperclass());
		}
	}

	private static void setupFields(Object obj, Field[] fields) throws IllegalAccessException {
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.getAnnotation(Spy.class) != null) {
				Object impl = field.get(obj);
				field.set(obj, MockUtil.mockType(field.getGenericType(), Settings.spyOn(impl)));
			} else if (field.getAnnotation(Mock.class) != null) {
				field.set(obj, MockUtil.mockType(field.getGenericType(), new MockSettings()));
			}
		}
    }

    public static <T> T awaitState(final long timeoutMillis, Object compareTo, final T object) {
        final Matcher matcher = toMatcher(compareTo);
        final Class<?> matcherType = Primitives.getRealClass(matcher.getType());
        return (T) ProxyUtil.newProxy(object.getClass(), new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                if (!Primitives.getRealClass(method.getReturnType()).isAssignableFrom(matcherType)) {
                    throw new UsageError(method + " is not comparable with " + matcherType);
                }
                long t = System.currentTimeMillis();
                while (true) {
                    Object result = null;
                    Exception exception = null;
                    try {
                        result = method.invoke(object, args);
                        if (matcher.matches(result)) {
                            return result;
                        }
                    } catch (IllegalAccessException e) {
                        throw e;
                    } catch (IllegalArgumentException e) {
                        throw e;
                    } catch (InvocationTargetException e) {
                        exception = e;
                    }
                    Thread.sleep(20);
                    if (System.currentTimeMillis() - t > timeoutMillis) {
                        if (exception != null) {
                            throw new VerificationError("await state timed out. Got exception: " + exception.getMessage(), exception);
                        }
                        throw new VerificationError("await state timed out. Expected " + matcher + " but got " + result);
                    }
                }
            }
        });
    }

    private static Matcher toMatcher(Object compareTo) {
        if (MatcherThreadHandler.isClean()) {
            return new EqualityMatcher(compareTo);
        }
        return MatcherThreadHandler.getMatcher(compareTo, false);
    }

    public static AssertThrows assertThrows(Class<? extends Throwable> clazz) {
        return new AssertThrows(clazz);
    }
}
