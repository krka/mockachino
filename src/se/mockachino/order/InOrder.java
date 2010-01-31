package se.mockachino.order;

import se.mockachino.MockData;
import se.mockachino.MethodCall;
import se.mockachino.MockContext;
import se.mockachino.proxy.ProxyUtil;
import se.mockachino.invocationhandler.InOrderVerifier;

public class InOrder {
	private final MockContext context;

	private int currentCallNumber;
	private MethodCall currentCall;

	public InOrder(MockContext context) {
		this.context = context;
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

	public void setCurrent(int number, MethodCall call) {
		this.currentCallNumber = number;
		this.currentCall = call;
	}
}
