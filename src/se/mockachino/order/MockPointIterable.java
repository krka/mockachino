package se.mockachino.order;

import se.mockachino.MethodCall;

import java.util.Iterator;

public class MockPointIterable implements Iterable<MethodCall> {
	private final Iterable<MethodCall> list;
	private final MockPoint start;
	private final MockPoint end;

	public MockPointIterable(Iterable<MethodCall> list, MockPoint start, MockPoint end) {
		this.list = list;
		this.start = start;
		this.end = end;
	}

	@Override
	public Iterator<MethodCall> iterator() {
		return new MockPointIterator(list.iterator(), start, end);
	}
}
