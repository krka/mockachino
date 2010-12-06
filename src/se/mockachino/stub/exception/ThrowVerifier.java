package se.mockachino.stub.exception;

import se.mockachino.exceptions.UsageError;
import se.mockachino.stub.StubVerifier;
import se.mockachino.util.MockachinoMethod;

public class ThrowVerifier implements StubVerifier {
    private final Class<? extends Throwable> clazz;

    public ThrowVerifier(Throwable e) {
        clazz = e.getClass();
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
