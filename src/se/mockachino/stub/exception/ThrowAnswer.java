package se.mockachino.stub.exception;

import se.mockachino.CallHandler;
import se.mockachino.MethodCall;
import se.mockachino.VerifyableCallHandler;
import se.mockachino.exceptions.UsageError;
import se.mockachino.util.MockachinoMethod;

import java.lang.reflect.Constructor;

public class ThrowAnswer implements VerifyableCallHandler {

	private final Constructor<Throwable> constructor;
	private final Object[] params;
    private final Class<? extends Throwable> clazz;

    public ThrowAnswer(Throwable e) {
        this.clazz = e.getClass();

		Throwable cause = e.getCause();
		String message = e.getMessage();

		int bestScore = -1;
		Constructor<Throwable> bestConstructor = null;
		Object[] bestParams = null;
		for (Constructor<?> constructor : e.getClass().getConstructors()) {
			Class<?>[] paramTypes = constructor.getParameterTypes();
			int n = paramTypes.length;
			Object[] objects = new Object[n];
			int foundString = 0;
			int foundCause = 0;
			for (int i = 0; i < n; i++) {
				Class<?> paramType = paramTypes[i];
				if (paramType == String.class) {
					objects[i] = message;
					foundString = 1;
				} else if (cause != null && paramType.isAssignableFrom(cause.getClass())) {
					objects[i] = cause;
					foundCause = 1;
				}

			}
			int score = foundString + foundCause;
			if (score > bestScore) {
				constructor.setAccessible(true);
				bestScore = score;
				bestConstructor = (Constructor<Throwable>) constructor;
				bestParams = objects;
			}
		}
		if (bestConstructor == null) {
			throw new UsageError("Could not find a suitable constructor for " + e.getClass());
		}
		constructor = bestConstructor;
		params = bestParams;
	}

	@Override
	public Object invoke(Object obj, MethodCall call) throws Throwable {
		throw constructor.newInstance(params);
	}

    @Override
    public void verify(MockachinoMethod method) {
        if (RuntimeException.class.isAssignableFrom(clazz) ||
                Error.class.isAssignableFrom(clazz)) {
            return;
        }

        Class<?>[] validExceptions = method.getMethod().getExceptionTypes();
        for (Class<?> validException : validExceptions) {
            if (validException.isAssignableFrom(clazz)) {
                return;
            }
        }
        throw new UsageError(method + " may not throw " + clazz);
    }
}
