package se.mockachino.proxy;

import net.sf.cglib.proxy.Enhancer;
import se.mockachino.ProxyMetadata;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class CglibAsmTester {
	private final static Class[] classes = {ProxyMetadata.class};
	static <T> T getCglibProxy(Class<T> clazz, final InvocationHandler handler) {
		net.sf.cglib.proxy.InvocationHandler callback = new net.sf.cglib.proxy.InvocationHandler() {
			@Override
			public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
				return handler.invoke(o, method, objects);
			}
		};
		return (T) Enhancer.create(clazz, classes, callback);
	}
}