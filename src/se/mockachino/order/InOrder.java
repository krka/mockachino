package se.mockachino.order;

import se.mockachino.MockData;
import se.mockachino.MethodCall;
import se.mockachino.MockContext;

public class InOrder {
	private final MockContext context;
	private MethodCall currentCall = MockData.NULL_METHOD;

	public InOrder(MockContext context) {
		this.context = context;
	}

	public InOrderVerify verify() {
		return verifyAtLeast(1);
	}

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
