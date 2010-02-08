package se.mockachino.stub;

import se.mockachino.Answer;
import se.mockachino.MockData;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.verifier.MatchingHandler;

public class StubAnswerHandler extends MatchingHandler {
	private final Answer answer;
	private final MockData data;

	public StubAnswerHandler(Answer answer, Object mock, MockData data) {
		super("StubAnswerHandler", mock.toString());
		this.answer = answer;
		this.data = data;
	}

	@Override
	public void match(Object o, MethodMatcher matcher) {
		data.getExpectations(matcher.getMethod()).add(new AnswerStub(answer, matcher));
	}
}
