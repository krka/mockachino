package se.mockachino.matchers;

import se.mockachino.expectations.DefaultValues;
import se.mockachino.matchers.matcher.*;

public class Matchers {

	public static <T> T match(Matcher<T> matcher) {
		MatcherThreadHandler.pushMatcher(matcher);
		return DefaultValues.forType(matcher.getType());
	}

	public static <T> T m(Matcher<T> matcher) {
		return match(matcher);
	}

	public static <T> Matcher<T> notM(Matcher<T> matcher) {
		return new NotMatcher<T>(matcher);
	}

	public static <T> T not(Matcher<T> matcher) {
		return match(notM(matcher));
	}

	public static <T> AndMatcher<T> andM(Matcher<T>... matchers) {
		return new AndMatcher(matchers);
	}

	public static <T, U extends T> T and(Matcher<U>... matchers) {
		return match(andM(matchers));
	}

	public static <T> OrMatcher<T> orM(Matcher<T>... matchers) {
		return new OrMatcher<T>(matchers);
	}

	public static <T> T or(Matcher<T>... matchers) {
		return match(orM(matchers));
	}


	public static RegexpMatcher regexpM(String s) {
		return new RegexpMatcher(s);
	}

	public static String regexp(String s) {
		return match(regexpM(s));
	}

	public static <T> AnyMatcher<T> anyM(Class<T> clazz) {
		return new AnyMatcher<T>(clazz);
	}

	public static <T> T any(Class<T> clazz) {
		return match(anyM(clazz));
	}

	public static <T> ClassMatcher<T> typeM(Class<T> clazz, Class<?>... classes) {
		return new ClassMatcher<T>(clazz, classes);
	}

	public static <T> T type(Class<T> clazz, Class<?>... classes) {
		return match(typeM(clazz, classes));
	}

	public static <T> Matcher<T> eqM(T value) {
		return new EqualityMatcher<T>(value);
	}

	public static <T> T eq(T value) {
		return match(eqM(value));
	}

	public static <T> T notEq(T value) {
		return not(eqM(value));
	}

	public static <T> IdentityMatcher<T> sameM(T value) {
		return new IdentityMatcher<T>(value);
	}

	public static <T> T same(T value) {
		return match(sameM(value));
	}

	public static <T> T notSame(T value) {
		return not(sameM(value));
	}

	public static int anyInt() {
		return m(anyIntM());
	}

	public static ClassMatcher<Integer> anyIntM() {
		return ClassMatcher.anyInt();
	}

	public static long anyLong() {
		return m(anyLongM());
	}

	public static ClassMatcher<Long> anyLongM() {
		return ClassMatcher.anyLong();
	}

	public static double anyDouble() {
		return m(anyDoubleM());
	}

	public static ClassMatcher<Double> anyDoubleM() {
		return ClassMatcher.anyDouble();
	}

	public static float anyFloat() {
		return m(anyFloatM());
	}

	public static ClassMatcher<Float> anyFloatM() {
		return ClassMatcher.anyFloat();
	}

	public static short anyShort() {
		return m(anyShortM());
	}

	public static ClassMatcher<Short> anyShortM() {
		return ClassMatcher.anyShort();
	}

	public static byte anyByte() {
		return m(anyByteM());
	}

	public static ClassMatcher<Byte> anyByteM() {
		return ClassMatcher.anyByte();
	}

	public static boolean anyBoolean() {
		return m(anyBooleanM());
	}

	public static ClassMatcher<Boolean> anyBooleanM() {
		return ClassMatcher.anyBoolean();
	}

	public static <T> Matcher<T> isNullM() {
		return sameM(null);
	}

	public static <T> T isNull() {
		return m(Matchers.<T>isNullM());
	}

	public static <T> Matcher<T> notNullM() {
		return (Matcher<T>) notM(sameM(null));
	}

	public static <T> T notNull() {
		return m(Matchers.<T>notNullM());
	}

	public static boolean isTrue() {
		return eq(Boolean.TRUE);
	}

	public static boolean isFalse() {
		return eq(Boolean.FALSE);
	}

}

