package se.mockachino.observer;

import se.mockachino.CallHandler;
import se.mockachino.MockData;
import se.mockachino.Mockachino;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.util.MockachinoMethod;
import se.mockachino.verifier.MatchingHandler;

public class AddObserverHandler extends MatchingHandler {
	private final MockData mockData;
	private final Object mock;
	private final CallHandler observer;

	public AddObserverHandler(MockData mockData, Object mock, CallHandler observer) {
		super("Observer", mockData.getName(), Mockachino.DEFAULT_VALUES, mockData.getTypeLiteral());
		this.mockData = mockData;
		this.mock = mock;
		this.observer = observer;
	}

	@Override
	public void match(Object o, MockachinoMethod method, MethodMatcher matcher) {
		mockData.getObservers(method).add(new MethodObserver(mock, observer, matcher));
	}
}
