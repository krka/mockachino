package se.mockachino.listener;

import se.mockachino.MethodCall;

public interface MethodCallListener {
	void listen(Object obj, MethodCall call);
}
