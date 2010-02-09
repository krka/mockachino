package se.mockachino.matchers.matcher;

public class NotMatcher<T> extends BasicMatcher<T> {
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
		return "!" + delegate.toString();

	}
}
