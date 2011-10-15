package se.mockachino.stub;

import se.mockachino.CallHandler;
import se.mockachino.MockData;
import se.mockachino.VerifyableCallHandler;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.util.MockachinoMethod;
import se.mockachino.verifier.BadUsageBuilder;
import se.mockachino.verifier.BadUsageHandler;
import se.mockachino.verifier.MatchingHandler;

public class StubHandler extends MatchingHandler {
	private static final BadUsageHandler BAD_USAGE_HANDLER = new BadUsageHandler(
			new BadUsageBuilder(
					"Incorrect usage. You can not chain calls when stubbing a deep mock. " +
							"You probably used stubReturn(value).on(mock).method1().method2(). " +
							"Correct usage is stubReturn(value).on(mock.method1()).method2()"));


	private final VerifyableCallHandler answer;
	private final MockData data;

	public StubHandler(VerifyableCallHandler answer, MockData data) {
		super("StubHandler", data.getName(), BAD_USAGE_HANDLER);
		this.answer = answer;
		this.data = data;
	}

	@Override
	public void match(Object o, MockachinoMethod method, MethodMatcher matcher) {
        answer.verify(method);
		data.getStubs(method).add(new MethodStub(answer, matcher));
	}
}
