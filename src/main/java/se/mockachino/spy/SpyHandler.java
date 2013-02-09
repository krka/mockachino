package se.mockachino.spy;

import se.mockachino.CallHandler;
import se.mockachino.MethodCall;

public class SpyHandler<T> implements CallHandler<T> {
	private final Object spy;

	public SpyHandler(Object spy) {
		this.spy = spy;
	}

	@Override
	public T invoke(Object obj, MethodCall<T> call) throws Throwable {
		return call.getMethod().invoke(spy, call.getArguments());
	}
}
