package se.mockachino.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MockachinoMethod {
	public static final MockachinoMethod EQUALS = MockachinoMethod.find(Object.class, "equals");
	public static final MockachinoMethod HASHCODE = MockachinoMethod.find(Object.class, "hashCode");
	public static final MockachinoMethod TOSTRING = MockachinoMethod.find(Object.class, "toString");


	private final Method method;
	private final String name;
	private final Class[] parameters;
	private final Class returnType;

	public MockachinoMethod(Method method) {
		method.setAccessible(true);
		this.method = method;
		name = method.getName();
		parameters = method.getParameterTypes();
		returnType = method.getReturnType();
	}

	public String getName() {
		return name;
	}

	public Class[] getParameters() {
		return parameters;
	}

	public Class getReturnType() {
		return returnType;
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
		for (Method method : Object.class.getMethods()) {
			if (method.getName().equals(name)) {
				return new MockachinoMethod(method);
			}
		}
		return null;
	}


	public Object invoke(Object impl, Object[] objects) throws InvocationTargetException, IllegalAccessException {
		return method.invoke(impl, objects);
	}
}
