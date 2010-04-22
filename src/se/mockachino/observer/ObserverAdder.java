package se.mockachino.observer;

import se.mockachino.CallHandler;
import se.mockachino.MockContext;
import se.mockachino.MockData;
import se.mockachino.Mockachino;

public class ObserverAdder {
	private final MockContext mockContext;
	private final CallHandler observer;

	public ObserverAdder(MockContext mockContext, CallHandler observer) {
		this.mockContext = mockContext;
		this.observer = observer;
	}

	public <T> T on(T mock) {
		MockData data = Mockachino.getData(mock);
		AddObserverHandler observerHandler = new AddObserverHandler(data, mock, observer);
		return mockContext.createProxy(mock, observerHandler);
	}
}
