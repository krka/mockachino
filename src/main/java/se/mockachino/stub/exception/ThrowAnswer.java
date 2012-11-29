package se.mockachino.stub.exception;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;
import se.mockachino.MethodCall;
import se.mockachino.VerifyableCallHandler;
import se.mockachino.exceptions.UsageError;
import se.mockachino.proxy.ObjenesisUtil;
import se.mockachino.util.MockachinoMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ThrowAnswer implements VerifyableCallHandler {

  private final Constructor<Throwable> constructor;
  private final Class<? extends Throwable> clazz;
  private final Throwable throwable;
  private final Object[] initArgs;
  private final Set<Field> fields;
  private Field cause;

  public ThrowAnswer(Throwable e) {
    this.clazz = e.getClass();

    Constructor<Throwable> bestConstructor = findBestConstructor(e.getClass());
    if (bestConstructor != null) {
      throwable = null;
      fields = null;
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
      fields = allFields(clazz);
      for (Field field : fields) {
        if (field.getName().equals("cause")) {
          cause = field;
        }
        field.setAccessible(true);
      }
      constructor = null;
      initArgs = null;
    }
  }

  private Set<Field> allFields(Class<?> clazz) {
    HashSet<Field> set = new HashSet<Field>();
    allFields(clazz, set);
    return set;
  }

  private void allFields(Class<?> clazz, HashSet<Field> set) {
    for (Field field : clazz.getDeclaredFields()) {
      if (!Modifier.isStatic(field.getModifiers())) {
        set.add(field);
      }
    }
    for (Class<?> iface : clazz.getInterfaces()) {
      allFields(iface, set);
    }
    Class<?> superclass = clazz.getSuperclass();
    if (superclass != null) {
      allFields(superclass, set);
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
      Throwable newThrowable = (Throwable) ObjenesisUtil.newInstance(clazz);
      for (Field field : fields) {
        Object value = field.get(throwable);
        if (field == cause) {
          value = newThrowable;
        }
        field.set(newThrowable, value);
      }
      newThrowable.setStackTrace(new RuntimeException().getStackTrace());
      throw newThrowable;
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
