package se.mockachino.verifier;

import se.mockachino.CallHandler;
import se.mockachino.MethodCall;
import se.mockachino.invocationhandler.AbstractInvocationHandler;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.matchers.MethodMatcherImpl;
import se.mockachino.util.MockachinoMethod;

import java.lang.reflect.Type;

public abstract class MatchingHandler<T> extends AbstractInvocationHandler<T> {
	private final CallHandler<T> defaultAnswer;

	protected MatchingHandler(String kind, String id, CallHandler<T> defaultAnswer, Type type) {
		super(kind + ":" + id, type);
		this.defaultAnswer = defaultAnswer;
	}

    @Override
	public final Object doInvoke(Object o, MockachinoMethod<T> method, Object[] objects) throws Throwable {
		MethodMatcher<T> matcher = new MethodMatcherImpl<T>(method, objects);
		match(o, method, matcher);

		return defaultAnswer.invoke(o, new MethodCall<T>(method, objects));
	}

	protected abstract void match(Object o, MockachinoMethod<T> method, MethodMatcher<T> matcher);
}
