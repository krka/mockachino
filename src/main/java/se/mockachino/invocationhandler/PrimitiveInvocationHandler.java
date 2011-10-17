package se.mockachino.invocationhandler;

import se.mockachino.CallHandler;
import se.mockachino.MethodCall;
import se.mockachino.Primitives;

public class PrimitiveInvocationHandler implements CallHandler {
	@Override
	public Object invoke(Object obj, MethodCall call) throws Throwable {
		return Primitives.forType(call.getMethod().getReturnType());
	}
}
