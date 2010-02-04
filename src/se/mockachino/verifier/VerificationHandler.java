package se.mockachino.verifier;

import se.mockachino.invocationhandler.AbstractInvocationHandler;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.expectations.DefaultMethodExpectations;

import java.lang.reflect.Method;

public abstract class VerificationHandler extends AbstractInvocationHandler {
	protected VerificationHandler(String kind, String id) {
		super(kind + ":" + id);
	}

	public final Object doInvoke(Object o, Method method, Object[] objects) throws Throwable {
		MethodMatcher matcher = new MethodMatcher(method, objects);
		verify(o, matcher);
		return DefaultMethodExpectations.forType(method.getReturnType()).getValue(null);
	}

	protected abstract void verify(Object o, MethodMatcher matcher);
}
