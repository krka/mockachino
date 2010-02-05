package se.mockachino.invocationhandler;

import se.mockachino.cleaner.StacktraceCleaner;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public abstract class AbstractInvocationHandler implements InvocationHandler {
	private static final Method EQUALS = find("equals");
	private static final Method HASHCODE = find("hashCode");
	public static final Method TOSTRING = find("toString");

	private final String name;

	protected AbstractInvocationHandler(String name) {
		this.name = name;
	}

	public final Object invoke(Object o, Method method, Object[] objects) throws Throwable {
		if (equals(method, EQUALS)) {
			return o == objects[0];
		}
		if (equals(method, HASHCODE)) {
			return System.identityHashCode(o);
		}
		if (equals(method, TOSTRING)) {
			return name;
		}
		try {
			return doInvoke(o, method, objects);
		} catch (Throwable throwable) {
			throw StacktraceCleaner.cleanError(throwable);
		}
	}

	protected abstract Object doInvoke(Object o, Method method, Object[] objects) throws Throwable;

	private static Method find(String name) {
		for (Method method : Object.class.getMethods()) {
			if (method.getName().equals(name)) {
				return method;
			}
		}
		return null;
	}

	private static boolean equals(Method a, Method b) {
		if (!a.getName().equals(b.getName())) {
			return false;
		}
		Class<?>[] paramsA = a.getParameterTypes();
		Class<?>[] paramsB = b.getParameterTypes();
		int n = paramsA.length;
		if (n != paramsB.length) {
			return false;
		}
		for (int i = n - 1; i >= 0; i--) {
			if (paramsA[i] != paramsB[i]) {
				return false;
			}
		}
		return true;
	}
}

