package se.mockachino.stub.returnvalue;

import se.mockachino.stub.StubVerifier;
import se.mockachino.util.MockachinoMethod;

public class MultipleReturnVerifier implements StubVerifier {
	private final Object[] returnValues;

	public MultipleReturnVerifier(Object... returnValues) {
		this.returnValues = returnValues;
	}

	public void verify(MockachinoMethod method) {
		for (Object returnValue : returnValues) {
			ReturnVerifier.verifyReturnType((Class<?>) method.getReturnType(), returnValue);
		}
	}
}