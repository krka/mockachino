package se.mockachino.invocationhandler;

import se.mockachino.expectations.DefaultMethodExpectations;
import se.mockachino.expectations.MethodExpectation;

import java.lang.reflect.Method;

public class DefaultInvocationHandler extends AbstractInvocationHandler {
	public DefaultInvocationHandler() {
		super("Default");
	}

	public Object doInvoke(Object o, Method method, Object[] objects) throws Throwable {
		MethodExpectation methodExpectation = DefaultMethodExpectations.forType(method.getReturnType());
		return methodExpectation.getValue();
	}
}
