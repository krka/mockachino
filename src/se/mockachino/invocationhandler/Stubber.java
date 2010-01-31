package se.mockachino.invocationhandler;

import se.mockachino.MockData;
import se.mockachino.expectations.MethodStub;
import se.mockachino.matchers.MethodMatcher;

public class Stubber<T> extends VerificationHandler {
	private final Object returnValue;
	private final MockData<T> data;

	public Stubber(Object returnValue, MockData<T> data) {
		super("Stubber");
		this.returnValue = returnValue;
		this.data = data;
	}

	public void verify(Object o, MethodMatcher matcher) {
		data.getExpectations(matcher.getMethod()).add(new MethodStub(returnValue, matcher));
	}
}
