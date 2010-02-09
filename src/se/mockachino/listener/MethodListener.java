package se.mockachino.listener;

import se.mockachino.MethodCall;
import se.mockachino.matchers.MethodMatcher;

public class MethodListener {
	private final Object mock;
	private final MethodCallListener listener;
	private final MethodMatcher matcher;

	public MethodListener(Object mock, MethodCallListener listener, MethodMatcher matcher) {
		this.mock = mock;
		this.listener = listener;
		this.matcher = matcher;
	}

	public void invoke(MethodCall call) {
		if (matcher.matches(call)) {
			listener.listen(mock, call);
		}
	}

}
