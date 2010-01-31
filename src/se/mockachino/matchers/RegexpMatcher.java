package se.mockachino.matchers;

class RegexpMatcher implements Matcher {
	private final String s;

	public RegexpMatcher(String s) {
		this.s = s;
	}

	public boolean matches(Object value) {
		if (value == null) {
			return false;
		}
		if (value instanceof String) {
			return ((String) value).matches(s);
		}
		return false;
	}

	public String toString() {
		return "regexp(\"" + s + "\")";
	}
}
