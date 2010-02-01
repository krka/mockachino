package se.mockachino.matchers;

import se.mockachino.util.Formatting;

public class InequalityMatcher<T> implements Matcher<T> {
	private final Object value;

	public InequalityMatcher(Object value) {
		this.value = value;
	}

	public boolean matches(T other) {
		if (value == null) {
			return other != null;
		}
		return !value.equals(other);
	}

	@Override
	public String toString() {
		return "!" + Formatting.argument(value);
	}

	@Override
	public Class<T> getType() {
		return (Class<T>) value.getClass();
	}
}