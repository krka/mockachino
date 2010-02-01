package se.mockachino.matchers;

import se.mockachino.expectations.DefaultMethodExpectations;

public class Matchers {

	public static <T> T matcher(Matcher<T> matcher) {
		MatcherThreadHandler.pushMatcher(matcher);
		try {
			return DefaultMethodExpectations.forType(matcher.getType()).getValue();
		} catch (Throwable throwable) {
			return null;
		}
	}

	public static String regexp(String s) {
		return matcher(new RegexpMatcher(s));
	}

	public static <T> T any(Class<T> clazz) {
		return matcher(new AnyMatcher<T>(clazz));
	}

	public static <T> T type(Class<T> clazz, Class<?>... classes) {
		return matcher(new ClassMatcher<T>(clazz, classes));
	}

	public static <T> T eq(T value) {
		return matcher(new EqualityMatcher<T>(value));
	}

	public static <T> T notEq(T value) {
		return matcher(new InequalityMatcher<T>(value));
	}

	public static <T> T same(T value) {
		return matcher(new IdentityMatcher<T>(value, true));
	}

	public static <T> T notSame(T value) {
		return matcher(new IdentityMatcher<T>(value, false));
	}

	public static int anyInt() {
		return type(Integer.class);
	}

	public static long anyLong() {
		return type(Long.class, Integer.class);
	}

	public static double anyDouble() {
		return type(Double.class);
	}

	public static float anyFloat() {
		return type(Float.class, Double.class);
	}

	public static short anyShort() {
		return type(Short.class, Integer.class);
	}

	public static byte anyByte() {
		return type(Byte.class, Integer.class);
	}

	public static boolean anyBoolean() {
		return type(Boolean.class);
	}

	public static <T> T isNull() {
		return same((T) null);
	}

	public static <T> T notNull() {
		return notSame((T) null);
	}
}
