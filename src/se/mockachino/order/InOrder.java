package se.mockachino.order;

import se.mockachino.MockData;
import se.mockachino.MethodCall;
import se.mockachino.MockContext;
import se.mockachino.proxy.ProxyUtil;
import se.mockachino.order.InOrderVerifier;

import java.util.HashSet;
import java.util.Set;

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
