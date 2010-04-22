package se.mockachino.invocationhandler;

import se.mockachino.CallHandler;
import se.mockachino.MethodCall;
import se.mockachino.MockContext;
import se.mockachino.Mockachino;
import se.mockachino.Settings;
import se.mockachino.matchers.MethodMatcherImpl;
import se.mockachino.util.MockachinoMethod;

public class DeepMockHandler implements CallHandler {
	private final MockContext context;
	private final CallHandler delegate;

	public DeepMockHandler(MockContext context, CallHandler delegate) {
		this.context = context;
		this.delegate = delegate;
	}

	@Override
	public Object invoke(Object obj, MethodCall call) throws Throwable {
		MockachinoMethod method = call.getMethod();
		Class returnType = method.getReturnType();
		if (Mockachino.canMock(returnType)) {
			Object returnValue = context.mock(returnType, Settings.fallback(this));
			Mockachino.stubReturn(returnValue).onMethod(obj, method, new MethodMatcherImpl(method, call.getArguments()));
			return returnValue;
		}
		return delegate.invoke(obj, call);
	}
}