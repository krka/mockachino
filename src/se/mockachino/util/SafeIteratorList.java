package se.mockachino.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SafeIteratorList<T> implements List<T> {
	private final List<T> delegate;
	private final Object lock = new Object();
	private final T nullObject;

	public SafeIteratorList(List<T> delegate, T nullObject) {
		this.delegate = delegate;
		this.nullObject = nullObject;
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			int index = 0;
			@Override
			public boolean hasNext() {
				if (index >= size()) {
					return false;
				}
				return get(index) != nullObject;
			}

			@Override
			public T next() {
				try {
					return get(index);
				} finally {
					index++;
				}
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Tried to modify a read only iterator");
			}
		};
	}


	@Override
	public int size() {
		synchronized (lock) {
			return delegate.size();
		}
	}

	@Override
	public boolean isEmpty() {
		synchronized (lock) {
			return delegate.isEmpty();
		}
	}

	@Override
	public boolean contains(Object o) {
		synchronized (lock) {
			return delegate.contains(o);
		}
	}

	@Override
	public Object[] toArray() {
		synchronized (lock) {
			return delegate.toArray();
		}
	}

	@Override
	public <T> T[] toArray(T[] ts) {
		synchronized (lock) {
			return delegate.toArray(ts);
		}
	}

	@Override
	public boolean add(T t) {
		synchronized (lock) {
			return delegate.add(t);
		}
	}

	@Override
	public boolean remove(Object o) {
		synchronized (lock) {
			return delegate.remove(o);
		}
	}

	@Override
	public boolean containsAll(Collection<?> objects) {
		synchronized (lock) {
			return delegate.containsAll(objects);
		}
	}

	@Override
	public boolean addAll(Collection<? extends T> ts) {
		synchronized (lock) {
			return delegate.addAll(ts);
		}
	}

	@Override
	public boolean addAll(int i, Collection<? extends T> ts) {
		synchronized (lock) {
			return delegate.addAll(i, ts);
		}
	}

	@Override
	public boolean removeAll(Collection<?> objects) {
		synchronized (lock) {
			return delegate.removeAll(objects);
		}
	}

	@Override
	public boolean retainAll(Collection<?> objects) {
		synchronized (lock) {
			return delegate.retainAll(objects);
		}
	}

	@Override
	public void clear() {
		synchronized (lock) {
			delegate.clear();
		}
	}

	@Override
	public T get(int i) {
		synchronized (lock) {
			if (i < 0 || i >= size()) {
				return nullObject;
			}
			try {
				return delegate.get(i);
			} catch (IndexOutOfBoundsException e) {
				return nullObject;
			}
		}
	}

	@Override
	public T set(int i, T t) {
		synchronized (lock) {
			return delegate.set(i, t);
		}
	}

	@Override
	public void add(int i, T t) {
		synchronized (lock) {
			delegate.add(i, t);
		}
	}

	@Override
	public T remove(int i) {
		synchronized (lock) {
			return delegate.remove(i);
		}
	}

	@Override
	public int indexOf(Object o) {
		synchronized (lock) {
			return delegate.indexOf(o);
		}
	}

	@Override
	public int lastIndexOf(Object o) {
		synchronized (lock) {
			return delegate.lastIndexOf(o);
		}
	}

	@Override
	public ListIterator<T> listIterator() {
		synchronized (lock) {
			return delegate.listIterator();
		}
	}

	@Override
	public ListIterator<T> listIterator(int i) {
		synchronized (lock) {
			return delegate.listIterator(i);
		}
	}

	@Override
	public List<T> subList(int i, int i1) {
		synchronized (lock) {
			return delegate.subList(i, i1);
		}
	}
}
