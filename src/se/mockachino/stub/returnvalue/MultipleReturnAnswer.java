package se.mockachino.stub.returnvalue;

import se.mockachino.CallHandler;
import se.mockachino.MethodCall;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MultipleReturnAnswer implements CallHandler {
	private final List<Object> returnValues = new ArrayList<Object>();
	private final AtomicInteger position = new AtomicInteger();

	public MultipleReturnAnswer(Object returnValue, Object... returnValues) {
		this.returnValues.add(returnValue);
		if (returnValues != null) {
			for (Object value: returnValues) {
				this.returnValues.add(value);
			}
		}
	}

	@Override
	public Object invoke(Object obj, MethodCall call) throws Throwable {
		int pos = Math.min(position.getAndIncrement(), returnValues.size() - 1);
		return returnValues.get(pos);
	}
}
