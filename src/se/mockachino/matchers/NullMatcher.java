package se.mockachino.matchers;

public class NullMatcher implements Matcher {
	@Override
	public boolean matches(Object value) {
		return value == null;
	}

	public String toString() {
		return "null";
	}
}
