package se.mockachino.matchers.matcher;

public class AnyMatcher<T> extends BasicMatcher<T> {
	private final Class<T> clazz;

	public AnyMatcher(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public boolean matches(T value) {
		return true;
	}

	@Override
	public Class<T> getType() {
		return clazz;
	}

	@Override
	protected String asString() {
		return "<mAny>";
	}
}
