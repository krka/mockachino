package se.mockachino.matchers.matcher;

import org.junit.Test;
import se.mockachino.matchers.Matchers;

import static org.junit.Assert.assertEquals;

public class NotMatcherTest {
	@Test
	public void testSimple() {
		Matcher<Integer> matcher = Matchers.notM(Matchers.eqM(123));
		assertEquals("!123", matcher.asString());
		assertEquals(false, matcher.matches(123));
		assertEquals(true, matcher.matches(124));
	}
}
