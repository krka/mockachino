package se.mockachino.stub.returnvalue;

import se.mockachino.Primitives;
import se.mockachino.exceptions.UsageError;
import se.mockachino.stub.StubVerifier;
import se.mockachino.util.MockachinoMethod;

public class ReturnVerifier implements StubVerifier {
	private final Object returnValue;

	public ReturnVerifier(Object returnValue) {
		this.returnValue = returnValue;
	}

	public void verify(MockachinoMethod method) {
		Class<?> returnType = method.getReturnType();
		if (returnType == void.class) {
			if (returnValue != null) {
				throw new UsageError(("Void methods must return null"));
			}
		} else {
			if (returnValue == null) {
				if (returnType.isPrimitive()) {
					throw new UsageError(("Expected a return value of type " + returnType.getSimpleName() + " but was null"));
				}
			} else {
				if (returnType.isPrimitive()) {
					if (returnValue.getClass() != Primitives.getRealClass(returnType)) {
						error(returnType);
					}
				} else {
					if (!returnType.isAssignableFrom(returnValue.getClass())) {
						error(returnType);
					}
				}
			}
		}
	}

	private void error(Class<?> returnType) {
		throw new UsageError(("Expected a return value of type " + returnType.getSimpleName() + " but was " + returnValue.getClass().getSimpleName()));
	}
}
