package se.mockachino.stub;

import se.mockachino.CallHandler;
import se.mockachino.matchers.MethodMatcher;

public class MethodStub<T> {
	private final CallHandler<T> answer;
	private final MethodMatcher<T> matcher;

	public MethodStub(CallHandler<T> answer, MethodMatcher<T> matcher) {
		this.answer = answer;
		this.matcher = matcher;
	}

	public CallHandler<T> getAnswer() {
		return answer;
	}

	public MethodMatcher<T> getMatcher() {
		return matcher;
	}
}
