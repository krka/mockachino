package se.mockachino.stub;

import se.mockachino.MockContext;
import se.mockachino.MockData;
import se.mockachino.expectations.MethodExpectations;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.util.MockachinoMethod;

public class StubReturn {
	private final MockContext mockContext;
	private final Object returnValue;

	public StubReturn(MockContext mockContext, Object returnValue) {
		this.mockContext = mockContext;
		this.returnValue = returnValue;
	}

	public <T> T on(T mock) {
		MockData data = mockContext.getData(mock);
		return mockContext.createProxy(mock, new StubReturnHandler(returnValue, mock, data));
	}

	public void onMethodWithAnyArgument(Object mock, MockachinoMethod method) {
		MockData data = mockContext.getData(mock);
		MethodExpectations methodExpectations = data.getExpectations(method);
		methodExpectations.add(new MethodStub(returnValue, MethodMatcher.matchAll(method)));
	}
}
