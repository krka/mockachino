package se.mockachino.matchers;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static se.mockachino.Mockachino.mock;
import static se.mockachino.Mockachino.verify;
import static se.mockachino.matchers.Matchers.*;

public class ArgumentCatcherTest {
	@Test
	public void testSimple() {
		List mock = mock(List.class);
		ArgumentCatcher<Integer> catcher = ArgumentCatcher.create(ClassMatcher.anyInt());
		mock.get(123);
		verify(mock).get(matcher(catcher));
		assertEquals(Integer.valueOf(123), catcher.getValue());
	}
}
