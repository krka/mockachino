package se.mockachino.matchers.matcher;

import se.mockachino.util.Formatting;

public class IdentityMatcher<T> extends Matcher<T> {
	private final T value;

	public IdentityMatcher(T value) {
		this.value = value;
	}

	@Override
	public boolean matches(T value) {
		return value == this.value;

	}

	@Override
	public Class<T> getType() {
		if (value != null) {
			return (Class<T>) value.getClass();
		}
		return (Class<T>) Object.class;
	}

	@Override
	protected String asString() {
		return Formatting.argument(value);
	}
}
