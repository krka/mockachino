package se.mockachino.stub;

import se.mockachino.MockContext;
import se.mockachino.MockData;
import se.mockachino.expectations.MethodStubs;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.matchers.MethodMatcherImpl;
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

	public void onMethod(Object mock, MockachinoMethod method, MethodMatcher matcher) {
		MockData data = mockContext.getData(mock);
		MethodStubs methodStubs = data.getExpectations(method);
		methodStubs.add(new ReturnValueStub(returnValue, matcher));
	}

	public void onMethodWithAnyArgument(Object mock, MockachinoMethod method) {
		onMethod(mock, method, MethodMatcherImpl.matchAll(method));
	}
}
