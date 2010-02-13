package se.mockachino.matchers;

import se.mockachino.matchers.matcher.Matcher;

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
 * Apart from using custom matchers, you can use some predefined ones.
 * All methods here that return a Matcher needs to be wrapped in Matchers.match()
 * when using in an argument.
 * The methods that return a primitive or a &lt;T> have already been wrapped in Matchers.match()
 * and can be used directly as is.
 *
 * <p>
 * Example:
 * <pre>
 * stubReturn(1).on(myMock).get(match(mAnyInt()));
 * stubReturn(1).on(myMock).get(m(mAnyInt()));
 * stubReturn(1).on(myMock).get(anyInt());
 * </pre>
 *
 *
 */
public class Matchers extends MatchersBase {
	public static <T> T m(Matcher<T> matcher) {
		return match(matcher);
	}

	// Boolean shortcuts

	public static <T> T same(T value) {
		return match(mSame(value));
	}

	public static <T> T notSame(T value) {
		return not(mSame(value));
	}

	public static <T> T eq(T value) {
		return match(mEq(value));
	}

	public static <T> T notEq(T value) {
		return not(mEq(value));
	}

	public static <T> T and(Matcher<T>... matchers) {
		return match(mAnd(matchers));
	}

	public static <T> T or(Matcher<T>... matchers) {
		return match(mOr(matchers));
	}

	public static <T> T not(Matcher<T> matcher) {
		return match(mNot(matcher));
	}

	public static boolean anyBoolean() {
		return m(mAnyBoolean());
	}

	// String shortcuts

	public static String contains(String s) {
		return m(mContains(s));
	}

	public static String startsWith(String s) {
		return m(mStartsWith(s));
	}

	public static String endsWith(String s) {
		return m(mEndsWith(s));
	}

	public static String regexp(String s) {
		return match(mRegexp(s));
	}

	// Primitive shortcuts

	public static <T> T any(Class<T> clazz) {
		return match(mAny(clazz));
	}

	public static char anyChar() {
		return m(mAnyChar());
	}

	public static long anyLong() {
		return m(mAnyLong());
	}

	public static float anyFloat() {
		return m(mAnyFloat());
	}

	public static double anyDouble() {
		return m(mAnyDouble());
	}

	public static short anyShort() {
		return m(mAnyShort());
	}

	public static byte anyByte() {
		return m(mAnyByte());
	}

	public static int anyInt() {
		return m(mAnyInt());
	}

	// Type shortcut
	public static <T> T type(Class<T> clazz, Class<?>... classes) {
		return match(mType(clazz, classes));
	}


}