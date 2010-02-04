package se.mockachino.matchers.matcher;

public class ArgumentCatcher<T> extends Matcher<T> {
	private final Matcher<T> delegate;
	private T value;

	protected ArgumentCatcher(Matcher<T> delegate) {
		this.delegate = delegate;
	}

	public static <T> ArgumentCatcher<T> create(Matcher<T> delegate) {
		return new ArgumentCatcher<T>(delegate);
	}

	@Override
	public boolean matches(T value) {
		this.value = value;
		return delegate.matches(value);
	}

	@Override
	public Class<T> getType() {
		return delegate.getType();
	}

	@Override
	protected String asString() {
		return delegate.asString();
	}

	public T getValue() {
		return value;
	}
}
