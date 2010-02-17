package se.mockachino.stub;

import se.mockachino.CallHandler;
import se.mockachino.MockData;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.util.MockachinoMethod;
import se.mockachino.verifier.MatchingHandler;

public class StubAnswerHandler extends MatchingHandler {
	private final CallHandler answer;
	private final MockData data;

	public StubAnswerHandler(CallHandler answer, Object mock, MockData data) {
		super("StubAnswerHandler", mock.toString());
		this.answer = answer;
		this.data = data;
	}

	@Override
	public void match(Object o, MockachinoMethod method, MethodMatcher matcher) {
		data.getExpectations(method).add(new AnswerStub(answer, matcher));
	}
}
