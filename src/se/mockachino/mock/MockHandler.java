package se.mockachino.mock;

import se.mockachino.*;
import se.mockachino.cleaner.StacktraceCleaner;
import se.mockachino.invocationhandler.AbstractInvocationHandler;
import se.mockachino.observer.MethodObserver;
import se.mockachino.stub.MethodStub;
import se.mockachino.stub.MethodStubs;
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

	public Object doInvoke(Object o, MockachinoMethod method, Object[] args) throws Throwable {
		if (method.equals(GET_CONTEXT)) {
			return getContext();
		}
		if (method.equals(GET_MOCKDATA)) {
			return getMockData();
		}
        Invocation invocation = mockData.addCall(o, method, args, getStackTrace());
        MethodCall methodCall = invocation.getMethodCall();
        try {

            List<MethodObserver> observers = mockData.getObservers(method);
            for (MethodObserver observer : observers) {
                observer.invoke(methodCall);
            }

            MethodStubs methodStubs = mockData.getStubs(method);
            MethodStub stub = methodStubs.findMatch(methodCall);
            if (stub != null) {
                return stub.getAnswer().invoke(o, methodCall);
            }
            return fallback.invoke(o, methodCall);
        }
        finally {
            WhenStubber.lastInvocation.set(invocation);
        }
	}

	private StackTraceElement[] getStackTrace() {
		if (quick) {
			return EMPTY_STACKTRACE;
		}
		return StacktraceCleaner.cleanError(new Throwable()).getStackTrace();
	}
}
