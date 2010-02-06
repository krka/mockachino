package se.mockachino.matchers.matcher;

import java.util.regex.Pattern;

public class RegexpMatcher extends Matcher<String> {
	private final Pattern pattern;

	public RegexpMatcher(String s) {
		pattern = Pattern.compile(s);
	}

	public RegexpMatcher(Pattern pattern) {
		this.pattern = pattern;
	}

	@Override
	public boolean matches(String value) {
		if (value == null) {
			return false;
		}
		return pattern.matcher(value).matches();
	}

	@Override
	public Class<String> getType() {
		return String.class;
	}

	public String asString() {
		return "regexp(\"" + pattern + "\")";
	}

}
