package se.mockachino.verifier;

import se.mockachino.Invocation;
import se.mockachino.MethodCall;

public class InvocationCount<T> extends Invocation<T> {
	private int count = 1;

	public InvocationCount(Invocation<T> call) {
		this(call.getObject(), call.getMethodCall(), call.getCallNumber(), call.getStacktrace());
	}

	public InvocationCount(Object obj, MethodCall<T> methodCall, int callNumber, StackTraceElement[] stacktrace) {
		super(obj, methodCall, callNumber, stacktrace);
	}

	public void increment(int amount) {
		count += amount;
	}

	public int getCount() {
		return count;
	}
}
