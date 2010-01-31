package se.mockachino.expectations;

import se.mockachino.matchers.MethodMatcher;
import se.mockachino.MethodCall;

public class MethodThrow implements MethodExpectation {
	private final Throwable e;
	private final MethodMatcher matcher;

	public MethodThrow(Throwable e, MethodMatcher matcher) {
		this.e = e;
		this.matcher = matcher;
	}

	public boolean matches(MethodCall call) {
		return matcher.matches(call);
	}

	public Object getValue() throws Throwable {
		throw e;
	}
}