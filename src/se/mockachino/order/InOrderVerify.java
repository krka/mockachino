package se.mockachino.order;

import se.mockachino.MockContext;
import se.mockachino.MockData;
import se.mockachino.proxy.ProxyUtil;

public class InOrderVerify {
	private final InOrder inOrder;
	private final MockContext context;
	private final int min;

	public InOrderVerify(InOrder inOrder, MockContext context, int min) {
		this.inOrder = inOrder;
		this.context = context;
		this.min = min;
	}

	public <T> T on(T mock) {
		MockData<T> data = context.getData(mock);
		Class<T> clazz = data.getInterface();
		return ProxyUtil.newProxy(clazz, new InOrderVerifier(inOrder, mock, data, min));
	}
}
