package se.mockachino.stub.exception;

import se.mockachino.MethodCall;
import se.mockachino.VerifyableCallHandler;
import se.mockachino.exceptions.UsageError;
import se.mockachino.util.MockachinoMethod;

import java.lang.reflect.Constructor;

public class ThrowAnswer implements VerifyableCallHandler {

  private final Constructor<Throwable> constructor;
  private final Class<? extends Throwable> clazz;
  private final Throwable throwable;
  private final Object[] initArgs;

  public ThrowAnswer(Throwable e) {
    this.clazz = e.getClass();

    Constructor<Throwable> bestConstructor = findBestConstructor(e.getClass());
    if (bestConstructor != null) {
      throwable = null;
      constructor = bestConstructor;
      Class<?>[] parameterTypes = constructor.getParameterTypes();
      initArgs = new Object[parameterTypes.length];
      for (int i = 0; i < initArgs.length; i++) {
        Class<?> paramClass = parameterTypes[i];
        if (paramClass == String.class) {
          initArgs[i] = e.getMessage();
        } else if (paramClass == Throwable.class) {
          initArgs[i] = e.getCause();
        } else {
          throw new RuntimeException();
        }
      }
    } else {
      throwable = e;
      constructor = null;
      initArgs = null;
    }
  }

  private Constructor<Throwable> findBestConstructor(Class<?> clazz) {
    Constructor<Throwable> bestConstructor = null;
    int bestScore = 0;

    for (Constructor<?> constructor : clazz.getConstructors()) {
      Class<?>[] paramTypes = constructor.getParameterTypes();
      int score = getScore(paramTypes);
      if (score > bestScore) {
        bestScore = score;
        bestConstructor = (Constructor<Throwable>) constructor;
      }
    }
    return bestConstructor;
  }

  private int getScore(Class<?>[] paramTypes) {
    if (equals(paramTypes, String.class, Throwable.class)) {
      return 4;
    }
    if (equals(paramTypes, Throwable.class, String.class)) {
      return 3;
    }
    if (equals(paramTypes, Throwable.class)) {
      return 2;
    }
    if (equals(paramTypes, String.class)) {
      return 1;
    }
    return 0;
  }

  private boolean equals(Class<?>[] paramTypes, Class<?>... b) {
    if (paramTypes.length != b.length) {
      return false;
    }
    for (int i = 0; i < paramTypes.length; i++) {
      if (!paramTypes[i].equals(b[i])) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Object invoke(Object obj, MethodCall call) throws Throwable {
    if (throwable != null) {
      throw throwable;
    }
    throw constructor.newInstance(initArgs);
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
