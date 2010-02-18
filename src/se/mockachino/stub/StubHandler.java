package se.mockachino.stub;

import se.mockachino.CallHandler;
import se.mockachino.MockData;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.util.MockachinoMethod;
import se.mockachino.verifier.MatchingHandler;

public class StubHandler extends MatchingHandler {
	private final CallHandler answer;
	private final MockData data;
	private final StubVerifier verifier;

	public StubHandler(CallHandler answer, Object mock, MockData data, StubVerifier verifier) {
		super("StubHandler", mock.toString());
		this.answer = answer;
		this.data = data;
		this.verifier = verifier;
	}

	@Override
	public void match(Object o, MockachinoMethod method, MethodMatcher matcher) {
		verifier.verify(method);
		data.getStubs(method).add(new MethodStub(answer, matcher));
	}
}
