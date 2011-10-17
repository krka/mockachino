package se.mockachino.stub;

import se.mockachino.CallHandler;
import se.mockachino.matchers.MethodMatcher;

public class MethodStub {
	private final CallHandler answer;
	private final MethodMatcher matcher;

	public MethodStub(CallHandler answer, MethodMatcher matcher) {
		this.answer = answer;
		this.matcher = matcher;
	}

	public CallHandler getAnswer() {
		return answer;
	}

	public MethodMatcher getMatcher() {
		return matcher;
	}
}
