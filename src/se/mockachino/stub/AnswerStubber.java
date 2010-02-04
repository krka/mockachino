package se.mockachino.stub;

import se.mockachino.Answer;
import se.mockachino.MockData;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.verifier.VerificationHandler;

import java.lang.reflect.InvocationHandler;

public class AnswerStubber extends VerificationHandler {
	private final Answer answer;
	private final MockData data;

	public AnswerStubber(Answer answer, Object mock, MockData data) {
		super("AnswerStubber", mock.toString());
		this.answer = answer;
		this.data = data;
	}

	@Override
	public void verify(Object o, MethodMatcher matcher) {
		data.getExpectations(matcher.getMethod()).add(new AnswerStub(answer, matcher));
	}
}
