package se.mockachino.matchers.matcher;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static se.mockachino.matchers.MatchersBase.mAnd;
import static se.mockachino.matchers.MatchersBase.mContains;
import static se.mockachino.matchers.MatchersBase.mEq;
import static se.mockachino.matchers.MatchersBase.mOr;

public class AndOrMatcherTest {
	@Test
	public void testSimple() {
		Matcher<Object> orMatcher = mOr();
		assertEquals("false", orMatcher.toString());

		Matcher<Object> andMatcher = mAnd();
		assertEquals("true", andMatcher.toString());

	}

	@Test
	public void testOr() {
		Matcher<String> orMatcher = mOr(mContains("Foo"), mEq("Hello"));
		assertEquals(false, orMatcher.matches("X"));
		assertEquals(true, orMatcher.matches("Hello"));
		assertEquals(true, orMatcher.matches("AFooA"));
		assertEquals("(regexp(\".*Foo.*\") | \"Hello\")", orMatcher.toString());
	}

	@Test
	public void testAnd() {
		Matcher<String> andMatcher = mAnd(mContains("lo"), mContains("Hell"));
		assertEquals(false, andMatcher.matches("aloa"));
		assertEquals(false, andMatcher.matches("Hellas"));
		assertEquals(true, andMatcher.matches("Hello"));
		assertEquals("(regexp(\".*lo.*\") & regexp(\".*Hell.*\"))", andMatcher.toString());
	}


}
