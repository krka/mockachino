package se.mockachino;

import org.junit.Test;
import se.mockachino.matchers.matcher.ArgumentCatcher;
import se.mockachino.matchers.matcher.ClassMatcher;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;
import static se.mockachino.Mockachino.mock;
import static se.mockachino.Mockachino.verifyOnce;
import static se.mockachino.matchers.MatchersBase.match;

public class ArgumentCatcherTest {
	@Test
	public void testSimple() {
		List mock = mock(List.class);
		ArgumentCatcher<Integer> catcher = ArgumentCatcher.create(ClassMatcher.anyInt());
		mock.get(123);
		verifyOnce().on(mock).get(match(catcher));
		assertEquals(Integer.valueOf(123), catcher.getValue());
	}

	@Test
	public void testComplex() {
		List mock = mock(List.class);
		ArgumentCatcher<Integer> catcher = ArgumentCatcher.create(ClassMatcher.anyInt());
		mock.subList(123, 456);
		verifyOnce().on(mock).subList(match(catcher), match(catcher));

		Iterator<Integer> iter = catcher.getValues();

		assertTrue(iter.hasNext());
		assertEquals(Integer.valueOf(123), iter.next());

		assertTrue(iter.hasNext());
		assertEquals(Integer.valueOf(456), iter.next());

		assertFalse(iter.hasNext());

		assertEquals(Integer.valueOf(456), catcher.getValue());

	}

}
