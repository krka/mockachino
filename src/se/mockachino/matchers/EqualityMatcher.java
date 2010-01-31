package se.mockachino.matchers;

import se.mockachino.util.Formatting;

public class EqualityMatcher implements Matcher {
	private final Object value;

	public EqualityMatcher(Object value) {
		this.value = value;
	}

	public boolean matches(Object other) {
		if (value == null) {
			return other == null;
		}
		return value.equals(other);
	}

	@Override
	public String toString() {
		return Formatting.argument(value);
	}
}
