package se.mockachino.expectations;

import se.mockachino.MethodCall;

public interface MethodExpectation<T> {
	boolean matches(MethodCall call);

	T getValue(MethodCall call) throws Throwable;
}
