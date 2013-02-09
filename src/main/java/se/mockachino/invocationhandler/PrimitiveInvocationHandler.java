package se.mockachino.invocationhandler;

import se.mockachino.CallHandler;
import se.mockachino.MethodCall;
import se.mockachino.Primitives;

public class PrimitiveInvocationHandler<T> implements CallHandler<T> {
	@Override
	public T invoke(Object obj, MethodCall call) throws Throwable {
		return (T) Primitives.forType(call.getMethod().getReturnType());
	}
}
