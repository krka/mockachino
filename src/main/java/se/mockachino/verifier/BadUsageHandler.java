package se.mockachino.verifier;

import se.mockachino.CallHandler;
import se.mockachino.MethodCall;

public class BadUsageHandler<T> implements CallHandler<T> {
	private final BadUsageBuilder builder;

	public BadUsageHandler(BadUsageBuilder builder) {
		this.builder = builder;
	}

	@Override
	public T invoke(Object obj, MethodCall<T> call) throws Throwable {
		return builder.forClass(call.getMethod().getReturnType());
	}
}
