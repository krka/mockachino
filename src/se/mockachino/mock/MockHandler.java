package se.mockachino.mock;

import se.mockachino.CallHandler;
import se.mockachino.MethodCall;
import se.mockachino.MockContext;
import se.mockachino.MockData;
import se.mockachino.ProxyMetadata;
import se.mockachino.cleaner.StacktraceCleaner;
import se.mockachino.expectations.MethodStubs;
import se.mockachino.expectations.MethodStub;
import se.mockachino.invocationhandler.AbstractInvocationHandler;
import se.mockachino.observer.MethodObserver;
import se.mockachino.util.MockachinoMethod;

import java.util.List;

public class MockHandler<T> extends AbstractInvocationHandler {
	private static final StackTraceElement[] EMPTY_STACKTRACE = new StackTraceElement[]{};

	private static final MockachinoMethod GET_CONTEXT = MockachinoMethod.find(ProxyMetadata.class, "mockachino_getContext");
	private static final MockachinoMethod GET_MOCKDATA = MockachinoMethod.find(ProxyMetadata.class, "mockachino_getMockData");

	private final MockContext context;
	private final CallHandler fallback;
	private final MockData<T> mockData;
	private final boolean quick;

	public MockHandler(MockContext context, CallHandler fallback, MockData<T> mockData, boolean quick, String name) {
		super(name);
		this.context = context;
		this.fallback = fallback;
		this.mockData = mockData;
		this.quick = quick;
	}

	private MockContext getContext() {
		return context;
	}

	private MockData getMockData() {
		return mockData;
	}

	public Object doInvoke(Object o, MockachinoMethod method, Object[] objects) throws Throwable {
		if (method.equals(GET_CONTEXT)) {
			return getContext();
		}
		if (method.equals(GET_MOCKDATA)) {
			return getMockData();
		}
		MethodCall methodCall = mockData.addCall(context, method, objects, getStackTrace());

		List<MethodObserver> observers = mockData.getObservers(method);
		for (MethodObserver observer : observers) {
			observer.invoke(methodCall);
		}

		MethodStubs methodStubs = mockData.getExpectations(method);
		MethodStub stub = methodStubs.findMatch(methodCall);
		if (stub != null) {
			return stub.invoke(o, methodCall);
		}
		return fallback.invoke(o, methodCall);
	}

	private StackTraceElement[] getStackTrace() {
		if (quick) {
			return EMPTY_STACKTRACE;
		}
		return StacktraceCleaner.cleanError(new Throwable()).getStackTrace();
	}
}
