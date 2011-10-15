package se.mockachino.proxy;

import se.mockachino.MockData;
import se.mockachino.Mockachino;
import se.mockachino.ProxyMetadata;
import se.mockachino.cleaner.StacktraceCleaner;
import se.mockachino.exceptions.UsageError;
import se.mockachino.matchers.MatcherThreadHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ProxyUtil {
	private static final boolean USE_CGLIB = canUseCgLib();

	private static boolean canUseCgLib() {
		try {
			Class.forName("net.sf.cglib.proxy.Enhancer");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	public static Object newProxy(Class<?> clazz, InvocationHandler handler) {
        Set<Class<?>> interfaces = new HashSet<Class<?>>(Arrays.asList(clazz.getInterfaces()));

        if (clazz.isInterface()) {
            return newInterfaceProxy(clazz, handler, interfaces);
        }
        while (clazz != Object.class &&
                (clazz.isSynthetic() ||
                        clazz.isAnonymousClass() ||
                        clazz.getName().startsWith("$Proxy") ||
                        Modifier.isFinal(clazz.getModifiers()) ||
                        Modifier.isNative(clazz.getModifiers()) ||
                        clazz == Proxy.class)) {

            clazz = clazz.getSuperclass();
        }
        if (clazz == Object.class && !interfaces.isEmpty()) {
            return newInterfaceProxy(null, handler, interfaces);
        }

		if (!USE_CGLIB) {
			throw new UsageError("Only interfaces can be mocked without cglib and asm installed");
		}
		try {
			return CglibAsmUtil.getCglibProxy(clazz, handler, interfaces);
		} catch (Exception e) {
			throw clean(new RuntimeException(e));
		}
	}

    private static Object newInterfaceProxy(Class<?> clazz, InvocationHandler handler, Set<Class<?>> interfaces) {
        if (clazz != null) {
            interfaces.add(clazz);
        }
        interfaces.add(ProxyMetadata.class);
        Class<?>[] classes = new Class<?>[interfaces.size()];
        int i = 0;
        for (Class<?> extraInterface : interfaces) {
            classes[i] = extraInterface;
            i++;
        }
        return Proxy.newProxyInstance(ProxyUtil.class.getClassLoader(), classes, handler);
    }

    private static <T extends Throwable> T clean(T e) {
		return StacktraceCleaner.cleanError(e);
	}

	public static boolean canMock(Class clazz) {
		if (clazz.isInterface()) {
			return true;
		}
		int modifiers = clazz.getModifiers();
		if ((modifiers & Modifier.FINAL) != 0) {
			return false;
		}
		if ((modifiers & Modifier.ABSTRACT) != 0) {
			return false;
		}
		if (clazz.isArray() ||
				clazz.isEnum() ||
				clazz.isAnonymousClass() ||
				clazz.isSynthetic()
				) {
			return false;
		}

		if (USE_CGLIB) {
			return true;
		}
		return false;
	}

	/**
	 * Generic method for creating a proxy for a mock object.
	 * <p/>
	 * Probably not interesting for regular users of Mockachino
	 */
	public static <T> T createProxy(T mock, InvocationHandler handler) {
		MatcherThreadHandler.assertEmpty();
		MockData data = Mockachino.getData(mock);
		Class<T> iface = data.getInterface();
		return (T) newProxy(iface, handler);
	}
}
