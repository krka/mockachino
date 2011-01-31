package se.mockachino.mock;

import se.mockachino.CallHandler;
import se.mockachino.Invocation;
import se.mockachino.MockData;
import se.mockachino.Mockachino;
import se.mockachino.exceptions.UsageError;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.matchers.MethodMatcherImpl;
import se.mockachino.util.MockachinoMethod;

public class WhenStubber<T> {
	static final ThreadLocal<Invocation> lastInvocation = new ThreadLocal<Invocation>();

	private final MockData<Object> data;
	private final Object mock;
	private final MockachinoMethod method;
	private final MethodMatcher matcher;

	public WhenStubber() {
		Invocation invocation = lastInvocation.get();
		if (invocation == null) {
			throw new UsageError("Illegal when(X).thenReturn(..) expression. X needs to be on the form mock.method(...)");
		}
		lastInvocation.set(null);

		mock = invocation.getObject();

		// Ensure that the mock is actually a mock
		data = Mockachino.getData(mock);

		method = invocation.getMethodCall().getMethod();
		matcher = new MethodMatcherImpl(method, invocation.getMethodCall().getArguments());

		// This invocation is a stubbing, not a call
		data.deleteLastInvocation();
	}

	public void thenReturn(T value) {
		Mockachino.stubReturn(value).onMethod(mock, method, matcher);
	}

	public void thenReturn(T value, T... values) {
		Mockachino.stubReturn(value, values).onMethod(mock, method, matcher);
	}

	public void thenAnswer(CallHandler answer) {
		Mockachino.stubAnswer(answer).onMethod(mock, method, matcher);
	}

	public void thenThrow(Throwable t) {
		Mockachino.stubThrow(t).onMethod(mock, method, matcher);
	}

}
