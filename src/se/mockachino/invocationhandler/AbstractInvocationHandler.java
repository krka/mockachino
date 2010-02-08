package se.mockachino.invocationhandler;

import se.mockachino.cleaner.StacktraceCleaner;
import se.mockachino.util.MockachinoMethod;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public abstract class AbstractInvocationHandler implements InvocationHandler {
	private final String name;

	protected AbstractInvocationHandler(String name) {
		this.name = name;
	}

	public final Object invoke(Object o, Method reflectMethod, Object[] objects) throws Throwable {
		MockachinoMethod method = new MockachinoMethod(reflectMethod);
		if (method.equals(MockachinoMethod.EQUALS)) {
			return o == objects[0];
		}
		if (method.equals(MockachinoMethod.HASHCODE)) {
			return System.identityHashCode(o);
		}
		if (method.equals(MockachinoMethod.TOSTRING)) {
			return name;
		}
		try {
			return doInvoke(o, method, objects);
		} catch (Throwable throwable) {
			throw StacktraceCleaner.cleanError(throwable);
		}
	}

	protected abstract Object doInvoke(Object o, MockachinoMethod method, Object[] objects) throws Throwable;
}

