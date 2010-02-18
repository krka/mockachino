package se.mockachino;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Primitives {
	private static final Map<Class<?>, Object> defaultValues = new HashMap<Class<?>, Object>();
	private static final Map<Class<?>, Class<?>> realClasses = new HashMap<Class<?>, Class<?>>();
	static {
		bindClasses(Boolean.class, boolean.class, Boolean.FALSE);
		bindClasses(Integer.class, int.class, Integer.valueOf(0));
		bindClasses(Long.class, long.class, Long.valueOf(0));
		bindClasses(Short.class, short.class, Short.valueOf((short) 0));
		bindClasses(Double.class, double.class, Double.valueOf(0));
		bindClasses(Float.class, float.class, Float.valueOf(0));
		bindClasses(Character.class, char.class, Character.valueOf('\0'));
		bindClasses(Byte.class, byte.class, Byte.valueOf((byte) 0));
	}

	private static void bindClasses(Class<?> first, Class<?> second, Object value) {
		defaultValues.put(first, value);
		defaultValues.put(second, value);
		realClasses.put(first, value.getClass());
		realClasses.put(second, value.getClass());
	}

	public static <T> T forType(Class<T> returnType) {
		return (T) defaultValues.get(returnType);
	}

	public static Class<?> getRealClass(Class<?> clazz) {
		Class<?> real = realClasses.get(clazz);
		if (real == null) {
			return clazz;
		}
		return real;
	}

	public static Object toList(Object arg) {
		if (arg == null) {
			return null;
		}
		if (arg.getClass().isArray()) {
			List ret = new ArrayList();
			int n = Array.getLength(arg);
			for (int i = 0; i < n; i++) {
				ret.add(toList(Array.get(arg, i)));
			}
			return ret;
		}
		return arg;
	}
}
