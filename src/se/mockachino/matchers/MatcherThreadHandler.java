package se.mockachino.matchers;

import se.mockachino.exceptions.UsageError;
import se.mockachino.matchers.matcher.EqualityMatcher;
import se.mockachino.matchers.matcher.Matcher;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.Queue;

public class MatcherThreadHandler {
	private static final Character ZERO_CHARACTER = Character.valueOf('\0');

	public static Matcher getMatcher(Object value, boolean varArgs) {
		if (value == null) {
			return MatcherThreadHandler.getMatcher();
		}

		// Support for varargs
		if (varArgs && value.getClass().isArray()) {
			int n = Array.getLength(value);
			Matcher[] matchers = new Matcher[n];
			for (int i = 0; i < n; i++) {
				matchers[i] = getMatcher(Array.get(value, i), false);
			}
			return new ArrayMatcher(matchers, value.getClass().getComponentType(), true);
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

	private static Matcher getMatcher() {
		try {
			return matchers.get().remove();
		} catch (Exception e) {
			matchers.get().clear();
			throw new UsageError(
					"Illegal mix of matchers and default values in the same method verification or stubbing. \n" +
							"Replace null, 0, '\\0' and false with matchers.");
		}
	}

	public static void assertEmpty() {
		if (matchers.get().size() > 0) {
			matchers.get().clear();
			throw new UsageError(
					"Matchers called in wrong context. " +
							"Only use matchers inside verification or stubbing method calls.");
		}

	}

	public static boolean isClean() {
		return matchers.get().isEmpty();
	}
}
