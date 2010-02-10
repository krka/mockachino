package se.mockachino.matchers;

import se.mockachino.Primitives;
import se.mockachino.matchers.matcher.*;
import se.mockachino.util.Formatting;

public class MatchersBase {

	public static <T> T match(Matcher<T> matcher) {
		MatcherThreadHandler.pushMatcher(matcher);
		return Primitives.forType(matcher.getType());
	}

	// Boolean matchers
	public static <T> Matcher<T> not(Matcher<T> matcher) {
		return new NotMatcher<T>(matcher);
	}

	public static <T> AndMatcher<T> and(Matcher<T>... matchers) {
		return new AndMatcher(matchers);
	}

	public static <T> OrMatcher<T> or(Matcher<T>... matchers) {
		return new OrMatcher<T>(matchers);
	}

	// String matchers
	public static RegexpMatcher regexp(String s) {
		return new RegexpMatcher(s);
	}

	// Any matchers
	public static <T> AnyMatcher<T> any(Class<T> clazz) {
		return new AnyMatcher<T>(clazz);
	}

	public static <T> ClassMatcher<T> type(Class<T> clazz, Class<?>... classes) {
		return new ClassMatcher<T>(clazz, classes);
	}

	public static <T> Matcher<T> eq(T value) {
		return new EqualityMatcher<T>(value);
	}

	public static <T> Matcher<T> eq(T value, Class<T> clazz) {
		return eq(value);
	}

	public static <T> IdentityMatcher<T> same(T value) {
		return new IdentityMatcher<T>(value);
	}

	public static ClassMatcher<Integer> anyInt() {
		return ClassMatcher.anyInt();
	}

	public static ClassMatcher<Long> anyLong() {
		return ClassMatcher.anyLong();
	}

	public static ClassMatcher<Double> anyDouble() {
		return ClassMatcher.anyDouble();
	}

	public static ClassMatcher<Float> anyFloat() {
		return ClassMatcher.anyFloat();
	}

	public static ClassMatcher<Short> anyShort() {
		return ClassMatcher.anyShort();
	}

	public static ClassMatcher<Byte> anyByte() {
		return ClassMatcher.anyByte();
	}

	public static ClassMatcher<Boolean> anyBoolean() {
		return ClassMatcher.anyBoolean();
	}

	public static ClassMatcher<Character> anyChar() {
		return ClassMatcher.anyChar();
	}

	public static <T> Matcher<T> isNull() {
		return same(null);
	}

	public static <T> Matcher<T> notNull() {
		return (Matcher<T>) not(same(null));
	}

	public static Matcher<String> contains(String s) {
		return new RegexpMatcher(".*" + Formatting.quote(s) + ".*");
	}

	public static Matcher<String> startsWith(String s) {
		return new RegexpMatcher(Formatting.quote(s) + ".*");
	}

	public static Matcher<String> endsWith(String s) {
		return new RegexpMatcher(".*" + Formatting.quote(s));
	}
}

