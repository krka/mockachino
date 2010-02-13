package se.mockachino.matchers;

import se.mockachino.Primitives;
import se.mockachino.matchers.matcher.AndMatcher;
import se.mockachino.matchers.matcher.AnyMatcher;
import se.mockachino.matchers.matcher.ClassMatcher;
import se.mockachino.matchers.matcher.EqualityMatcher;
import se.mockachino.matchers.matcher.IdentityMatcher;
import se.mockachino.matchers.matcher.Matcher;
import se.mockachino.matchers.matcher.NotMatcher;
import se.mockachino.matchers.matcher.OrMatcher;
import se.mockachino.matchers.matcher.RegexpMatcher;
import se.mockachino.util.Formatting;

public class MatchersBase {

	public static <T> T match(Matcher<T> matcher) {
		MatcherThreadHandler.pushMatcher(matcher);
		return Primitives.forType(matcher.getType());
	}

	// Boolean matchers
	public static <T> Matcher<T> mNot(Matcher<T> matcher) {
		return new NotMatcher<T>(matcher);
	}

	public static <T> AndMatcher<T> mAnd(Matcher<T>... matchers) {
		return new AndMatcher(matchers);
	}

	public static <T> OrMatcher<T> mOr(Matcher<T>... matchers) {
		return new OrMatcher<T>(matchers);
	}

	// String matchers
	public static RegexpMatcher mRegexp(String s) {
		return new RegexpMatcher(s);
	}

	// Any matchers
	public static <T> AnyMatcher<T> mAny(Class<T> clazz) {
		return new AnyMatcher<T>(clazz);
	}

	public static <T> ClassMatcher<T> mType(Class<T> clazz, Class<?>... classes) {
		return new ClassMatcher<T>(clazz, classes);
	}

	public static <T> Matcher<T> mEq(T value) {
		return new EqualityMatcher<T>(value);
	}

	public static <T> Matcher<T> matchEq(T value, Class<T> clazz) {
		return mEq(value);
	}

	public static <T> IdentityMatcher<T> mSame(T value) {
		return new IdentityMatcher<T>(value);
	}

	public static ClassMatcher<Integer> mAnyInt() {
		return ClassMatcher.anyInt();
	}

	public static ClassMatcher<Long> mAnyLong() {
		return ClassMatcher.anyLong();
	}

	public static ClassMatcher<Double> mAnyDouble() {
		return ClassMatcher.anyDouble();
	}

	public static ClassMatcher<Float> mAnyFloat() {
		return ClassMatcher.anyFloat();
	}

	public static ClassMatcher<Short> mAnyShort() {
		return ClassMatcher.anyShort();
	}

	public static ClassMatcher<Byte> mAnyByte() {
		return ClassMatcher.anyByte();
	}

	public static ClassMatcher<Boolean> mAnyBoolean() {
		return ClassMatcher.anyBoolean();
	}

	public static ClassMatcher<Character> mAnyChar() {
		return ClassMatcher.anyChar();
	}

	public static Matcher<String> mContains(String s) {
		return new RegexpMatcher(".*" + Formatting.quote(s) + ".*");
	}

	public static Matcher<String> mStartsWith(String s) {
		return new RegexpMatcher(Formatting.quote(s) + ".*");
	}

	public static Matcher<String> mEndsWith(String s) {
		return new RegexpMatcher(".*" + Formatting.quote(s));
	}
}

