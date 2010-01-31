package se.mockachino.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyUtil {
	public static <T> T newProxy(Class<T> iface, InvocationHandler handler) {
		return (T) Proxy.newProxyInstance(iface.getClassLoader(), new Class<?>[]{iface}, handler);
	}
}
