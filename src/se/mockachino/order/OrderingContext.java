package se.mockachino.order;

import se.mockachino.Invocation;
import se.mockachino.matchers.MatcherThreadHandler;

public class OrderingContext {
	private final MockPoint start;
	private final MockPoint end;
	private Invocation currentInvocation = Invocation.NULL;

	public OrderingContext(MockPoint start, MockPoint end) {
		this.start = start;
		this.end = end;
	}

	/**
	 * Verifies that a method call is called at least once.
	 * <p/>
	 * Typical usage:
	 * <pre>
	 * orderingContext.verify().on(mock).method();
	 * </pre>
	 *
	 * @return a verifier
	 */
	public InOrderVerify verify() {
		return verifyAtLeast(1);
	}

	/**
	 * Verifies that a method call is called at least min number of times.
	 * <p/>
	 * Typical usage:
	 * <pre>
	 * orderingContext.verifyAtLeast(3).on(mock).method();
	 * </pre>
	 *
	 * @param min minimum number of calls
	 * @return a verifier
	 */
	public InOrderVerify verifyAtLeast(int min) {
		MatcherThreadHandler.assertEmpty();
		return new InOrderVerify(this, min, start, end);
	}

	Invocation getCurrentInvocation() {
		return currentInvocation;
	}

	void setCurrent(Invocation call) {
		this.currentInvocation = call;
	}

	private MockPoint getPoint(int offset) {
		MatcherThreadHandler.assertEmpty();
		int base = currentInvocation.getCallNumber();
		return new MockPoint(base + offset);
	}

	public MockPoint atLastCall() {
		return getPoint(0);
	}

	public MockPoint beforeLastCall() {
		return getPoint(-1);
	}

	public MockPoint afterLastCall() {
		return getPoint(1);
	}

}
