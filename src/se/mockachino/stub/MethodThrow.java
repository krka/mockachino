package se.mockachino.stub;

import se.mockachino.MethodCall;
import se.mockachino.expectations.MethodExpectation;
import se.mockachino.matchers.MethodMatcher;

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

	@Override
	public Object invoke(Object mock, MethodCall call) throws Throwable {
		throw e;
	}
}