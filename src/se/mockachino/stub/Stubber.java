package se.mockachino.stub;

import se.mockachino.MockData;
import se.mockachino.exceptions.UsageError;
import se.mockachino.expectations.DefaultValues;
import se.mockachino.stub.MethodStub;
import se.mockachino.verifier.VerificationHandler;
import se.mockachino.matchers.MethodMatcher;

public class Stubber<T> extends VerificationHandler {
	private final Object returnValue;
	private final MockData<T> data;

	public Stubber(Object returnValue, T mock, MockData<T> data) {
		super("Stubber", mock.toString());
		this.returnValue = returnValue;
		this.data = data;
	}

	public void verify(Object o, MethodMatcher matcher) {
		Class<?> returnType = matcher.getMethod().getReturnType();
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
					if (returnValue.getClass() != DefaultValues.getRealClass(returnType)) {
						error(returnType);
					}
				} else {
					if (!returnType.isAssignableFrom(returnValue.getClass())) {
						error(returnType);
					}
				}
			}
		}

		data.getExpectations(matcher.getMethod()).add(new MethodStub(returnValue, matcher));
	}

	private void error(Class<?> returnType) {
		throw new UsageError(("Expected a return value of type " + returnType.getSimpleName() + " but was " + returnValue.getClass().getSimpleName()));
	}
}
