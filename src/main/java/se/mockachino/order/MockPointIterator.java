package se.mockachino.order;

import se.mockachino.Invocation;

import java.util.Iterator;

public class MockPointIterator<T> implements Iterator<Invocation<T>> {
	private final Iterator<Invocation<T>> iterator;
	private final MockPoint start;
	private final MockPoint end;

	private Invocation next;

	public MockPointIterator(Iterator<Invocation<T>> iterator, MockPoint start, MockPoint end) {
		this.iterator = iterator;
		this.start = start;
		this.end = end;
	}

	@Override
	public boolean hasNext() {
		if (next != null) {
			return true;
		}
		next = fetchNext();
		return next != null;
	}

	@Override
	public Invocation<T> next() {
		Invocation<T> obj = next;
		next = null;
		if (obj == null) {
			obj = fetchNext();
		}
		return obj;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("remove() is unsupported");
	}

	public Invocation<T> fetchNext() {
		while (iterator.hasNext()) {
			Invocation<T> obj = iterator.next();
			if (obj.getCallNumber() > end.getCallNumber()) {
				return null;
			}
			if (obj.getCallNumber() >= start.getCallNumber()) {
				return obj;
			}
		}
		return null;
	}
}
