package se.mockachino.matchers;

public class AnyMatcher<T> implements Matcher<T> {
	private final Class<T> clazz;

	public AnyMatcher(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public boolean matches(T value) {
		return true;
	}

	@Override
	public Class<T> getType() {
		return clazz;
	}
}
