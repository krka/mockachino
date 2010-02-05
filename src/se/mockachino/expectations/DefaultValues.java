package se.mockachino.expectations;

import se.mockachino.MethodCall;

import java.util.HashMap;
import java.util.Map;

public class DefaultValues {
	private static final Map<Class<?>, Object> defaultValues = new HashMap<Class<?>, Object>(){
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
			put(first, value);
			put(second, value);
		}
	};

	public static <T> T forType(Class<T> returnType) {
		return (T) defaultValues.get(returnType);
	}
}
