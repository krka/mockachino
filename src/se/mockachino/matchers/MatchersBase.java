package se.mockachino.matchers;

import se.mockachino.matchers.matcher.*;
import se.mockachino.util.Formatting;

/**
 * This class contain a lot of built in matchers.
 *
 */
public class MatchersBase {

	/**
	 * Return a new matcher that's the negation of the specified matcher.
	 *
	 * @param matcher
	 * @return a new matcher
	 */
	public static <T> Matcher<T> mNot(Matcher<T> matcher) {
		return new NotMatcher<T>(matcher);
	}

	/**
	 * Return a new matcher that matches if all the given matchers match
	 *
	 * @param matchers
	 * @return a new matcher
	 */
	public static <T> Matcher<T> mAnd(Matcher<T>... matchers) {
		return new AndMatcher(matchers);
	}

	/**
	 * Return a new matcher that matches if any of the given matchers match
	 *
	 * @param matchers
	 * @return a new matcher
	 */
	public static <T> Matcher<T> mOr(Matcher<T>... matchers) {
		return new OrMatcher<T>(matchers);
	}

	/**
	 * Return a new matcher that matches if
	 * the argument is an instance of any of the classes.
	 *
	 * @return a new matcher
	 */
	public static <T> Matcher<T> mType(Class<T> clazz, Class<?>... classes) {
		return new ClassMatcher<T>(clazz, classes);
	}


	/**
	 * Returns a new matcher that
	 * compares the value with the actual parameter based on equality.
	 * For primitive arrays, it compares based in array content, instead of array identity.
	 * This is also recursive, so it handles values such as int[][], et.c.
	 *
	 * @param value
	 * @return a new matcher
	 */
	public static <T> Matcher<T> mEq(T value) {
		return new EqualityMatcher<T>(value);
	}

	public static <T> Matcher<T> matchEq(T value, Class<T> clazz) {
		return mEq(value);
	}

	/**
	 * Returns a new matcher that
	 * compares the identity of the value with the actual parameter
	 *
	 * @param value
	 * @return a new matcher
	 */
	public static <T> Matcher<T> mSame(T value) {
		return new IdentityMatcher<T>(value);
	}

	/**
	 * Return a new matcher that always matches.
	 *
	 * @param clazz - the return type
	 * @return a new matcher
	 */
	public static <T> Matcher<T> mAny(Class<T> clazz) {
		return new AnyMatcher<T>(clazz);
	}

	/**
	 * Return a new matcher that always matches.
	 *
	 * @return a new matcher
	 */
	public static Matcher<Integer> mAnyInt() {
		return ClassMatcher.anyInt();
	}

	/**
	 * Return a new matcher that always matches.
	 *
	 * @return a new matcher
	 */
	public static Matcher<Long> mAnyLong() {
		return ClassMatcher.anyLong();
	}

	/**
	 * Return a new matcher that always matches.
	 *
	 * @return a new matcher
	 */
	public static Matcher<Double> mAnyDouble() {
		return ClassMatcher.anyDouble();
	}

	/**
	 * Return a new matcher that always matches.
	 *
	 * @return a new matcher
	 */
	public static Matcher<Float> mAnyFloat() {
		return ClassMatcher.anyFloat();
	}

	/**
	 * Return a new matcher that always matches.
	 *
	 * @return a new matcher
	 */
	public static Matcher<Short> mAnyShort() {
		return ClassMatcher.anyShort();
	}

	/**
	 * Return a new matcher that always matches.
	 *
	 * @return a new matcher
	 */
	public static Matcher<Byte> mAnyByte() {
		return ClassMatcher.anyByte();
	}

	/**
	 * Return a new matcher that always matches.
	 *
	 * @return a new matcher
	 */
	public static Matcher<Boolean> mAnyBoolean() {
		return ClassMatcher.anyBoolean();
	}

	/**
	 * Return a new matcher that always matches.
	 *
	 * @return a new matcher
	 */
	public static Matcher<Character> mAnyChar() {
		return ClassMatcher.anyChar();
	}

	/**
	 * Return a new matcher that matches against a regular expression
	 *
	 * @param s
	 * @return a new matcher
	 */
	public static Matcher<String> mRegexp(String s) {
		return new RegexpMatcher(s);
	}

	/**
	 * Return a new matcher that matches if the argument contains s.
	 *
	 * @param s
	 * @return a new matcher
	 */
	public static Matcher<String> mContains(String s) {
		return new RegexpMatcher(".*" + Formatting.quote(s) + ".*");
	}

	/**
	 * Return a new matcher that matches if the argument starts with s.
	 *
	 * @param s
	 * @return a new matcher
	 */
	public static Matcher<String> mStartsWith(String s) {
		return new RegexpMatcher(Formatting.quote(s) + ".*");
	}

	/**
	 * Return a new matcher that matches if the argument ends with s.
	 *
	 * @param s
	 * @return a new matcher
	 */
	public static Matcher<String> mEndsWith(String s) {
		return new RegexpMatcher(".*" + Formatting.quote(s));
	}
}

