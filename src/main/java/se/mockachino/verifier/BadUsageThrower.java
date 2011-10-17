package se.mockachino.verifier;

import se.mockachino.exceptions.UsageError;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class BadUsageThrower implements InvocationHandler {
	private final String message;

	public BadUsageThrower(String message) {
		this.message = message;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		throw new UsageError(message);
	}
}
