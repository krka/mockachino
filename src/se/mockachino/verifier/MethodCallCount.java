package se.mockachino.verifier;

import se.mockachino.MethodCall;
import se.mockachino.util.MockachinoMethod;

public class MethodCallCount extends MethodCall {
	private int count = 1;

	public MethodCallCount(MethodCall call) {
		this(call.getMethod(), call.getArguments(), call.getCallNumber(), call.getStackTrace());
	}

	public MethodCallCount(MockachinoMethod method, Object[] args, int callNumber, StackTraceElement[] stacktrace) {
		super(method, args, callNumber, stacktrace);
	}
	public void increment(int amount) {
		count += amount;
	}

	public int getCount() {
		return count;
	}
}
