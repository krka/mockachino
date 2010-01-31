package se.mockachino.expectations;

import se.mockachino.MethodCall;

import java.util.HashMap;
import java.util.Map;

public class DefaultMethodExpectations implements MethodExpectation {
	private static final Map<Class<?>, MethodExpectation> defaultReturnTypes = new HashMap<Class<?>, MethodExpectation>(){{
		put(Boolean.class, new DefaultMethodExpectations(false));
		put(boolean.class, new DefaultMethodExpectations(false));
		put(Integer.class, new DefaultMethodExpectations(0));
		put(int.class, new DefaultMethodExpectations(0));
	}};
	private static final MethodExpectation NULL_EXPECTATION = new DefaultMethodExpectations(null);

	private final Object value;

	private DefaultMethodExpectations(Object value) {
		this.value = value;
	}

	public static MethodExpectation forType(Class<?> returnType) {
		MethodExpectation methodExpectation = defaultReturnTypes.get(returnType);
		if (methodExpectation != null) {
			return methodExpectation;
		}
		return NULL_EXPECTATION;

	}

	@Override
	public boolean matches(MethodCall call) {
		return true;
	}

	@Override
	public Object getValue() {
		return value;
	}
}
