package se.mockachino.matchers;

public class Matchers {

	public static <T> T isNull() {
		MatcherThreadHandler.pushMatcher(new NullMatcher());
		return null;
	}

	public static <T> T regexp(final String s) {
		MatcherThreadHandler.pushMatcher(new RegexpMatcher(s));
		return null;
	}

	public static <T> T any() {
		MatcherThreadHandler.pushMatcher(new AnyMatcher());
		return null;
	}

	public static int anyInt() {
		MatcherThreadHandler.pushMatcher(new AnyIntMatcher());
		return MagicNumbers.INT_MATCHER;
	}

	public static int eq(int value) {
		MatcherThreadHandler.pushMatcher(new EqualityMatcher(value));
		return MagicNumbers.INT_MATCHER;
	}
}
