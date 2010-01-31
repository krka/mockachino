package se.mockachino.matchers;

public class ClassMatcher implements Matcher {
	private final Class[] clazz;

	public ClassMatcher(Class... clazz) {
		this.clazz = clazz;
	}

	@Override
	public boolean matches(Object value) {
		for (Class aClass : clazz) {
			if (aClass.isInstance(value)) {
				return true;
			}
		}
		return false;
	}

	public String toString() {
		return "Any:<" + list() + ">";
	}

	private String list() {
		StringBuilder builder = new StringBuilder();
		for (Class arg : clazz) {
			if (builder.length() > 0) {
				builder.append(",");
			}
			builder.append(arg.getSimpleName());
		}
		return builder.toString();
	}

}
