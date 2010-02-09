package se.mockachino.matchers.matcher;

import org.junit.Test;
import se.mockachino.matchers.Matchers;

import static org.junit.Assert.assertEquals;

public class AnyMatcherTest {
	@Test
	public void testSimple() {
		Matcher<Integer> matcher = Matchers.anyM(Integer.class);
		assertEquals("<any>", matcher.toString());
		assertEquals(true, matcher.matches(null));
		assertEquals(true, matcher.matches(124));
	}
}
