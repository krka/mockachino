package se.mockachino.expectations;

import se.mockachino.matchers.MethodMatcher;
import se.mockachino.MethodCall;

public class MethodStub implements MethodExpectation {
	private final Object returnValue;
	private final MethodMatcher matcher;

	public MethodStub(Object returnValue, MethodMatcher matcher) {
		this.returnValue = returnValue;
		this.matcher = matcher;
	}

	public boolean matches(MethodCall call) {
		return matcher.matches(call);
	}

	public Object getValue() {
		return returnValue;
	}
}
