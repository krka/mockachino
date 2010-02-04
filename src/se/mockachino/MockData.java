package se.mockachino;

import se.mockachino.expectations.MethodExpectations;
import se.mockachino.listener.MethodListeners;
import se.mockachino.util.SafeIteratorList;

import java.util.*;
import java.lang.reflect.Method;

public class MockData<T> {
	private final Class<T> iface;
	private final List<MethodCall> calls;
	private final List<MethodCall> readOnlyCalls;
	private final Map<Method, MethodListeners> listeners;
	private final Map<Method, MethodExpectations> expectations;

	public MockData(Class<T> iface) {
		this.iface = iface;
		calls = new SafeIteratorList<MethodCall>(new ArrayList<MethodCall>());
		readOnlyCalls = Collections.unmodifiableList(calls);
		expectations = new HashMap<Method,MethodExpectations>();
		listeners = new HashMap<Method,MethodListeners>();
		for (Method method : iface.getMethods()) {
			expectations.put(method, new MethodExpectations());
			listeners.put(method, new MethodListeners());
		}
	}

	public List<MethodCall> getCalls() {
		return readOnlyCalls;
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

	public synchronized MethodCall addCall(MockContext mockContext, Method method, Object[] objects, StackTraceElement[] stackTrace) {
		int callNumber = mockContext.incrementSequence();
		MethodCall call = new MethodCall(method, objects, callNumber, stackTrace);
		calls.add(call);
		return call;
	}

	public synchronized void reset() {
		calls.clear();
	}

}
