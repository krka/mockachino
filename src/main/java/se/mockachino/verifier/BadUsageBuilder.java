package se.mockachino.verifier;

import se.mockachino.Primitives;
import se.mockachino.proxy.ProxyUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class BadUsageBuilder {
	private final Map<Class<?>, Object> proxys = new HashMap<Class<?>, Object>();
	private final String message;

	public BadUsageBuilder(String message) {
		this.message = message;
	}

	public synchronized <T> T forClass(Class<T> returnType) {
		T proxy = (T) proxys.get(returnType);
		if (proxy == null) {
			if (!ProxyUtil.canMock(returnType)) {
				return Primitives.forType(returnType);
			}
			proxy = (T) ProxyUtil.newProxy(returnType, new BadUsageThrower(message));
			proxys.put(returnType, proxy);
		}
		return proxy;
	}
}
