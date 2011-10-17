package se.mockachino.verifier;

import se.mockachino.CallHandler;
import se.mockachino.MethodCall;

public class BadUsageHandler implements CallHandler {
	private final BadUsageBuilder builder;

	public BadUsageHandler(BadUsageBuilder builder) {
		this.builder = builder;
	}

	@Override
	public Object invoke(Object obj, MethodCall call) throws Throwable {
		return builder.forClass(call.getMethod().getReturnType());
	}
}
