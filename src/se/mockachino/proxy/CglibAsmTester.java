package se.mockachino.proxy;

import net.sf.cglib.proxy.Enhancer;
import se.mockachino.ProxyMetadata;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Set;

public class CglibAsmTester {
	private final static Class[] CLASSES = {ProxyMetadata.class};

	static <T> T getCglibProxy(Class<T> clazz, final InvocationHandler handler, Set<Class<?>> extraInterfaces) {
		net.sf.cglib.proxy.InvocationHandler callback = new net.sf.cglib.proxy.InvocationHandler() {
			@Override
			public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
				return handler.invoke(o, method, objects);
			}
		};
		Class[] classes = CglibAsmTester.CLASSES;
		if (extraInterfaces.size() > 0) {
			classes = new Class[1 + extraInterfaces.size()];
			classes[0] = ProxyMetadata.class;

			int i = 1; 
			for (Class<?> extraInterface : extraInterfaces) {
				classes[i] = extraInterface;
				i++;
			}

		}
		return (T) Enhancer.create(clazz, classes, callback);
	}
}