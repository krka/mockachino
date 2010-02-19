package se.mockachino.matchers;

import se.mockachino.Primitives;
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

	/**
	 * Matches using a custom matcher.
	 * @param matcher
	 */
	public static <T> T match(Matcher<T> matcher) {
		MatcherThreadHandler.pushMatcher(matcher);
		return Primitives.forType(matcher.getType());
	}
	
	/**
	 * Shortcut for {@link #match}
	 */
	public static <T> T m(Matcher<T> matcher) {
		return match(matcher);
	}

	/**
	 * Compares value with the actual parameter based on object identity.
	 * Don't use this matcher for primitive data types.
	 *
	 * @param value the value
	 * @return
	 */
	public static <T> T same(T value) {
		return match(mSame(value));
	}

	/**
	 * Matches negation of {@link #same}
	 */
	public static <T> T notSame(T value) {
		return not(mSame(value));
	}

	/**
	 * Compares value with the actual parameter based on equality.
	 * For primitive arrays, it compares based in array content, instead of array identity.
	 * This is also recursive, so it handles values such as int[][], et.c.
	 *
	 * @param value
	 */
	public static <T> T eq(T value) {
		return match(mEq(value));
	}

	/**
	 * Matches negation of {@link #eq}
	 */
	public static <T> T notEq(T value) {
		return not(mEq(value));
	}

	/**
	 * Matches the parameter if and only if all the matchers match.
	 *
	 * If the list of matchers is empty, it defaults to true.
	 *
	 * @param matchers
	 */
	public static <T> T and(Matcher<T>... matchers) {
		return match(mAnd(matchers));
	}

	/**
	 * Matches the parameter if any of the matchers match.
	 *
	 * If the list of matchers is empty, it defaults to false.
	 *
	 * @param matchers
	 */
	public static <T> T or(Matcher<T>... matchers) {
		return match(mOr(matchers));
	}

	/**
	 * Negates the matching of the specified matcher.
	 * @param matcher
	 */
	public static <T> T not(Matcher<T> matcher) {
		return match(mNot(matcher));
	}

	/**
	 * Matches if the parameter value contains s.
	 *
	 * @param s the string to search for
	 */
	public static String contains(String s) {
		return m(mContains(s));
	}

	/**
	 * Matches if the parameter value starts with s.
	 *
	 * @param s the string to search for
	 */
	public static String startsWith(String s) {
		return m(mStartsWith(s));
	}

	/**
	 * Matches if the parameter value ends with s.
	 *
	 * @param s the string to search for
	 */
	public static String endsWith(String s) {
		return m(mEndsWith(s));
	}

	/**
	 * Matches if the parameter value matches the regular expression.
	 *
	 * @param s the string to search for
	 */
	public static String regexp(String s) {
		return match(mRegexp(s));
	}

	/**
	 * Always matches.
	 */
	public static boolean anyBoolean() {
		return m(mAnyBoolean());
	}

	/**
	 * Always matches.
	 */
	public static <T> T any(Class<T> clazz) {
		return match(mAny(clazz));
	}

	/**
	 * Always matches.
	 */
	public static char anyChar() {
		return m(mAnyChar());
	}

	/**
	 * Always matches.
	 */
	public static long anyLong() {
		return m(mAnyLong());
	}

	/**
	 * Always matches.
	 */
	public static float anyFloat() {
		return m(mAnyFloat());
	}

	/**
	 * Always matches.
	 */
	public static double anyDouble() {
		return m(mAnyDouble());
	}

	/**
	 * Always matches.
	 */
	public static short anyShort() {
		return m(mAnyShort());
	}

	/**
	 * Always matches.
	 */
	public static byte anyByte() {
		return m(mAnyByte());
	}

	/**
	 * Always matches.
	 */
	public static int anyInt() {
		return m(mAnyInt());
	}

	/**
	 * Matches if the argument is an instance of any of the classes.
	 */
	public static <T> T type(Class<T> clazz, Class<?>... classes) {
		return match(mType(clazz, classes));
	}

}