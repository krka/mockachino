package se.mockachino.matchers.matcher;

import se.mockachino.util.SafeIteratorList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArgumentCatcher<T> extends BasicMatcher<T> {
	private final Matcher<T> delegate;
	private final List<T> values = new SafeIteratorList<T>(new ArrayList<T>(), null);

	protected ArgumentCatcher(Matcher<T> delegate) {
		this.delegate = delegate;
	}

	public static <T> ArgumentCatcher<T> create(Matcher<T> delegate) {
		return new ArgumentCatcher<T>(delegate);
	}

	@Override
	public boolean matches(T value) {
		boolean isMatch = delegate.matches(value);
		if (isMatch) {
			values.add(value);
		}
		return isMatch;
	}

	@Override
	public Class<T> getType() {
		return delegate.getType();
	}

	@Override
	protected String asString() {
		return delegate.toString();
	}

	public T getValue() {
		return values.get(values.size() - 1);
	}

	public Iterator<T> getValues() {
		return values.iterator();
	}
}
