package se.mockachino.matchers.matcher;

public interface Matcher<T> {
	boolean matches(T value);
	Class<T> getType();
}
