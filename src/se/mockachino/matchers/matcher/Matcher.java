package se.mockachino.matchers.matcher;

public abstract class Matcher<T> {
	public abstract boolean matches(T value);
	public abstract Class<T> getType();
	public final String toString() {
		return asString();
	}

	protected abstract String asString();
}
