package se.mockachino.matchers;

public class NotMatcher<T> implements Matcher<T> {
	private final Matcher<T> matcher;

	public NotMatcher(Matcher<T> matcher) {
		this.matcher = matcher;
	}

	@Override
	public boolean matches(T value) {
		return !matcher.matches(value);
	}

	@Override
	public Class<T> getType() {
		return matcher.getType();
	}
}
