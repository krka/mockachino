package se.mockachino.mock;

import se.mockachino.MethodCall;
import se.mockachino.Mock;
import se.mockachino.MockContext;
import se.mockachino.MockData;
import se.mockachino.cleaner.StacktraceCleaner;
import se.mockachino.expectations.MethodExpectation;
import se.mockachino.expectations.MethodExpectations;
import se.mockachino.invocationhandler.AbstractInvocationHandler;
import se.mockachino.listener.MethodListener;
import se.mockachino.util.MockachinoMethod;

import java.util.List;

public class MockHandler<T> extends AbstractInvocationHandler {
	private static final MockachinoMethod GET_CONTEXT = MockachinoMethod.find(Mock.class, "mockachino_getContext");
	private static final MockachinoMethod GET_MOCKDATA = MockachinoMethod.find(Mock.class, "mockachino_getMockData");

	private final MockContext context;
	private final T impl;
	private final MockData<T> mockData;

	public MockHandler(MockContext context, T impl, String kind, String type, String id, MockData<T> mockData) {
		super(kind + ":" + type + ":" + id);
		this.context = context;
		this.impl = impl;
		this.mockData = mockData;
	}

	public Object doInvoke(Object o, MockachinoMethod method, Object[] objects) throws Throwable {
		if (method.equals(GET_CONTEXT)) {
			return context;
		}
		if (method.equals(GET_MOCKDATA)) {
			return mockData;
		}
		MethodCall methodCall = mockData.addCall(context, method, objects, StacktraceCleaner.cleanError(new Throwable()).getStackTrace());

		List<MethodListener> listeners = mockData.getListeners(method);
		for (MethodListener listener : listeners) {
			listener.invoke(methodCall);
		}

		MethodExpectations methodExpectations = mockData.getExpectations(method);
		MethodExpectation expectation = methodExpectations.findMatch(methodCall);
		if (expectation != null) {
			return expectation.getValue(methodCall);
		}
		return method.invoke(impl, objects);
	}
}
