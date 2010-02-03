package se.mockachino.listener;

import se.mockachino.MockContext;
import se.mockachino.MockData;
import se.mockachino.stub.StubStart;

public class ListenerAdder implements StubStart {
	private final MockContext mockContext;
	private final MethodCallListener listener;

	public ListenerAdder(MockContext mockContext, MethodCallListener listener) {
		this.mockContext = mockContext;
		this.listener = listener;
	}

	public <T> T on(T mock) {
		MockData data = mockContext.getData(mock);
		return mockContext.createProxy(mock, new AddListener(data, mock, listener));
	}
}
