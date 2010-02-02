package se.mockachino.matchers.matcher;

public class RegexpMatcher extends Matcher<String> {
	private final String s;

	public RegexpMatcher(String s) {
		this.s = s;
	}

	@Override
	public boolean matches(String value) {
		if (value == null) {
			return false;
		}
		return value.matches(s);
	}

	@Override
	public Class<String> getType() {
		return String.class;
	}

	public String asString() {
		return "regexp(\"" + s + "\")";
	}

}
