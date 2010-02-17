package se.mockachino.invocationhandler;

import se.mockachino.cleaner.StacktraceCleaner;
import se.mockachino.util.MockachinoMethod;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public abstract class AbstractInvocationHandler implements InvocationHandler {

	protected final String name;

	protected AbstractInvocationHandler(String name) {
		this.name = name;
	}

	public final Object invoke(Object o, Method reflectMethod, Object[] objects) throws Throwable {
		MockachinoMethod method = new MockachinoMethod(reflectMethod);
		try {
			return defaultToString(method, doInvoke(o, method, objects));
		} catch (Throwable throwable) {
			throw StacktraceCleaner.cleanError(throwable);
		}
	}

	protected Object defaultToString(MockachinoMethod method, Object ret) {
		if (ret == null && method.equals(MockachinoMethod.TOSTRING)) {
			return name;
		}
		return ret;
	}

	protected abstract Object doInvoke(Object o, MockachinoMethod method, Object[] objects) throws Throwable;
}

