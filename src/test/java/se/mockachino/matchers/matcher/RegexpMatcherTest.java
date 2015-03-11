package se.mockachino.matchers.matcher;

import org.junit.Test;
import se.mockachino.matchers.MatchersBase;

import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

public class RegexpMatcherTest {
	@Test
	public void testSimple() {
		RegexpMatcher matcher = new RegexpMatcher(".*A.*");
		assertEquals("regexp(\".*A.*\")", matcher.asString());
		assertEquals(false, matcher.matches(null));
		assertEquals(false, matcher.matches("B"));
		assertEquals(true, matcher.matches("BAB"));
		assertEquals(true, matcher.matches("A"));
	}

	@Test
	public void testPattern() {
		RegexpMatcher matcher = new RegexpMatcher(Pattern.compile(".*A.*"));
		assertEquals("regexp(\".*A.*\")", matcher.asString());
		assertEquals(false, matcher.matches(null));
		assertEquals(false, matcher.matches("B"));
		assertEquals(true, matcher.matches("BAB"));
		assertEquals(true, matcher.matches("A"));
	}

	@Test
	public void testContains() {
		Matcher<String> matcher = MatchersBase.mContains("A");
		assertEquals("regexp(\".*A.*\")", matcher.toString());
		assertEquals(false, matcher.matches("B"));
		assertEquals(true, matcher.matches("BAB"));
		assertEquals(true, matcher.matches("A"));
	}

	@Test
	public void testStartsWith() {
		Matcher<String> matcher = MatchersBase.mStartsWith("A");
		assertEquals("regexp(\"A.*\")", matcher.toString());
		assertEquals(false, matcher.matches("B"));
		assertEquals(true, matcher.matches("AB"));
		assertEquals(true, matcher.matches("A"));
	}

	@Test
	public void testEndsWith() {
		Matcher<String> matcher = MatchersBase.mEndsWith("A");
		assertEquals("regexp(\".*A\")", matcher.toString());
		assertEquals(false, matcher.matches("B"));
		assertEquals(true, matcher.matches("BA"));
		assertEquals(true, matcher.matches("A"));
	}

}
