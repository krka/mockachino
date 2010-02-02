package se.mockachino;

import se.mockachino.expectations.MethodExpectations;
import se.mockachino.listener.MethodListeners;
import se.mockachino.proxy.ProxyUtil;
import se.mockachino.verifier.Verifier;

import java.util.*;
import java.lang.reflect.Method;

public class MockData<T> {
	private final Class<T> iface;
	private final List<MethodCall> calls;
	private final Map<Method, MethodListeners> listeners;
	private final Map<Method, MethodExpectations> expectations;

	public MockData(Class<T> iface) {
		this.iface = iface;
		calls = Collections.synchronizedList(new ArrayList<MethodCall>());
		expectations = new HashMap<Method,MethodExpectations>();
		listeners = new HashMap<Method,MethodListeners>();
		for (Method method : iface.getMethods()) {
			expectations.put(method, new MethodExpectations(method.getReturnType()));
			listeners.put(method, new MethodListeners(method.getReturnType()));
		}
	}

	public List<MethodCall> getCalls() {
		return calls;
	}

	public MethodExpectations getExpectations(Method method) {
		return expectations.get(method);
	}

	public MethodListeners getListeners(Method method) {
		return listeners.get(method);
	}

	public Class<T> getInterface() {
		return iface;
	}

	public T getVerifier(int min, int max) {
		return ProxyUtil.newProxy(iface, new Verifier(this, min, max));
	}

	public T getVerifier() {
		return ProxyUtil.newProxy(iface, new Verifier(this, 1, Integer.MAX_VALUE));
	}
}
