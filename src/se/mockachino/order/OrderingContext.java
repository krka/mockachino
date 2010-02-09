package se.mockachino.order;

import se.mockachino.MethodCall;
import se.mockachino.MockContext;
import se.mockachino.MockData;

public class OrderingContext {
	private final MockContext context;
	private MethodCall currentCall = MockData.NULL_METHOD;

	public OrderingContext(MockContext context) {
		this.context = context;
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
		return new InOrderVerify(this, context, min);
	}

	MethodCall getCurrentCall() {
		return currentCall;
	}

	void setCurrent(MethodCall call) {
		this.currentCall = call;
	}
}
