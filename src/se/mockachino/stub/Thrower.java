package se.mockachino.stub;

import se.mockachino.MockData;
import se.mockachino.stub.MethodThrow;
import se.mockachino.verifier.VerificationHandler;
import se.mockachino.matchers.MethodMatcher;

public class Thrower<T> extends VerificationHandler {
	private final Throwable e;
	private final MockData<T> data;


	public Thrower(Throwable e, T mock, MockData<T> data) {
		super("Thrower", mock.toString());
		this.e = e;
		this.data = data;
	}

	public void verify(Object o, MethodMatcher matcher) {
		data.getExpectations(matcher.getMethod()).add(new MethodThrow(e, matcher));
	}
}
