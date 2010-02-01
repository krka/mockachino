package se.mockachino.matchers;

public interface Matcher<T> {
	boolean matches(T value);
	Class<T> getType();
}
