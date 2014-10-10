package se.testmockachino.matchers.matcher;

import org.junit.Test;
import se.mockachino.matchers.MatchersBase;
import se.mockachino.matchers.matcher.Matcher;

import static org.junit.Assert.assertEquals;

public class NotMatcherTest {
	@Test
	public void testSimple() {
		Matcher<Integer> matcher = MatchersBase.mNot(MatchersBase.mEq(123));
		assertEquals("!123", matcher.toString());
		assertEquals(false, matcher.matches(123));
		assertEquals(true, matcher.matches(124));
	}
}
