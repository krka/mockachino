package se.mockachino.matchers;

import se.mockachino.matchers.matcher.EqualityMatcher;
import se.mockachino.matchers.matcher.Matcher;

import java.util.Queue;
import java.util.LinkedList;

public class MatcherThreadHandler {
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

	private static final ThreadLocal<Queue<Matcher>> matchers = new ThreadLocal<Queue<Matcher>>() {
		@Override
		protected Queue<Matcher> initialValue() {
			return new LinkedList<Matcher>();
		}
	};

	public static void pushMatcher(Matcher m) {
		matchers.get().add(m);
	}

	static Matcher getMatcher() {
		return matchers.get().remove();
	}

	public static void assertEmpty() {
		if (matchers.get().size() > 0) {
			matchers.get().clear();
			throw new RuntimeException("matchers called in wrong context");
		}

	}

	public static boolean isClean() {
		return matchers.get().isEmpty();
	}
}
