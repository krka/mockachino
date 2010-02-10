package se.mockachino.matchers;

import se.mockachino.matchers.matcher.*;

/**
 * Matchers is a utility class, containing a lot of useful default matchers.
 *
 * <p>
 * A matcher is just a plain java object of the {@link se.mockachino.matchers.matcher.Matcher} type,
 * which can be used by verifying and stubbing.
 *
 * <p>
 * The only important thing is that when using a matcher for a method call, you need to wrap it in
 * Matchers.match() (or Matchers.m() as a shortcut).
 *
 * <p>
 * This wrapping MUST be directly in the actual method call for it to work.
 * Example:
 * <pre>
 * List myMock = mock(List.class);
 * Matcher<Integer> myMatcher = new Matcher<Integer>{...};
 *
 * // won't work, get expects an integer, not a matcher
 * stubReturn(1).on(myMock).get(myMatcher));
 *
 * // Will work:
 * stubReturn(1).on(myMock).get(match(myMatcher)));
 * </pre>
 *
 * <p>
 * Apart from using custom matchers, you can use some predefined.
 * All methods here that return a Matcher needs to be wrapped in Matchers.match()
 * when using in an argument.
 * The methods that return a primitive or a &lt;T> have already been wrapped in Matchers.match()
 * and can be used directly as is.
 *
 * <p>
 * Example:
 * <pre>
 * stubReturn(1).on(myMock).get(match(anyInt()));
 * stubReturn(1).on(myMock).get(m(anyInt()));
 * stubReturn(1).on(myMock).get(mAnyInt());
 * </pre>
 *
 *
 */
public class Matchers extends MatchersBase {
	public static <T> T m(Matcher<T> matcher) {
		return match(matcher);
	}

	// Null shortcuts

	public static <T> T mNotNull() {
		return m(Matchers.<T>notNull());
	}

	public static <T> T mNull() {
		return m(Matchers.<T>isNull());
	}

	// Boolean shortcuts

	public static <T> T mSame(T value) {
		return match(same(value));
	}

	public static <T> T mNotSame(T value) {
		return mNot(same(value));
	}

	public static <T> T mEq(T value) {
		return match(eq(value));
	}

	public static <T> T mNotEq(T value) {
		return mNot(eq(value));
	}

	public static <T> T mAnd(Matcher<T>... matchers) {
		return match(and(matchers));
	}

	public static <T> T mOr(Matcher<T>... matchers) {
		return match(or(matchers));
	}

	public static <T> T mNot(Matcher<T> matcher) {
		return match(not(matcher));
	}

	public static boolean mAnyBoolean() {
		return m(anyBoolean());
	}

	public static boolean mTrue() {
		return mEq(Boolean.TRUE);
	}


	public static boolean mFalse() {
		return mEq(Boolean.FALSE);
	}

	// String shortcuts


	public static String mContains(String s) {
		return m(contains(s));
	}

	public static String mStartsWith(String s) {
		return m(startsWith(s));
	}

	public static String mEndsWith(String s) {
		return m(endsWith(s));
	}

	public static String mRegexp(String s) {
		return match(regexp(s));
	}

	// Primitive shortcuts

	public static <T> T mAny(Class<T> clazz) {
		return match(any(clazz));
	}

	public static char mAnyChar() {
		return m(anyChar());
	}

	public static long mAnyLong() {
		return m(anyLong());
	}

	public static float mAnyFloat() {
		return m(anyFloat());
	}

	public static double mAnyDouble() {
		return m(anyDouble());
	}

	public static short mAnyShort() {
		return m(anyShort());
	}

	public static byte mAnyByte() {
		return m(anyByte());
	}

	public static int mAnyInt() {
		return m(anyInt());
	}

	// Type shortcut
	public static <T> T mType(Class<T> clazz, Class<?>... classes) {
		return match(type(clazz, classes));
	}


}