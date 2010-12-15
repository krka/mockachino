package se.mockachino.stub;

import se.mockachino.CallHandler;
import se.mockachino.MockData;
import se.mockachino.Mockachino;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.matchers.MethodMatcherImpl;
import se.mockachino.proxy.ProxyUtil;
import se.mockachino.util.MockachinoMethod;

public class Stubber {
	private final CallHandler answer;
	private final StubVerifier verifier;

	public Stubber(CallHandler answer, StubVerifier verifier) {
		this.answer = answer;
		this.verifier = verifier;
	}

	public <T> T on(T mock) {
		MockData data = Mockachino.getData(mock);
		return ProxyUtil.createProxy(mock, new StubHandler(answer, data, verifier));
	}


	public void onMethod(Object mock, MockachinoMethod method, MethodMatcher matcher) {
		verifier.verify(method);
		MockData data = Mockachino.getData(mock);
		MethodStubs methodStubs = data.getStubs(method);
		methodStubs.add(new MethodStub(answer, matcher));
	}

	public void onMethodWithAnyArgument(Object mock, MockachinoMethod method) {
		onMethod(mock, method, MethodMatcherImpl.matchAll(method));
	}

	public void onAnyMethod(Object mock) {
		MockData<Object> data = Mockachino.getData(mock);
		for (MockachinoMethod method : data.getMethods()) {
			onMethodWithAnyArgument(mock, method);
		}
	}

}
