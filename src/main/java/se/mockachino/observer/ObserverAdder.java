package se.mockachino.observer;

import se.mockachino.CallHandler;
import se.mockachino.MockData;
import se.mockachino.Mockachino;
import se.mockachino.proxy.ProxyUtil;

public class ObserverAdder<R> {
	private final CallHandler<R> observer;

	public ObserverAdder(CallHandler<R> observer) {
		this.observer = observer;
	}

	public <T> T on(T mock) {
		MockData data = Mockachino.getData(mock);
		AddObserverHandler<R> observerHandler = new AddObserverHandler<R>(data, mock, observer);
		return ProxyUtil.createProxy(mock, observerHandler);
	}
}
