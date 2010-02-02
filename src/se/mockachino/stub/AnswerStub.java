package se.mockachino.stub;

import se.mockachino.Answer;
import se.mockachino.MethodCall;
import se.mockachino.expectations.MethodExpectation;
import se.mockachino.matchers.MethodMatcher;

public class AnswerStub implements MethodExpectation {
	private final Answer answer;
	private final MethodMatcher matcher;

	public AnswerStub(Answer answer, MethodMatcher matcher) {
		this.answer = answer;
		this.matcher = matcher;
	}

	@Override
	public boolean matches(MethodCall call) {
		return matcher.matches(call);
	}

	@Override
	public Object getValue(MethodCall call) throws Throwable {
		return answer.getValue(call);

	}
}
