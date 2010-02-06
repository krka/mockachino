package se.mockachino.matchers.matcher;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RegexpMatcherTest {
	@Test
	public void testNull() {
		RegexpMatcher matcher = new RegexpMatcher(null);
		assertEquals("regexp(\"null\")", matcher.asString());
		assertEquals(false, matcher.matches(null));
	}

	@Test
	public void testSimple() {
		RegexpMatcher matcher = new RegexpMatcher(".*A.*");
		assertEquals("regexp(\".*A.*\")", matcher.asString());
		assertEquals(false, matcher.matches(null));
		assertEquals(false, matcher.matches("B"));
		assertEquals(true, matcher.matches("BAB"));
		assertEquals(true, matcher.matches("A"));
	}

}
