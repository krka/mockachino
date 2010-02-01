package se.mockachino.matchers;

public abstract class Matcher<T> {
	abstract boolean matches(T value);
	abstract Class<T> getType();
	public final String toString() {
		return asString();
	}

	protected abstract String asString();
}
