package se.mockachino.invocationhandler;

import se.mockachino.MockData;
import se.mockachino.expectations.MethodThrow;
import se.mockachino.matchers.MethodMatcher;

public class Thrower<T> extends VerificationHandler {
	private final Throwable e;
	private final MockData<T> data;


	public Thrower(Throwable e, MockData<T> data) {
		super("Thrower");
		this.e = e;
		this.data = data;
	}

	public void verify(Object o, MethodMatcher matcher) {
		data.getExpectations(matcher.getMethod()).add(new MethodThrow(e, matcher));
	}
}
