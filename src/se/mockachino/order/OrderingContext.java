package se.mockachino.order;

import se.mockachino.MethodCall;
import se.mockachino.MockContext;
import se.mockachino.matchers.MatcherThreadHandler;

public class OrderingContext {
	private final MockContext context;
	private final MockPoint start;
	private final MockPoint end;
	private MethodCall currentCall = MethodCall.NULL;

	public OrderingContext(MockContext context, MockPoint start, MockPoint end) {
		this.context = context;
		this.start = start;
		this.end = end;
	}

	/**
	 * Verifies that a method call is called at least once.
	 *
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
	 *
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
		return new InOrderVerify(this, context, min, start, end);
	}

	MethodCall getCurrentCall() {
		return currentCall;
	}

	void setCurrent(MethodCall call) {
		this.currentCall = call;
	}

	private MockPoint getPoint(int offset) {
		MatcherThreadHandler.assertEmpty();
		int base = currentCall.getCallNumber();
		return new MockPoint(context, base + offset);
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
