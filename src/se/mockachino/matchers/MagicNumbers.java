package se.mockachino.matchers;

import java.util.Random;

public class MagicNumbers {
	private static final Character ZERO_CHARACTER = Character.valueOf('\0');

	public static Matcher getMatcher(Object value) {
		if (value == null) {
			return MatcherThreadHandler.getMatcher();
		}
		if (value instanceof Matcher) {
			return (Matcher) value;
		}
		if (value instanceof Number) {
			if (((Number) value).longValue() == 0) {
				return MatcherThreadHandler.getMatcher();
			}
			return new EqualityMatcher(value);
		}
		if (Boolean.FALSE.equals(value)) {
			return MatcherThreadHandler.getMatcher();
		}
		if (ZERO_CHARACTER.equals(value)) {
			return MatcherThreadHandler.getMatcher();
		}
		return new EqualityMatcher(value);
	}
}
