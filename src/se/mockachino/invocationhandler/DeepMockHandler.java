package se.mockachino.invocationhandler;

import se.mockachino.CallHandler;
import se.mockachino.MethodCall;
import se.mockachino.MockContext;
import se.mockachino.Mockachino;
import se.mockachino.Settings;
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
			context.stubReturn(returnValue).onMethodWithAnyArgument(obj, method);
			return returnValue;
		}
		return delegate.invoke(obj, call);
	}
}