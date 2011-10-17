package se.mockachino.order;

import se.mockachino.Invocation;

import java.util.Iterator;

public class MockPointIterable implements Iterable<Invocation> {
	private final Iterable<Invocation> list;
	private final MockPoint start;
	private final MockPoint end;

	public MockPointIterable(Iterable<Invocation> list, MockPoint start, MockPoint end) {
		this.list = list;
		this.start = start;
		this.end = end;
	}

	@Override
	public Iterator<Invocation> iterator() {
		return new MockPointIterator(list.iterator(), start, end);
	}
}
