package se.mockachino.matchers;

import se.mockachino.util.Formatting;

public class EqualityMatcher<T> extends Matcher<T> {
	private final Object value;

	public EqualityMatcher(Object value) {
		this.value = value;
	}

	@Override
	public boolean matches(T other) {
		if (value == null) {
			return other == null;
		}
		return value.equals(other);
	}

	@Override
	public String asString() {
		return Formatting.argument(value);
	}

	@Override
	public Class<T> getType() {
		return (Class<T>) value.getClass();
	}
}
