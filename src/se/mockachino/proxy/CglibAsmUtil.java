package se.mockachino.proxy;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import se.mockachino.Primitives;
import se.mockachino.ProxyMetadata;
import se.mockachino.exceptions.UsageError;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Set;

public class CglibAsmUtil {
	private final static Class[] CLASSES = {ProxyMetadata.class};
	private static final boolean HAS_OBJENESIS = hasObjenesis();

	private static boolean hasObjenesis() {
		try {
			Class<?> clazz = Class.forName("org.objenesis.ObjenesisStd");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	static <T> T getCglibProxy(Class<T> clazz, final InvocationHandler handler, Set<Class<?>> extraInterfaces) throws Exception {
		net.sf.cglib.proxy.InvocationHandler callback = new net.sf.cglib.proxy.InvocationHandler() {
			@Override
			public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
				return handler.invoke(o, method, objects);
			}
		};
        extraInterfaces.add(ProxyMetadata.class);
        Class[] classes = new Class[extraInterfaces.size()];
        int i = 0;
        for (Class<?> extraInterface : extraInterfaces) {
            classes[i] = extraInterface;
            i++;
        }

		Enhancer enhancer = new Enhancer() {
			@Override
			@SuppressWarnings("unchecked")
			protected void filterConstructors(Class sc, List constructors) {
			}
		};
		enhancer.setSuperclass(clazz);
		enhancer.setInterfaces(classes);

		if (HAS_OBJENESIS) {
			enhancer.setUseFactory(true);
			enhancer.setCallbackTypes(new Class[]{net.sf.cglib.proxy.InvocationHandler.class});
			Factory factory = (Factory) ObjenesisUtil.newInstance(enhancer.createClass());
			factory.setCallbacks(new Callback[]{callback});
			return (T) factory;
		}

		enhancer.setCallback(callback);

		Exception constructorError = null;
		for (Constructor<?> constructor : clazz.getConstructors()) {
			Class<?>[] types = constructor.getParameterTypes();
			Object[] args = new Object[types.length];
			for (i = 0; i < types.length; i++) {
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
