package se.mockachino.observer;

import se.mockachino.CallHandler;
import se.mockachino.MockData;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.util.MockachinoMethod;
import se.mockachino.verifier.BadUsageBuilder;
import se.mockachino.verifier.BadUsageHandler;
import se.mockachino.verifier.MatchingHandler;

public class AddObserverHandler extends MatchingHandler {
	private static final BadUsageHandler BAD_USAGE_HANDLER = new BadUsageHandler(
			new BadUsageBuilder(
					"Incorrect usage. " +
					"You probably used Mockachino.observeWith(observer).on(mock).method1().method2(). " +
					"You should only use it as Mockachino.observeWith(observer).on(mock).method1()"));


	private final MockData mockData;
	private final Object mock;
	private final CallHandler observer;

	public AddObserverHandler(MockData mockData, Object mock, CallHandler observer) {
		super("Observer", mockData.getName(), BAD_USAGE_HANDLER, mockData.getTypeLiteral());
		this.mockData = mockData;
		this.mock = mock;
		this.observer = observer;
	}

	@Override
	public void match(Object o, MockachinoMethod method, MethodMatcher matcher) {
		mockData.getObservers(method).add(new MethodObserver(mock, observer, matcher));
	}
}
