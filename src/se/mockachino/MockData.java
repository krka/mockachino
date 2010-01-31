package se.mockachino;

import se.mockachino.expectations.MethodExpectations;
import se.mockachino.proxy.ProxyUtil;
import se.mockachino.invocationhandler.Verifier;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.reflect.Method;

public class MockData<T> {
	private final Class<T> iface;
	private final List<MethodCall> calls;
	private final Map<Method, MethodExpectations> expectations;

	public MockData(Class<T> iface) {
		this.iface = iface;
		calls = Collections.synchronizedList(new ArrayList<MethodCall>());
		expectations = new ConcurrentHashMap<Method,MethodExpectations>();
		for (Method method : iface.getMethods()) {
			expectations.put(method, new MethodExpectations(method.getReturnType()));
		}
	}

	public List<MethodCall> getCalls() {
		return calls;
	}

	public MethodExpectations getExpectations(Method method) {
		return expectations.get(method);
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
