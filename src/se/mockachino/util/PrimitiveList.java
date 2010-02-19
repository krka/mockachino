package se.mockachino.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class PrimitiveList {
	private final List list;
	private final Object obj;

	public PrimitiveList(Object obj) {
		this.obj = obj;
		list = new ArrayList();
		int len = Array.getLength(obj);
		for (int i = 0; i < len; i++) {
			list.add(toList(Array.get(obj, i)));
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!(o instanceof PrimitiveList)) {
			return false;
		}
		PrimitiveList other = (PrimitiveList) o;
		return list.equals(other.list);
	}

	@Override
	public int hashCode() {
		return list.hashCode();
	}

	@Override
	public String toString() {
		Class<?> type = obj.getClass().getComponentType();

		return type.getSimpleName() + "[]{" + Formatting.join(", ", list) + "}";
	}

	public static Object toList(Object arg) {
		if (arg == null) {
			return null;
		}
		if (arg.getClass().isArray()) {
			return new PrimitiveList(arg);
		}
		return arg;
	}
}
