package se.mockachino.verifier;

import se.mockachino.CallHandler;
import se.mockachino.MethodCall;
import se.mockachino.invocationhandler.AbstractInvocationHandler;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.matchers.MethodMatcherImpl;
import se.mockachino.util.MockachinoMethod;

public abstract class MatchingHandler extends AbstractInvocationHandler {
	private final CallHandler defaultAnswer;

	protected MatchingHandler(String kind, String id, CallHandler defaultAnswer) {
		super(kind + ":" + id);
		this.defaultAnswer = defaultAnswer;
	}

	public final Object doInvoke(Object o, MockachinoMethod method, Object[] objects) throws Throwable {
		MethodMatcher matcher = new MethodMatcherImpl(method, objects);
		match(o, method, matcher);

		return defaultAnswer.invoke(o, new MethodCall(method, objects));
	}

	protected abstract void match(Object o, MockachinoMethod method, MethodMatcher matcher);
}
