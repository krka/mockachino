package se.mockachino.observer;

import se.mockachino.CallHandler;
import se.mockachino.MethodCall;
import se.mockachino.matchers.MethodMatcher;

public class MethodObserver<T> {
	private final Object mock;
	private final CallHandler<T> observer;
	private final MethodMatcher<T> matcher;

	public MethodObserver(Object mock, CallHandler<T> observer, MethodMatcher<T> matcher) {
		this.mock = mock;
		this.observer = observer;
		this.matcher = matcher;
	}

	public void invoke(MethodCall<T> call) {
		if (matcher.matches(call)) {
			try {
				observer.invoke(mock, call);
			} catch (Throwable throwable) {
			}
		}
	}

}
