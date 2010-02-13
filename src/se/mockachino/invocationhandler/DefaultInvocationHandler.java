package se.mockachino.invocationhandler;

import se.mockachino.Primitives;
import se.mockachino.util.MockachinoMethod;

public class DefaultInvocationHandler extends AbstractInvocationHandler {
	public DefaultInvocationHandler() {
		super(null);
	}

	public Object doInvoke(Object o, MockachinoMethod method, Object[] objects) throws Throwable {
		return Primitives.forType(method.getReturnType());
	}
}
