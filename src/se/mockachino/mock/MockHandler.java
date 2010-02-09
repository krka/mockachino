package se.mockachino.mock;

import se.mockachino.MethodCall;
import se.mockachino.MockContext;
import se.mockachino.MockData;
import se.mockachino.cleaner.StacktraceCleaner;
import se.mockachino.expectations.MethodExpectation;
import se.mockachino.expectations.MethodExpectations;
import se.mockachino.invocationhandler.AbstractInvocationHandler;
import se.mockachino.listener.MethodListener;
import se.mockachino.util.MockachinoMethod;

import java.util.List;

public class MockHandler extends AbstractInvocationHandler {
	private final MockContext context;
	private final Object impl;

	public MockHandler(MockContext context, Object impl, String kind, String type, String id) {
		super(kind + ":" + type + ":" + id);
		this.context = context;
		this.impl = impl;
	}

	public Object doInvoke(Object o, MockachinoMethod method, Object[] objects) throws Throwable {
		MockData data = context.getData(o);
		MethodCall methodCall = data.addCall(context, method, objects, StacktraceCleaner.cleanError(new Throwable()).getStackTrace());

		List<MethodListener> listeners = data.getListeners(method);
		for (MethodListener listener : listeners) {
			listener.invoke(methodCall);
		}

		MethodExpectations methodExpectations = data.getExpectations(method);
		MethodExpectation expectation = methodExpectations.findMatch(methodCall);
		if (expectation != null) {
			return expectation.getValue(methodCall);
		}
		return method.invoke(impl, objects);
	}
}
