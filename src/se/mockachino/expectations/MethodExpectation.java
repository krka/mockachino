package se.mockachino.expectations;

import se.mockachino.MethodCall;

public interface MethodExpectation {
	boolean matches(MethodCall call);

	Object getValue() throws Throwable;
}
