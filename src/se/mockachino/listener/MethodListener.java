package se.mockachino.listener;

import se.mockachino.CallHandler;
import se.mockachino.MethodCall;
import se.mockachino.matchers.MethodMatcher;

public class MethodListener {
	private final Object mock;
	private final CallHandler listener;
	private final MethodMatcher matcher;

	public MethodListener(Object mock, CallHandler listener, MethodMatcher matcher) {
		this.mock = mock;
		this.listener = listener;
		this.matcher = matcher;
	}

	public void invoke(MethodCall call) {
		if (matcher.matches(call)) {
			try {
				listener.invoke(mock, call);
			} catch (Throwable throwable) {
			}
		}
	}

}
