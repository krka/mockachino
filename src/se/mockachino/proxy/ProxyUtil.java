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
import java.util.Collections;
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

	public static <T> T newProxy(Class<T> clazz, InvocationHandler handler) {
		return newProxy(clazz, handler, (Set<Class<?>>) Collections.EMPTY_SET);
	}

	public static <T> T newProxy(Class<T> clazz, InvocationHandler handler, Set<Class<?>> extraInterfaces) {
		for (Class<?> extraInterface : extraInterfaces) {
			if (extraInterface == null) {
				throw new UsageError("extra interface can not be null");
			}
			if (!extraInterface.isInterface()) {
				throw new UsageError(extraInterface + " must be an interface");
			}
		}
		if (clazz.isInterface()) {
			Class<?>[] classes = new Class<?>[2 + extraInterfaces.size()];
			classes[0] = clazz;
			classes[1] = ProxyMetadata.class;

			int i = 2;
			for (Class<?> extraInterface : extraInterfaces) {
				classes[i] = extraInterface;
				i++;
			}
			return (T) Proxy.newProxyInstance(ProxyUtil.class.getClassLoader(), classes, handler);
		}
		if (!USE_CGLIB) {
			throw new UsageError("Only interfaces can be mocked without cglib and asm installed");
		}
		try {
			return CglibAsmUtil.getCglibProxy(clazz, handler, extraInterfaces);
		} catch (Exception e) {
			throw clean(new RuntimeException(e));
		}
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
		return newProxy(iface, handler, (Set<Class<?>>) data.getExtraInterfaces());
	}
}
