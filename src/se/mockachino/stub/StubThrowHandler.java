package se.mockachino.stub;

import se.mockachino.MockData;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.util.MockachinoMethod;
import se.mockachino.verifier.MatchingHandler;

public class StubThrowHandler<T> extends MatchingHandler {
	private final Throwable e;
	private final MockData<T> data;


	public StubThrowHandler(Throwable e, T mock, MockData<T> data) {
		super("StubThrowHandler", mock.toString());
		this.e = e;
		this.data = data;
	}

	@Override
	public void match(Object o, MockachinoMethod method, MethodMatcher matcher) {
		data.getExpectations(method).add(new ExceptionStub(e, matcher));
	}
}
