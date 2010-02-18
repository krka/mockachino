package se.mockachino.stub.returnvalue;

import se.mockachino.CallHandler;
import se.mockachino.MethodCall;

public class ReturnAnswer implements CallHandler {
	private final Object returnValue;

	public ReturnAnswer(Object returnValue) {
		this.returnValue = returnValue;
	}

	@Override
	public Object invoke(Object obj, MethodCall call) throws Throwable {
		return returnValue;
	}
}
