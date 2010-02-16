package se.mockachino.spy;

import se.mockachino.CallHandler;
import se.mockachino.MethodCall;

public class SpyHandler implements CallHandler {
	private final Object spy;

	public SpyHandler(Object spy) {
		this.spy = spy;
	}

	@Override
	public Object invoke(Object obj, MethodCall call) throws Throwable {
		return call.getMethod().invoke(spy, call.getArguments());
	}
}
