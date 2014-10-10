package se.testmockachino.matchers.matcher;

import org.junit.Test;
import se.mockachino.matchers.MatchersBase;
import se.mockachino.matchers.matcher.Matcher;

import static org.junit.Assert.assertEquals;

public class AnyMatcherTest {
	@Test
	public void testSimple() {
		Matcher<Integer> matcher = MatchersBase.mAny(Integer.class);
		assertEquals("<Any>", matcher.toString());
		assertEquals(true, matcher.matches(null));
		assertEquals(true, matcher.matches(124));
	}
}
