package se.mockachino.verifier;

import se.mockachino.Primitives;
import se.mockachino.invocationhandler.AbstractInvocationHandler;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.util.MockachinoMethod;

public abstract class MatchingHandler extends AbstractInvocationHandler {
	protected MatchingHandler(String kind, String id) {
		super(kind + ":" + id);
	}

	public final Object doInvoke(Object o, MockachinoMethod method, Object[] objects) throws Throwable {
		MethodMatcher matcher = new MethodMatcher(method, objects);
		match(o, matcher);

		return Primitives.forType(method.getReturnType());
	}

	protected abstract void match(Object o, MethodMatcher matcher);
}
