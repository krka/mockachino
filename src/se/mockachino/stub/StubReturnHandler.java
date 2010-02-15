package se.mockachino.stub;

import se.mockachino.MockContext;
import se.mockachino.MockData;
import se.mockachino.Primitives;
import se.mockachino.exceptions.UsageError;
import se.mockachino.expectations.MethodExpectations;
import se.mockachino.matchers.MethodMatcher;
import se.mockachino.util.MockachinoMethod;
import se.mockachino.verifier.MatchingHandler;

public class StubReturnHandler<T> extends MatchingHandler {
	private final Object returnValue;
	private final MockData<T> data;

	public StubReturnHandler(Object returnValue, T mock, MockData<T> data) {
		super("StubReturnHandler", mock.toString());
		this.returnValue = returnValue;
		this.data = data;
	}

	public void match(Object o, MethodMatcher matcher) {
		MockachinoMethod method = matcher.getMethod();
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

		MethodExpectations methodExpectations = data.getExpectations(method);
		methodExpectations.add(new MethodStub(returnValue, matcher));
	}

	private void error(Class<?> returnType) {
		throw new UsageError(("Expected a return value of type " + returnType.getSimpleName() + " but was " + returnValue.getClass().getSimpleName()));
	}
}
