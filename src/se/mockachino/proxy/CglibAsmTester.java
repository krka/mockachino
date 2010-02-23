package se.mockachino.proxy;

import net.sf.cglib.proxy.Enhancer;
import se.mockachino.Primitives;
import se.mockachino.ProxyMetadata;
import se.mockachino.exceptions.UsageError;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Set;

public class CglibAsmTester {
	private final static Class[] CLASSES = {ProxyMetadata.class};

	static <T> T getCglibProxy(Class<T> clazz, final InvocationHandler handler, Set<Class<?>> extraInterfaces) throws Exception {
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

        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(callback);
        enhancer.setSuperclass(clazz);
        enhancer.setInterfaces(classes);

        Exception constructorError = null;
        for (Constructor<?> constructor : clazz.getConstructors()) {
            Class<?>[] types = constructor.getParameterTypes();
            Object[] args = new Object[types.length];
            for (int i = 0; i < types.length; i++) {
                args[i] = Primitives.forType(types[i]);
            }
            try {
                return (T) enhancer.create(types, args);
            } catch (Exception e) {
                if (constructorError == null) {
                    constructorError = e;
                }
            }
        }
        if (constructorError != null) {
            throw constructorError;
        }
        throw new UsageError("Class " + clazz.getSimpleName() + " has no non-private constructors.");
	}
}