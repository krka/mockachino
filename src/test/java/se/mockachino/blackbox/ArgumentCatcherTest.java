package se.mockachino.blackbox;

import org.junit.Test;
import se.mockachino.matchers.matcher.ArgumentCatcher;
import se.mockachino.matchers.matcher.ClassMatcher;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static se.mockachino.Mockachino.mock;
import static se.mockachino.Mockachino.verifyOnce;
import static se.mockachino.matchers.Matchers.match;

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

		List<Integer> values = catcher.getValues();

		assertEquals(2, values.size());
		assertEquals(Integer.valueOf(123), values.get(0));
		assertEquals(Integer.valueOf(456), values.get(1));

		assertEquals(Integer.valueOf(456), catcher.getValue());

	}

}
