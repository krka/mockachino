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
	private final Set<MethodCall> consumedCalls;
	private int currentCallNumber;
	private MethodCall currentCall;

	public InOrder(MockContext context) {
		this.context = context;
		consumedCalls = new HashSet<MethodCall>();
	}

	public <T> T verify(T mock) {
		MockData<T> data = context.getData(mock);
		Class<T> clazz = data.getInterface();
		return ProxyUtil.newProxy(clazz, new InOrderVerifier(this, mock, data));
	}

	int getCurrentCallNumber() {
		return currentCallNumber;
	}

	MethodCall getCurrentCall() {
		return currentCall;
	}

	boolean consume(MethodCall call) {
		synchronized (consumedCalls) {
			return consumedCalls.add(call);
		}
	}

	void setCurrent(int number, MethodCall call) {
		this.currentCallNumber = number;
		this.currentCall = call;
	}
}
