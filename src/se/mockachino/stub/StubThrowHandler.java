package se.mockachino.stub;

import se.mockachino.MockData;
import se.mockachino.stub.MethodThrow;
import se.mockachino.verifier.MatchingHandler;
import se.mockachino.matchers.MethodMatcher;

public class StubThrowHandler<T> extends MatchingHandler {
	private final Throwable e;
	private final MockData<T> data;


	public StubThrowHandler(Throwable e, T mock, MockData<T> data) {
		super("StubThrowHandler", mock.toString());
		this.e = e;
		this.data = data;
	}

	public void match(Object o, MethodMatcher matcher) {
		data.getExpectations(matcher.getMethod()).add(new MethodThrow(e, matcher));
	}
}
