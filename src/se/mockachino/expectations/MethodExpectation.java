package se.mockachino.expectations;

import se.mockachino.CallHandler;
import se.mockachino.MethodCall;

public interface MethodExpectation<T> extends CallHandler {
	boolean matches(MethodCall call);
}
