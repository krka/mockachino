package se.mockachino.assertion;

import se.mockachino.Mockachino;
import se.mockachino.Primitives;
import se.mockachino.cleaner.StacktraceCleaner;
import se.mockachino.proxy.ProxyUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AssertThrows extends Mockachino {
    private final Class<? extends Throwable> clazz;

    public AssertThrows(Class<? extends Throwable> clazz) {
        this.clazz = clazz;
    }

    public <T> T on(final T obj) {
        return (T) ProxyUtil.newProxy(obj.getClass(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                try {
                    method.invoke(obj, args);
                } catch (InvocationTargetException e) {
                    Throwable te = e.getTargetException();
                    if (clazz.isInstance(te)) {
                        return Primitives.forType(method.getReturnType());
                    }
                    throw StacktraceCleaner.cleanError(new AssertionError("Expected exception of type " + clazz.getName() + " but got an exception of type " + te.getClass().getName() + ": " + te.getMessage()));
                }
                throw StacktraceCleaner.cleanError(new AssertionError("Expected exception of type " + clazz.getName() + " but got no exception"));
            }
        });
    }
}
