package se.mockachino.stub;

import se.mockachino.MockData;
import se.mockachino.stub.MethodStub;
import se.mockachino.verifier.VerificationHandler;
import se.mockachino.matchers.MethodMatcher;

public class Stubber<T> extends VerificationHandler {
	private final Object returnValue;
	private final MockData<T> data;

	public Stubber(Object returnValue, T mock, MockData<T> data) {
		super("Stubber", mock.toString());
		this.returnValue = returnValue;
		this.data = data;
	}

	public void verify(Object o, MethodMatcher matcher) {
		data.getExpectations(matcher.getMethod()).add(new MethodStub(returnValue, matcher));
	}
}
