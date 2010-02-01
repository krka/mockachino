package se.mockachino.matchers;

import se.mockachino.util.Formatting;

public class IdentityMatcher<T> extends Matcher<T> {
	private final T value;
	private final boolean same;

	public IdentityMatcher(T value, boolean same) {
		this.value = value;
		this.same = same;
	}

	@Override
	public boolean matches(T value) {
		return (value == this.value) == same;

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
