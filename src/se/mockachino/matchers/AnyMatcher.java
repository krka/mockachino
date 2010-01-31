package se.mockachino.matchers;

public class AnyMatcher implements Matcher {
	@Override
	public boolean matches(Object value) {
		return true;
	}

	public String toString() {
		return "<any>";
	}
}
