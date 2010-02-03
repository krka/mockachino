package se.mockachino.stub;

import se.mockachino.MockContext;
import se.mockachino.MockData;

public class StubReturn implements StubStart {
	private final MockContext mockContext;
	private final Object returnValue;

	public StubReturn(MockContext mockContext, Object returnValue) {
		this.mockContext = mockContext;
		this.returnValue = returnValue;
	}

	@Override
	public <T> T on(T mock) {
		MockData data = mockContext.getData(mock);
		return mockContext.createProxy(mock, new Stubber(returnValue, data));
	}
}
