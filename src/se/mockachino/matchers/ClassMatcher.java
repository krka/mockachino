package se.mockachino.matchers;

public class ClassMatcher<T> implements Matcher<T> {
	private final Class clazz;
	private final Class[] classes;

	public ClassMatcher(Class clazz, Class... classes) {
		this.clazz = clazz;
		this.classes = classes;
	}

	@Override
	public boolean matches(Object value) {
		if (clazz.isInstance(value)) {
			return true;
		}
		for (Class clazz : classes) {
			if (clazz.isInstance(value)) {
				return true;
			}
		}
		return false;
	}

	public String toString() {
		return "<type:" + list() + ">";
	}

	private String list() {
		StringBuilder builder = new StringBuilder();
		builder.append(clazz.getSimpleName());
		for (Class arg : classes) {
			builder.append(",");
			builder.append(arg.getSimpleName());
		}
		return builder.toString();
	}

	@Override
	public Class<T> getType() {
		return clazz;
	}
}
