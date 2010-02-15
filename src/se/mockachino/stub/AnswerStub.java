package se.mockachino.stub;

import se.mockachino.CallHandler;
import se.mockachino.MethodCall;
import se.mockachino.expectations.MethodExpectation;
import se.mockachino.matchers.MethodMatcher;

public class AnswerStub implements MethodExpectation {
	private final CallHandler answer;
	private final MethodMatcher matcher;

	public AnswerStub(CallHandler answer, MethodMatcher matcher) {
		this.answer = answer;
		this.matcher = matcher;
	}

	@Override
	public boolean matches(MethodCall call) {
		return matcher.matches(call);
	}

	@Override
	public Object invoke(Object mock, MethodCall call) throws Throwable {
		return answer.invoke(mock, call);
	}
}
