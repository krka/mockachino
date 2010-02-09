package se.mockachino.listener;

import se.mockachino.MockData;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.verifier.MatchingHandler;

public class AddListenerHandler extends MatchingHandler {
	private final MockData mockData;
	private final Object mock;
	private final MethodCallListener listener;

	public AddListenerHandler(MockData mockData, Object mock, MethodCallListener listener) {
		super("Listener", mock.toString());
		this.mockData = mockData;
		this.mock = mock;
		this.listener = listener;
	}

	@Override
	public void match(Object o, MethodMatcher matcher) {
		mockData.getListeners(matcher.getMethod()).add(new MethodListener(mock, listener, matcher));
	}
}
