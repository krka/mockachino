package se.mockachino.stub.returnvalue;

import se.mockachino.MethodCall;
import se.mockachino.Primitives;
import se.mockachino.VerifyableCallHandler;
import se.mockachino.exceptions.UsageError;
import se.mockachino.util.MockachinoMethod;

public class ReturnAnswer<T> implements VerifyableCallHandler<T> {
	private final T returnValue;

	public ReturnAnswer(T returnValue) {
		this.returnValue = returnValue;
	}

	@Override
	public T invoke(Object obj, MethodCall call) throws Throwable {
		return returnValue;
	}

    @Override
    public void verify(MockachinoMethod<T> method) {
        verifyReturnType(method.getReturnType(), returnValue);
    }

    protected static <T> void verifyReturnType(Class<T> returnType, T value) {
        if (returnType == void.class) {
            if (value != null) {
                throw new UsageError(("Void methods must return null"));
            }
        } else {
            if (value == null) {
                if (returnType.isPrimitive()) {
                    throw new UsageError(("Expected a return value of type " + returnType.getSimpleName() + " but was null"));
                }
            } else {
                if (returnType.isPrimitive()) {
                    if (value.getClass() != Primitives.getRealClass(returnType)) {
                        error(returnType, value);
                    }
                } else {
                    if (!returnType.isAssignableFrom(value.getClass())) {
                        error(returnType, value);
                    }
                }
            }
        }
    }

    protected static void error(Class<?> returnType, Object value) {
        throw new UsageError(("Expected a return value of type " + returnType.getSimpleName() + " but was " + value.getClass().getSimpleName()));
    }
}
