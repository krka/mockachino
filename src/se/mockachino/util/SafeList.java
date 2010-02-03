package se.mockachino.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;

public class SafeList<T> extends ArrayList<T> {

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			int index = 0;
			@Override
			public boolean hasNext() {
				if (index >= size()) {
					return false;
				}
				try {
					return get(index) != null;
				} catch (Exception e) {
					return false;
				}

			}

			@Override
			public T next() {
				try {
					return get(index);
				} catch (Exception e) {
					return null;
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
}
