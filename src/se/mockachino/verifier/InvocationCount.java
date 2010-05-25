package se.mockachino.verifier;

import se.mockachino.Invocation;
import se.mockachino.MethodCall;

public class InvocationCount extends Invocation {
	private int count = 1;

	public InvocationCount(Invocation call) {
		this(call.getObject(), call.getMethodCall(), call.getCallNumber(), call.getStacktrace());
	}

	public InvocationCount(Object obj, MethodCall methodCall, int callNumber, StackTraceElement[] stacktrace) {
        super(obj, methodCall, callNumber, stacktrace);
	}

    public void increment(int amount) {
		count += amount;
	}

	public int getCount() {
		return count;
	}
}
