package se.mockachino.matchers;

public class NotMatcher<T> extends Matcher<T> {
	private final Matcher<T> delegate;

	public NotMatcher(Matcher<T> delegate) {
		this.delegate = delegate;
	}

	@Override
	public boolean matches(T value) {
		return !delegate.matches(value);
	}

	@Override
	public Class<T> getType() {
		return delegate.getType();
	}

	@Override
	protected String asString() {
		return "!" + delegate.asString();

	}
}
