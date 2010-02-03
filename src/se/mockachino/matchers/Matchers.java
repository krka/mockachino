package se.mockachino.matchers;

import se.mockachino.expectations.DefaultMethodExpectations;
import se.mockachino.matchers.matcher.*;

public class Matchers {

	public static <T> T matcher(Matcher<T> matcher) {
		MatcherThreadHandler.pushMatcher(matcher);
		try {
			return DefaultMethodExpectations.forType(matcher.getType()).getValue(null);
		} catch (Throwable throwable) {
			return null;
		}
	}

	public static <T> T m(Matcher<T> matcher) {
		return matcher(matcher);
	}

	public static <T> T not(Matcher<T> matcher) {
		return matcher(new NotMatcher<T>(matcher));
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
		return m(ClassMatcher.anyInt());
	}

	public static long anyLong() {
		return m(ClassMatcher.anyLong());
	}

	public static double anyDouble() {
		return m(ClassMatcher.anyDouble());
	}

	public static float anyFloat() {
		return m(ClassMatcher.anyFloat());
	}

	public static short anyShort() {
		return m(ClassMatcher.anyShort());
	}

	public static byte anyByte() {
		return m(ClassMatcher.anyByte());
	}

	public static boolean anyBoolean() {
		return m(ClassMatcher.anyBoolean());
	}

	public static <T> T isNull() {
		return same((T) null);
	}

	public static <T> T notNull() {
		return notSame((T) null);
	}

	public static <T> T and(Matcher<T>... matchers) {
		return m(new AndMatcher<T>(matchers));
	}

	public static <T> T or(Matcher<T>... matchers) {
		return m(new OrMatcher<T>(matchers));
	}

}

