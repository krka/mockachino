package se.mockachino.matchers;

import java.util.Queue;
import java.util.LinkedList;

public class MatcherThreadHandler {
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
