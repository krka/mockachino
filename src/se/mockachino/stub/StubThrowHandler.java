package se.mockachino.stub;

import se.mockachino.MockContext;
import se.mockachino.MockData;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.verifier.MatchingHandler;

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
