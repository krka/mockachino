package se.mockachino.order;

import se.mockachino.MockData;
import se.mockachino.MethodCall;
import se.mockachino.MockContext;
import se.mockachino.proxy.ProxyUtil;
import se.mockachino.invocationhandler.InOrderVerifier;

import java.util.Collections;
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
		return ProxyUtil.newProxy(clazz, new InOrderVerifier(this, data));
	}

	public int getCurrentCallNumber() {
		return currentCallNumber;
	}

	public MethodCall getCurrentCall() {
		return currentCall;
	}

	public boolean consume(MethodCall call) {
		synchronized (consumedCalls) {
			return consumedCalls.add(call);
		}
	}

	public void setCurrent(int number, MethodCall call) {
		this.currentCallNumber = number;
		this.currentCall = call;
	}
}
