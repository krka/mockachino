package se.mockachino.order;

import se.mockachino.Invocation;
import se.mockachino.MockData;
import se.mockachino.Mockachino;
import se.mockachino.proxy.ProxyUtil;

public class InOrderVerify {
	private final OrderingContext orderingContext;
	private final int min;
	private final MockPoint start;
	private final MockPoint end;

	public InOrderVerify(OrderingContext orderingContext, int min, MockPoint start, MockPoint end) {
		this.orderingContext = orderingContext;
		this.min = min;
		this.start = start;
		this.end = end;
	}

	public <T> T on(T mock) {
		MockData<T> data = Mockachino.getData(mock);
		Class<T> clazz = data.getInterface();
		Iterable<Invocation> calls = data.getCalls(start, end);
		return (T) ProxyUtil.newProxy(clazz, new InOrderVerifyHandler(orderingContext, mock, calls, min));
	}
}
