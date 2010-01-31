package se.mockachino.invocationhandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public abstract class AbstractInvocationHandler implements InvocationHandler {
	private final String kind;

	protected AbstractInvocationHandler(String kind) {
		this.kind = kind;
	}
	public final Object invoke(Object o, Method method, Object[] objects) throws Throwable {
		//System.out.println("method:" + method + ", " + Formatting.list(objects));
		if (method.getName().equals("equals") && method.getParameterTypes().length == 1) {
			return o == objects[0];
		}
		if (method.getName().equals("hashCode") && method.getParameterTypes().length == 0) {
			return System.identityHashCode(o);
		}
		if (method.getName().equals("toString") && method.getParameterTypes().length == 0) {
			return "Mockachino" + kind + ":" + System.identityHashCode(o);
		}
		return doInvoke(o, method, objects);
	}

	public abstract Object doInvoke(Object o, Method method, Object[] objects) throws Throwable;
}

