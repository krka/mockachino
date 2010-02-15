package se.mockachino.listener;

import se.mockachino.CallHandler;
import se.mockachino.MockContext;
import se.mockachino.MockData;

public class ListenerAdder {
	private final MockContext mockContext;
	private final CallHandler listener;

	public ListenerAdder(MockContext mockContext, CallHandler listener) {
		this.mockContext = mockContext;
		this.listener = listener;
	}

	public <T> T on(T mock) {
		MockData data = mockContext.getData(mock);
		AddListenerHandler listenerHandler = new AddListenerHandler(data, mock, listener);
		return mockContext.createProxy(mock, listenerHandler);
	}
}
