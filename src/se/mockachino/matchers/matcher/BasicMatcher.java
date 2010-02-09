package se.mockachino.matchers.matcher;

public abstract class BasicMatcher<T> implements Matcher<T> {
	public final String toString() {
		return asString();
	}
	protected abstract String asString();
}
