package se.mockachino.expectations;

import se.mockachino.MethodCall;

import java.util.HashMap;
import java.util.Map;

public class DefaultMethodExpectations implements MethodExpectation {
	private static final Map<Class<?>, MethodExpectation> defaultReturnTypes = new HashMap<Class<?>, MethodExpectation>(){
		{
			bindClasses(Boolean.class, boolean.class, Boolean.FALSE);
			bindClasses(Integer.class, int.class, Integer.valueOf(0));
			bindClasses(Long.class, long.class, Long.valueOf(0));
			bindClasses(Short.class, short.class, Short.valueOf((short) 0));
			bindClasses(Double.class, double.class, Double.valueOf(0));
			bindClasses(Float.class, float.class, Float.valueOf(0));
			bindClasses(Character.class, char.class, Character.valueOf('\0'));
			bindClasses(Byte.class, byte.class, Byte.valueOf((byte) 0));
		}
		private void bindClasses(Class<?> first, Class<?> second, Object value) {
			final DefaultMethodExpectations expectations = new DefaultMethodExpectations(value);
			put(first, expectations);
			put(second,expectations);
		}
	};
	private static final MethodExpectation NULL_EXPECTATION = new DefaultMethodExpectations(null);

	private final Object value;

	private DefaultMethodExpectations(Object value) {
		this.value = value;
	}

	public static <T> MethodExpectation<T> forType(Class<T> returnType) {
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
