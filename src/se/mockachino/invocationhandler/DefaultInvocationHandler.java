package se.mockachino.invocationhandler;

import se.mockachino.CallHandler;
import se.mockachino.MethodCall;
import se.mockachino.MockContext;
import se.mockachino.MockData;
import se.mockachino.Primitives;
import se.mockachino.util.MockachinoMethod;

public class DefaultInvocationHandler implements CallHandler {
	@Override
	public Object invoke(Object obj, MethodCall call) throws Throwable {
		return Primitives.forType(call.getMethod().getReturnType());
	}
}
