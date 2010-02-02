package se.mockachino.listener;

import se.mockachino.MethodCall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MethodListeners {
	private final List<MethodListener> listeners;
	public MethodListeners(Class<?> returnType) {
		listeners = Collections.synchronizedList(new ArrayList<MethodListener>());
	}

	public void add(MethodListener methodListener) {
		listeners.add(methodListener);
	}

	public void notifyListeners(MethodCall methodCall) {
		for (MethodListener listener : listeners) {
			listener.invoke(methodCall);
		}
	}
}
