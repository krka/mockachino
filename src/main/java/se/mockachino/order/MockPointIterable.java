package se.mockachino.order;

import se.mockachino.Invocation;

import java.util.Iterator;

public class MockPointIterable<T> implements Iterable<Invocation<T>> {
	private final Iterable<Invocation<T>> list;
	private final MockPoint start;
	private final MockPoint end;

	public MockPointIterable(Iterable<Invocation<T>> list, MockPoint start, MockPoint end) {
		this.list = list;
		this.start = start;
		this.end = end;
	}

	@Override
	public Iterator<Invocation<T>> iterator() {
		return new MockPointIterator<T>(list.iterator(), start, end);
	}
}
