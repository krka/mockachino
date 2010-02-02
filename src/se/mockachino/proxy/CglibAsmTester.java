package se.mockachino.proxy;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class CglibAsmTester {
	static <T> T getCglibProxy(Class<T> clazz, final InvocationHandler handler) {
		Callback callback = new net.sf.cglib.proxy.InvocationHandler() {
			@Override
			public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
				return handler.invoke(o, method, objects);
			}
		};
		return (T) Enhancer.create(clazz, callback);
	}
}