package se.mockachino.proxy;

import se.mockachino.ProxyMetadata;
import se.mockachino.cleaner.StacktraceCleaner;
import se.mockachino.exceptions.UsageError;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

public class ProxyUtil {
	private static final boolean CGLIB_FOR_INTERFACES = false;

	private static final boolean USE_CGLIB = canUseCgLib();
	private static boolean canUseCgLib() {
		try {
			Class.forName("net.sf.cglib.proxy.Enhancer");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	private static final boolean useJavaReflect = !(USE_CGLIB && CGLIB_FOR_INTERFACES);

	public static <T> T newProxy(Class<T> clazz, final InvocationHandler handler) {
		if (useJavaReflect && clazz.isInterface()) {
			return (T) Proxy.newProxyInstance(ProxyUtil.class.getClassLoader(), new Class<?>[]{clazz, ProxyMetadata.class}, handler);
		}
		if (!USE_CGLIB) {
			throw new UsageError("Only interfaces can be mocked without cglib and asm installed");
		}
		try {
			return CglibAsmTester.getCglibProxy(clazz, handler);
		} catch (RuntimeException e) {
			throw clean(e);
		}
	}

	private static <T extends Throwable> T clean(T e) {
		return StacktraceCleaner.cleanError(e);
	}

	public static boolean canMock(Class clazz) {
		if (useJavaReflect && clazz.isInterface()) {
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
}
