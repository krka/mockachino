package se.mockachino.unittests;

import org.junit.Test;
import se.mockachino.matchers.matcher.ArgumentCatcher;
import se.mockachino.matchers.matcher.ClassMatcher;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static se.mockachino.Mockachino.*;
import static se.mockachino.matchers.Matchers.*;

public class ArgumentCatcherTest {
	@Test
	public void testSimple() {
		List mock = mock(List.class);
		ArgumentCatcher<Integer> catcher = ArgumentCatcher.create(ClassMatcher.anyInt());
		mock.get(123);
		verifyOnce().on(mock).get(match(catcher));
		assertEquals(Integer.valueOf(123), catcher.getValue());
	}
}
