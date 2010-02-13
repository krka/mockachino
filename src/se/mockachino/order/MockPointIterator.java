package se.mockachino.order;

import se.mockachino.MethodCall;

import java.util.Iterator;

public class MockPointIterator implements Iterator<MethodCall> {
	private final Iterator<MethodCall> iterator;
	private final MockPoint start;
	private final MockPoint end;

	private MethodCall next;

	public MockPointIterator(Iterator<MethodCall> iterator, MockPoint start, MockPoint end) {
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
	public MethodCall next() {
		MethodCall obj = next;
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

	public MethodCall fetchNext() {
		while (iterator.hasNext()) {
			MethodCall obj = iterator.next();
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
