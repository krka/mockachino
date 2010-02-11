package se.mockachino.matchers.matcher;

import org.junit.Test;
import se.mockachino.matchers.MatchersBase;

import static org.junit.Assert.assertEquals;

public class AnyMatcherTest {
	@Test
	public void testSimple() {
		Matcher<Integer> matcher = MatchersBase.mAny(Integer.class);
		assertEquals("<mAny>", matcher.toString());
		assertEquals(true, matcher.matches(null));
		assertEquals(true, matcher.matches(124));
	}
}
