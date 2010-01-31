package se.mockachino.matchers;

import java.util.Random;

public class MagicNumbers {
	private static final Random random = new Random();
	static final int INT_MATCHER = Integer.MIN_VALUE + random.nextInt(Integer.MAX_VALUE / 4);

	public static Matcher getMatcher(Object value) {
		if (value == null) {
			return MatcherThreadHandler.getMatcher();
		}
		if (value instanceof Matcher) {
			return (Matcher) value;
		}
		if (value instanceof Integer && value.equals(INT_MATCHER)) {
			return MatcherThreadHandler.getMatcher();
		}
		return new EqualityMatcher(value);
	}
}
