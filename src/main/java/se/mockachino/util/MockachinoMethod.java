package se.mockachino.util;

import com.googlecode.gentyref.GenericTypeReflector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;

public class MockachinoMethod {
	public static final MockachinoMethod EQUALS = find(Object.class, "equals");
	public static final MockachinoMethod HASHCODE = find(Object.class, "hashCode");
	public static final MockachinoMethod NULL = new MockachinoMethod("<null>", new Class[0], void.class);

	private final Method method;
	private final String name;
	private final Class[] parameters;
	private final Class returnType;
    private final Type type;

    public MockachinoMethod(Type type, Method method) {
        this.type = type;
        method.setAccessible(true);
		this.method = method;
		name = method.getName();
		parameters = method.getParameterTypes();
		returnType = method.getReturnType();
	}

	public MockachinoMethod(String name, Class<Object>[] parameterTypes, Class<?> returnType) {
		this.method = null;
		this.name = name;
		this.parameters = parameterTypes;
		this.returnType = returnType;
        this.type = null;
	}

    public String getName() {
		return name;
	}

	public Class[] getParameters() {
		return parameters;
	}

	public Class<?> getReturnType() {
        return getReturnClass(type, method);
	}

	public Method getMethod() {
		return method;
	}

	@Override
	public String toString() {
		StringBuilder params = new StringBuilder();
		boolean first = true;
		for (Class parameter : parameters) {
			if (first) {
				first = false;
			} else {
				params.append(", ");
			}
			params.append(parameter.getSimpleName());
		}
		return String.format("%s %s(%s)",
				returnType.getSimpleName(),
				name,
				params.toString());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		MockachinoMethod that = (MockachinoMethod) o;

		if (!name.equals(that.name)) return false;
		if (!Arrays.equals(parameters, that.parameters)) return false;
		if (!returnType.equals(that.returnType)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + Arrays.hashCode(parameters);
		result = 31 * result + returnType.hashCode();
		return result;
	}

	public static MockachinoMethod find(Class clazz, String name) {
		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(name)) {
				return new MockachinoMethod(clazz, method);
			}
		}
		return null;
	}


	public Object invoke(Object impl, Object[] objects) throws Throwable {
        try {
            return method.invoke(impl, objects);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

	public boolean isToStringCall() {
		return name.equals("toString") && parameters.length == 0;
	}

    public static Type getReturnType(Type type, Method method) {
        try {
            return GenericTypeReflector.getExactReturnType(method, type);
		  } catch (Throwable e) {
			  return method.getReturnType();
        }
    }

    public static Class<?> getReturnClass(Type type, Method method) {
        return GenericTypeReflector.erase(getReturnType(type, method));
    }
}
