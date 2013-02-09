package se.mockachino.observer;

import se.mockachino.CallHandler;
import se.mockachino.MockData;
import se.mockachino.Mockachino;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.util.MockachinoMethod;
import se.mockachino.verifier.MatchingHandler;

public class AddObserverHandler<T> extends MatchingHandler<T> {
	private final MockData<?> mockData;
	private final Object mock;
	private final CallHandler<T> observer;

	public AddObserverHandler(MockData mockData, Object mock, CallHandler<T> observer) {
        super("Observer", mockData.getName(), Mockachino.DEFAULT_VALUES, mockData.getTypeLiteral());
		this.mockData = mockData;
		this.mock = mock;
		this.observer = observer;
	}

	@Override
	public void match(Object o, MockachinoMethod<T> method, MethodMatcher<T> matcher) {
        mockData.getObservers(method).add(new MethodObserver<T>(mock, observer, matcher));
	}
}
