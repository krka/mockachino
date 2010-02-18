package se.mockachino.matchers.matcher;

import se.mockachino.Primitives;
import se.mockachino.util.Formatting;

public class EqualityMatcher<T> extends BasicMatcher<T> {
	private final Object value;

	public EqualityMatcher(Object value) {
		this.value = Primitives.toList(value);
	}

	@Override
	public boolean matches(T other) {
		if (value == null) {
			return other == null;
		}
		other = (T) Primitives.toList(other);
		return value.equals(other);
	}

	@Override
	public String asString() {
		return Formatting.argument(value);
	}

	@Override
	public Class<T> getType() {
		if (value != null) {
			return (Class<T>) value.getClass();
		}
		return (Class<T>) Object.class;

	}
}
