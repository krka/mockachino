package se.mockachino.stub;

import se.mockachino.CallHandler;
import se.mockachino.MockContext;
import se.mockachino.MockData;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.matchers.MethodMatcherImpl;
import se.mockachino.util.MockachinoMethod;

public class Stubber {
	private final MockContext mockContext;
	private final CallHandler answer;
	private final StubVerifier verifier;

	public Stubber(MockContext mockContext, CallHandler answer, StubVerifier verifier) {
		this.mockContext = mockContext;
		this.answer = answer;
		this.verifier = verifier;
	}

	public <T> T on(T mock) {
		MockData data = mockContext.getData(mock);
		return mockContext.createProxy(mock, new StubHandler(answer, mock, data, verifier));
	}


	public void onMethod(Object mock, MockachinoMethod method, MethodMatcher matcher) {
		MockData data = mockContext.getData(mock);
		MethodStubs methodStubs = data.getStubs(method);
		methodStubs.add(new MethodStub(answer, matcher));
	}

	public void onMethodWithAnyArgument(Object mock, MockachinoMethod method) {
		onMethod(mock, method, MethodMatcherImpl.matchAll(method));
	}
}
