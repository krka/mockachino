package se.mockachino.stub.exception;

import se.mockachino.CallHandler;
import se.mockachino.MethodCall;

public class ThrowAnswer implements CallHandler {
	private final Throwable e;

	public ThrowAnswer(Throwable e) {
		this.e = e;
	}

	@Override
	public Object invoke(Object obj, MethodCall call) throws Throwable {
		RuntimeException actualStacktrace = new RuntimeException();
		e.setStackTrace(actualStacktrace.getStackTrace());
		throw e;
	}
}
