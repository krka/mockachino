package se.mockachino.observer;

import se.mockachino.CallHandler;
import se.mockachino.MethodCall;
import se.mockachino.matchers.MethodMatcher;

public class MethodObserver {
	private final Object mock;
	private final CallHandler observer;
	private final MethodMatcher matcher;

	public MethodObserver(Object mock, CallHandler observer, MethodMatcher matcher) {
		this.mock = mock;
		this.observer = observer;
		this.matcher = matcher;
	}

	public void invoke(MethodCall call) {
		if (matcher.matches(call)) {
			try {
				observer.invoke(mock, call);
			} catch (Throwable throwable) {
			}
		}
	}

}
