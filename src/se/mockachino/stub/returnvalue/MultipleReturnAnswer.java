package se.mockachino.stub.returnvalue;

import se.mockachino.CallHandler;
import se.mockachino.MethodCall;

import java.util.concurrent.atomic.AtomicInteger;

public class MultipleReturnAnswer implements CallHandler {
	private final Object[] returnValues;
	private final AtomicInteger position = new AtomicInteger();

	public MultipleReturnAnswer(Object... returnValues) {
		this.returnValues = returnValues;
	}

	@Override
	public Object invoke(Object obj, MethodCall call) throws Throwable {
		int pos = Math.min(position.getAndIncrement(), returnValues.length - 1);
		return returnValues[pos];
	}
}