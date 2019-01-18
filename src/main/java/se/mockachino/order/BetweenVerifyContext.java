package se.mockachino.order;

import se.mockachino.Invocation;
import se.mockachino.MockData;
import se.mockachino.Mockachino;
import se.mockachino.matchers.MatcherThreadHandler;
import se.mockachino.verifier.VerifyRangeStart;

public class BetweenVerifyContext {
	private final MockPoint start;
	private final MockPoint end;

	public BetweenVerifyContext(
			MockPoint start, MockPoint end) {
		this.start = start;
		this.end = end;
	}

	public Iterable<Invocation> getCalls(Object mock) {
		MockData<Object> data = Mockachino.getData(mock);
		return data.getCalls(start, end);
	}


	/**
	 * Verifies that a method call is called between min and max times, inclusive.
	 * <p></p>
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
		return new VerifyRangeStart(min, max, start, end);
	}

	/**
	 * Verifies that a method call is called an exact number of times.
	 * <p></p>
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
	 * <p></p>
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
	 * <p></p>
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
	 * <p></p>
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
	 * <p></p>
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
	 * Creates a new ordering context which is used to verify method calls in order.
	 * Ordering contexts are completely independent of each other.
	 * <p></p>
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
		return new OrderingContext(start, end);
	}
}
