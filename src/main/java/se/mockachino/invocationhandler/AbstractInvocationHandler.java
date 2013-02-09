package se.mockachino.invocationhandler;

import se.mockachino.cleaner.StacktraceCleaner;
import se.mockachino.util.MockachinoMethod;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public abstract class AbstractInvocationHandler<T> implements InvocationHandler {

	protected final String name;
    private final Type type;

    protected AbstractInvocationHandler(String name, Type type) {
		this.name = name;
        this.type = type;
    }

	public final Object invoke(Object o, Method reflectMethod, Object[] objects) throws Throwable {
		MockachinoMethod<T> method = new MockachinoMethod<T>(type, reflectMethod);
		try {
			return defaultToString(method, doInvoke(o, method, objects));
		} catch (Throwable throwable) {
			throw StacktraceCleaner.cleanError(throwable);
		}
	}

	protected Object defaultToString(MockachinoMethod<T> method, Object ret) {
		if (ret == null && method.isToStringCall()) {
			return name;
		}
		return ret;
	}

	protected abstract Object doInvoke(Object o, MockachinoMethod<T> method, Object[] objects) throws Throwable;
}

