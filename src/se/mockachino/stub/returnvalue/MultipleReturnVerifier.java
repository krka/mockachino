package se.mockachino.stub.returnvalue;

import java.util.*;
import se.mockachino.stub.StubVerifier;
import se.mockachino.util.MockachinoMethod;

public class MultipleReturnVerifier implements StubVerifier {
	private final List<Object> returnValues = new ArrayList<Object>();

	public MultipleReturnVerifier(Object returnValue, Object... returnValues) {
		this.returnValues.add(returnValue);
		if (returnValues != null) {
			for (Object value: returnValues) {
				this.returnValues.add(value);
			}
		}
	}

	public void verify(MockachinoMethod method) {
		for (Object returnValue : returnValues) {
			ReturnVerifier.verifyReturnType((Class<?>) method.getReturnType(), returnValue);
		}
	}
}
