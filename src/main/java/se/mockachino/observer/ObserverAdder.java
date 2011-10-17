package se.mockachino.observer;

import se.mockachino.CallHandler;
import se.mockachino.MockData;
import se.mockachino.Mockachino;
import se.mockachino.proxy.ProxyUtil;

public class ObserverAdder {
	private final CallHandler observer;

	public ObserverAdder(CallHandler observer) {
		this.observer = observer;
	}

	public <T> T on(T mock) {
		MockData data = Mockachino.getData(mock);
		AddObserverHandler observerHandler = new AddObserverHandler(data, mock, observer);
		return ProxyUtil.createProxy(mock, observerHandler);
	}
}
