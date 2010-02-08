package se.mockachino.stub;

import se.mockachino.MockContext;
import se.mockachino.MockData;

public class StubThrow {
	private final MockContext mockContext;
	private final Throwable e;

	public StubThrow(MockContext mockContext, Throwable e) {
		this.mockContext = mockContext;
		this.e = e;
	}

	public <T> T on(T mock) {
		MockData data = mockContext.getData(mock);
		return mockContext.createProxy(mock, new StubThrowHandler(e, mock, data));
	}
}
