package se.mockachino.expectations;

import se.mockachino.MethodCall;

public interface MethodExpectation<T> {
	boolean matches(MethodCall call);

	T getValue() throws Throwable;
}
